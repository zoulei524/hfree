<template>
	<div class="info-page">
		<div class="titles">
			<p>
				艰苦地区工作经历(a359)
			</p>
			<!--这里放操作按钮-->
			<div>
				<el-button @click="addA359Info">
					<img class="icon" src="@/assets/imgs/zzgb/rygl/icon-bc.png"  />
					&nbsp;添加
				</el-button>
				<el-button @click="exportA359Excel">
					<svg class="icon">
						<use xlink:href="#el-icon-gwy-excelmbdy">
						</use>
					</svg>
					&nbsp;导出Excel
				</el-button>
			</div>
		</div>
		<div class="info-box">
			<el-table @row-click="rowA359Click" @row-dblclick="rowA359DblClick" :data="a359TableData" height="450" header-cell-class-name="headerCell" border stripe >
				<!--表格列-->
				<el-table-column type="index" label="序号" width="55" align="center" >
				</el-table-column>
				<!--表格列:类别-->
				<el-table-column prop="a35901.value" label="类别" width="245" align="center" >
				</el-table-column>
				<!--表格列:级别-->
				<el-table-column prop="a35902.value" label="级别" width="216" align="center" >
				</el-table-column>
				<!--表格列:开始时间-->
				<el-table-column prop="a35903.time" label="开始时间" width="216" align="center" >
				</el-table-column>
				<!--表格列:结束时间-->
				<el-table-column prop="a35904.time" label="结束时间" width="274" align="center" >
				</el-table-column>
				<!--表格列-->
				<el-table-column label="操作" align="center" >
					<template slot-scope="scope">
						<div style="text-align: center;">
							<el-button @click.stop="handleA359Edit(scope.row)" type="text" size="small" >
								<img class="icon" src="@/assets/imgs/zzgb/grid_icon/edit.svg"  />
								编辑
							</el-button>
							<el-button @click.stop="dialog.a35900=scope.row.a35900;dialog.visible=true;dialog.msg = '确定删除，是否继续？';dialog.wintitle='系统提示';" type="text" size="small" >
								<img class="icon" src="@/assets/imgs/zzgb/grid_icon/del.svg"  />
								删除
							</el-button>
						</div>
					</template>
				</el-table-column>
			</el-table>
			<!--分页bar-->
			<div style="display: flex; border: 1px solid rgb(220, 223, 230);height: 40px;align-items:center;">
				<div>
					<el-pagination @size-change="queryA359List" @current-change="queryA359List" :current-page.sync="a359PageInfo.currentPage" :page-sizes="[20, 50, 100, 200]" class="pagination" :page-size.sync="a359PageInfo.pageSize" layout="total, sizes, prev, pager, next, jumper" :total.sync="a359PageInfo.total" >
					</el-pagination>
				</div>
				<div style="margin-left: 20px;">
					<el-button @click="queryA359List" type="primary" icon="el-icon-refresh" >
						刷新
					</el-button>
				</div>
			</div>
		</div>
		<div class="titles" v-if="booleanObj.isShowA359Form" >
			<p>
				详情
			</p>
		</div>
		<el-form :rules="rules" ref="a359Form" :model="a359EntityData" label-width="80px" class="elform" :disabled="booleanObj.isA359EditDisabled" v-if="booleanObj.isShowA359Form" >
			<el-row>
				<ep-select
					colspan="8"
					labelWidth="80"
					:custom-path="dataUrl"
					label="类别"
					name="a35901"
					:property="a359EntityData.a35901"
					:codetype="a359EntityData.a35901.codetype"
					placeholder="请选择类别"
				>
				</ep-select>
				<ep-select
					colspan="8"
					labelWidth="80"
					:custom-path="dataUrl"
					label="级别"
					name="a35902"
					:property="a359EntityData.a35902"
					:codetype="a359EntityData.a35902.codetype"
					placeholder="请选择级别"
				>
				</ep-select>
				<epl-timeinput
					colspan="8"
					isHideKey="true"
					labelWidth="80"
					label="开始时间"
					name="a35903"
					:property="a359EntityData.a35903"
					placeholder="示例：202301或20230101"
				>
				</epl-timeinput>
			</el-row>
			<el-row>
				<epl-timeinput
					colspan="8"
					isHideKey="true"
					labelWidth="80"
					label="结束时间"
					name="a35904"
					:property="a359EntityData.a35904"
					placeholder="示例：202301或20230101"
				>
				</epl-timeinput>
			</el-row>
		</el-form>
		<div style="text-align: center; margin: 10px 0px;">
			<el-button @click="saveA359InfoValidate" v-if="!booleanObj.isA359EditDisabled" >
				<img class="icon" src="@/assets/imgs/zzgb/rygl/bc.png"  />
				&nbsp;保存
			</el-button>
			<el-button @click="booleanObj.isShowA359Form=false;resetA359Fields()" v-if="booleanObj.isA359EditDisabled&&booleanObj.isShowA359Form" >
				<img class="icon" src="@/assets/imgs/zzgb/rygl/qx.png"  />
				&nbsp;关闭
			</el-button>
			<el-button @click="booleanObj.isA359EditDisabled=true;booleanObj.isShowA359Form=false;resetA359Fields()" v-if="!booleanObj.isA359EditDisabled" >
				<img class="icon" src="@/assets/imgs/zzgb/rygl/qx.png"  />
				&nbsp;取消
			</el-button>
		</div>
		<!--弹框提示-->
		<epl-window :hasHelp="false" :title="dialog.wintitle" width="400" height="250" append-to-body :visible.sync="dialog.visible" center >
			<hy-dialog :show-dialog="dialog.visible" :msg="dialog.msg" @closeStMsgwin="dialog.visible = false" @confirmMsg="handleA359Delete" >
			</hy-dialog>
		</epl-window>
	</div>
</template>

<script src="./js/abs.js"></script>
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
			.pagination{
				float: none;
				margin-top: 0px;
				height: 40px;
			}
			/deep/.pagination .el-pager li:not(.disabled).active{
				background-color: #409EFF;
				color: #FFF;
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
