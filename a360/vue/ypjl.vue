<template>
	<div class="info-page">
		<div class="titles">
			<p>
				援派经历(a360)
			</p>
			<!--这里放操作按钮-->
			<div>
				<el-button @click="addA360Info">
					<img class="icon" src="@/assets/imgs/zzgb/rygl/icon-bc.png"  />
					&nbsp;添加
				</el-button>
			</div>
		</div>
		<div class="info-box">
			<el-table @row-click="rowA360Click" @row-dblclick="rowA360DblClick" :data="a360TableData" height="450" header-cell-class-name="headerCell" border stripe >
				<!--表格列-->
				<el-table-column type="index" label="序号" width="55" align="center" >
				</el-table-column>
				<!--表格列:援派地区-->
				<el-table-column prop="a36001.value" label="援派地区" width="120" align="center" >
				</el-table-column>
				<!--表格列:援派部职别-->
				<el-table-column prop="a36002" label="援派部职别" width="370" align="center" >
				</el-table-column>
				<!--表格列:开始时间-->
				<el-table-column prop="a36003.time" label="开始时间" width="250" align="center" >
				</el-table-column>
				<!--表格列:结束时间-->
				<el-table-column prop="a36004.time" label="结束时间" width="250" align="center" >
				</el-table-column>
				<!--表格列-->
				<el-table-column label="操作" align="center" >
					<template slot-scope="scope">
						<div style="text-align: center;">
							<el-button @click.stop="handleA360Edit(scope.row)" type="text" size="small" >
								<img class="icon" src="@/assets/imgs/zzgb/grid_icon/edit.svg"  />
								编辑
							</el-button>
							<el-button @click.stop="dialog.a36000=scope.row.a36000;dialog.visible=true;dialog.msg = '确定删除，是否继续？';dialog.wintitle='系统提示';" type="text" size="small" >
								<img class="icon" src="@/assets/imgs/zzgb/grid_icon/del.svg"  />
								删除
							</el-button>
						</div>
					</template>
				</el-table-column>
			</el-table>
		</div>
		<div class="titles" v-if="booleanObj.isShowA360Form" >
			<p>
				详情
			</p>
		</div>
		<el-form :rules="rules" ref="a360Form" :model="a360EntityData" label-width="80px" class="elform" :disabled="booleanObj.isA360EditDisabled" v-if="booleanObj.isShowA360Form" >
			<el-row>
				<epl-public-window-edit
					colspan="8"
					labelWidth="80"
					label="援派地区"
					name="a36001"
					:property="a360EntityData.a36001"
					:codetype="a360EntityData.a36001.codetype"
					placeholder="请选择援派地区"
				>
				</epl-public-window-edit>
				<el-col :span="8">
					<el-form-item label="援派部职别" prop="a36002" >
						<el-input v-model="a360EntityData.a36002" placeholder="请输入援派部职别" >
						</el-input>
					</el-form-item>
				</el-col>
				<epl-timeinput
					colspan="8"
					labelWidth="80"
					label="开始时间"
					name="a36003"
					:property="a360EntityData.a36003"
					placeholder="示例：202301或20230101"
				>
				</epl-timeinput>
			</el-row>
			<el-row>
				<epl-timeinput
					colspan="8"
					labelWidth="80"
					label="结束时间"
					name="a36004"
					:property="a360EntityData.a36004"
					placeholder="示例：202301或20230101"
				>
				</epl-timeinput>
			</el-row>
		</el-form>
		<div style="text-align: center; margin: 10px 0px;">
			<el-button @click="saveA360InfoValidate" v-if="!booleanObj.isA360EditDisabled" >
				<img class="icon" src="@/assets/imgs/zzgb/rygl/bc.png"  />
				&nbsp;保存
			</el-button>
			<el-button @click="booleanObj.isShowA360Form=false;resetA360Fields()" v-if="booleanObj.isA360EditDisabled&&booleanObj.isShowA360Form" >
				<img class="icon" src="@/assets/imgs/zzgb/rygl/qx.png"  />
				&nbsp;关闭
			</el-button>
			<el-button @click="booleanObj.isA360EditDisabled=true;booleanObj.isShowA360Form=false;resetA360Fields()" v-if="!booleanObj.isA360EditDisabled" >
				<img class="icon" src="@/assets/imgs/zzgb/rygl/qx.png"  />
				&nbsp;取消
			</el-button>
		</div>
		<!--弹框提示-->
		<epl-window :hasHelp="false" :title="dialog.wintitle" width="400" height="250" append-to-body :visible.sync="dialog.visible" center >
			<hy-dialog :show-dialog="dialog.visible" :msg="dialog.msg" @closeStMsgwin="dialog.visible = false" @confirmMsg="handleA360Delete" >
			</hy-dialog>
		</epl-window>
	</div>
</template>

<script src="./js/ypjl.js"></script>
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
