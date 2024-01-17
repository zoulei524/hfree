<template>
	<div class="info-page">
		<div class="titles">
			<p>
				(aa01)
			</p>
			<!--这里放操作按钮-->
			<div>
				<el-button @click="addAa01Info">
					<img class="icon" src="@/assets/imgs/zzgb/rygl/icon-bc.png"  />
					&nbsp;添加
				</el-button>
			</div>
		</div>
		<div class="info-box">
			<el-table @row-click="rowAa01Click" @row-dblclick="rowAa01DblClick" :data="aa01TableData" height="450" header-cell-class-name="headerCell" border stripe >
				<!--表格列-->
				<el-table-column type="index" label="序号" width="55" align="center" >
				</el-table-column>
				<!--表格列:参数类别名称-->
				<el-table-column prop="aaa002" label="参数类别名称" width="120" align="center" >
				</el-table-column>
				<!--表格列:参数值-->
				<el-table-column prop="aaa005" label="参数值" width="120" align="center" >
				</el-table-column>
				<!--表格列:参数值说明描述-->
				<el-table-column prop="aaa105" label="参数值说明描述" width="120" align="center" >
				</el-table-column>
				<!--表格列:参数类别代码是否可维护标志代码-->
				<el-table-column prop="aaa104" label="参数类别代码是否可维护标志代码" width="120" align="center" >
				</el-table-column>
				<!--表格列:是否有效标志代码-->
				<el-table-column prop="active" label="是否有效标志代码" width="120" align="center" >
				</el-table-column>
				<!--表格列:上一版本参数值-->
				<el-table-column prop="oldparam" label="上一版本参数值" width="120" align="center" >
				</el-table-column>
				<!--表格列-->
				<el-table-column label="操作" align="center" >
					<template slot-scope="scope">
						<div style="text-align: center;">
							<el-button @click.stop="handleAa01Edit(scope.row)" type="text" size="small" >
								<img class="icon" src="@/assets/imgs/zzgb/grid_icon/edit.svg"  />
								编辑
							</el-button>
							<el-button @click.stop="dialog.aaa001=scope.row.aaa001;dialog.visible=true;dialog.msg = '确定删除，是否继续？';dialog.wintitle='系统提示';" type="text" size="small" >
								<img class="icon" src="@/assets/imgs/zzgb/grid_icon/del.svg"  />
								删除
							</el-button>
						</div>
					</template>
				</el-table-column>
			</el-table>
		</div>
		<div class="titles" v-if="booleanObj.isShowAa01Form" >
			<p>
				详情
			</p>
		</div>
		<el-form :rules="rules" ref="aa01Form" :model="aa01EntityData" label-width="80px" class="elform" :disabled="booleanObj.isAa01EditDisabled" v-if="booleanObj.isShowAa01Form" >
			<el-row>
				<el-col :span="8">
					<el-form-item label="参数类别名称" prop="aaa002" >
						<el-input v-model="aa01EntityData.aaa002" placeholder="请输入参数类别名称" >
						</el-input>
					</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="参数值" prop="aaa005" >
						<el-input v-model="aa01EntityData.aaa005" placeholder="请输入参数值" >
						</el-input>
					</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="参数值说明描述" prop="aaa105" >
						<el-input v-model="aa01EntityData.aaa105" placeholder="请输入参数值说明描述" >
						</el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="8">
					<el-form-item label="参数类别代码是否可维护标志代码" prop="aaa104" >
						<el-input v-model="aa01EntityData.aaa104" placeholder="请输入参数类别代码是否可维护标志代码" >
						</el-input>
					</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="是否有效标志代码" prop="active" >
						<el-input v-model="aa01EntityData.active" placeholder="请输入是否有效标志代码" >
						</el-input>
					</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="上一版本参数值" prop="oldparam" >
						<el-input v-model="aa01EntityData.oldparam" placeholder="请输入上一版本参数值" >
						</el-input>
					</el-form-item>
				</el-col>
			</el-row>
		</el-form>
		<div style="text-align: center; margin: 10px 0px;">
			<el-button @click="saveAa01InfoValidate" v-if="!booleanObj.isAa01EditDisabled" >
				<img class="icon" src="@/assets/imgs/zzgb/rygl/bc.png"  />
				&nbsp;保存
			</el-button>
			<el-button @click="booleanObj.isShowAa01Form=false;resetAa01Fields()" v-if="booleanObj.isAa01EditDisabled&&booleanObj.isShowAa01Form" >
				<img class="icon" src="@/assets/imgs/zzgb/rygl/qx.png"  />
				&nbsp;关闭
			</el-button>
			<el-button @click="booleanObj.isAa01EditDisabled=true;booleanObj.isShowAa01Form=false;resetAa01Fields()" v-if="!booleanObj.isAa01EditDisabled" >
				<img class="icon" src="@/assets/imgs/zzgb/rygl/qx.png"  />
				&nbsp;取消
			</el-button>
		</div>
		<!--弹框提示-->
		<epl-window :hasHelp="false" :title="dialog.wintitle" width="400" height="250" append-to-body :visible.sync="dialog.visible" center >
			<hy-dialog :show-dialog="dialog.visible" :msg="dialog.msg" @closeStMsgwin="dialog.visible = false" @confirmMsg="handleAa01Delete" >
			</hy-dialog>
		</epl-window>
	</div>
</template>

<script src="./js/gZ.js"></script>
<style lang="less" scoped >
	.info-page{
		padding: 0px 20px;
		overflow: hidden;
		width: 100%;
		.titles{
			height: 50px;
			line-height: 50px;
			color: #4098ff;
			border-bottom: 1px solid #4098ff;
			font-size: 16px;
			/*”弹性布局”，用来为盒状模型提供最大的灵活性*/
			display: flex;
			/*两端对齐，项目之间的间隔都相等。*/
			justify-content: space-between;
			/*垂直中点对齐*/
			align-items: center;
			p{
				display: flex;
				align-items: center;
				&::before{
					content: '';
					display: block;
					width: 5px;
					height: 18px;
					border-radius: 3px;
					background: #4098ff;
					margin-right: 10px;
				}
			}
		}
		.info-box{

			/*表头样式*/
			/deep/.headerCell{
				padding: 0 0 0 0 !important;
				height: 40px;
				color: #555;
				font-weight: normal;
				background-image: linear-gradient(180deg, #ffffff 0%, #ededed 100% ) !important;
			}
			/*表格单元格内按钮样式*/
			/deep/.el-table__body .el-button--text span{
				display: inline-block;
			}
			/deep/.el-table td{
				padding: 2px 0px;
			}
		}
		/*编辑区域的滚动条，高度在mounted里js计算*/
		.elform{
			margin-top: 10px;
			overflow-y: auto;
			overflow-x: hidden;
			/*表单背景颜色*/
			background: #f4f4f4;
			padding-top: 15px;
			/*表单控件的行样式*/
			/deep/.el-row{
				display: flex;
				align-items: center;
				/*标签及input的父级*/
				.el-form-item{
					display: flex;
					align-items: center;
					/*标签*/
					.el-form-item__label{
						line-height: unset;
					}
					/*输入框*/
					.el-form-item__content{
						width: calc(100% - 90px);
						margin-left: 0px !important;
						/*下拉选类型的输入框*/
						.el-select{
							width: 100%;
						}
					}
				}
			}
		}
	}
</style>
