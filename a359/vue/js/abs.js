

export default {
	name: "a359-page",
	components: {
        
	},
	data() {
		return {
			dataUrl: '/business/a359', //请求地址
			a359TableData: [],//表格数据对象
			//单条数据对象 用于编辑
			a359EntityData : {
				a35900: "", /** 主键 */
				a0000: "", /** 人员唯一标识符 */
				a35901: {key: "", value: "", p: "E", codetype: "ZZGB132"}, /** 类别 */
				a35902: {key: "", value: "", p: "E", codetype: "A0215A_YJB"}, /** 级别 */
				a35903: {key: "", value: "", p: "E", codetype: ""}, /** 开始时间 */
				a35904: {key: "", value: "", p: "E", codetype: ""}, /** 结束时间 */
				a35991: "",
				a35991_str: "", /** 创建时间 */
				a35992: "", /** 创建用户编码 */
				a35994: "",
				a35994_str: "", /** 修改时间 */
				a35995: "", /** 更新用户编码 */
			},
			a359EntityDataText: '',
			
			//这里放布尔类型的变量
			booleanObj: {
				isA359EditDisabled: true,//表格是否可编辑
				isShowA359Form:false,//是否显示编辑区域
			},
			//提示框对象
			dialog: {
				visible: false,//显示隐藏
				wintitle: '',//窗口左上角title
				msg: '',//提示信息
				a35900 : '',//a359主键
			},
			//表单校验规则，只生成不为空校验
			rules: {

			},

			//分页页面默认20行一页，若不分页默认显示500行
			a359PageInfo: {
				total: 0, //总行数
				pageSize: 20, //每页显示数：分页
				currentPage: 1, //当前页
			},

			//下拉选的代码
			codeTypes: {
				ZZGB132: [], //艰苦地区类别(浙江消防干部)
				A0215A_YJB: [], //专业技术职务(浙江消防干部)

			}, 
               
		};

	},
	
	props:{
        
	},
	
	created(){
		//这里是针对ep-select的代码初始化 没有就注释了
        let param = {
			path:this.dataUrl,
			codeTypes: this.codeTypes,
		};
		sessionStorage.removeItem(this.dataUrl);
		this.$store.dispatch("SET_PATH", this.dataUrl);
		this.$store.dispatch("SET_INIT", param);
	},
	
	mounted() {
		// 查询列表数据
		this.queryA359List();
		//将空对象序列化成文本，通过反序列化重置对象
		this.a359EntityDataText = JSON.stringify(this.a359EntityData);
		//这里数据维护若超出边界，设置高度，通过纵向滚动条显示
		//let ef = $('.elform');
		setTimeout(() => {
			//ef.height(ef.parent().parent().parent().parent().height()-565)
		}, 500);
	},
	
	destroyed(){
		//页面关闭调用父页面的方法  需要调用则去掉注释。 父页面组件传入@closeStMsgwin="XXXfn"
		//this.$emit('closeStMsgwin')
	},
	
	watch: {
        
	},
    
	methods: {
		// 表格行单击事件 用户查看
		rowA359Click(row, column, event) {
			//获取选中条的数据
			this.queryA359Data(row.a35900);
			//表单可编辑并显示保存和取消按钮
			this.booleanObj.isA359EditDisabled = true;
			//显示表单
			this.booleanObj.isShowA359Form = true;
		},
		
		// 表格行双击事件 用于双击编辑
		rowA359DblClick(row, column, event) {
		    //获取选中条的数据
		    this.queryA359Data(row.a35900);
		    this.setEditable();
		},
       
		// 查询列表数据
		queryA359List() {
			let _this = this;
			let url = _this.dataUrl + "/getA359List";
			//请求参数
			let param = {
                pageInfo: this.a359PageInfo,
			};
			_this.$api.commonPost(url, param).then(function (res) {
				if (res.status == "0") {
					//表格数据
					_this.a359TableData = res.data.tableData;
					_this.a359PageInfo = res.data.pageInfo;
				}
			});
		},
		
		//新增
		addA359Info() {
			//表单控件值重置
			this.resetA359Fields();
			//表单可编辑并显示保存和取消按钮
			this.setEditable();
		},

        // 编辑按钮
		handleA359Edit(data) {
			//获取选中条的数据
		    this.queryA359Data(data.a35900);
		    //表单可编辑并显示保存和取消按钮
		    this.setEditable();
		},
		
		//设置可编辑
		setEditable(){
		    //表单可编辑并显示保存和取消按钮
		    this.booleanObj.isA359EditDisabled = false;
		    //显示表单
		    this.booleanObj.isShowA359Form = true;
		},
		
		// 根据主键id查询数据
		queryA359Data(a35900) {
		    let _this = this;
		    let url = _this.dataUrl + "/getA359InfoById";
		    let param = {
		        a35900,
		    };
		    _this.$api.commonPost(url, param).then(function (res) {
		        if (res.status == "0") {
		            _this.a359EntityData = res.data;
					//弹出框是否禁用
					for (let x in _this.a359EntityData) {
						if (_this.a359EntityData[x].p) {
							if (_this.booleanObj.isA359EditDisabled) {
								_this.a359EntityData[x].p = "D";
							} else {
								_this.a359EntityData[x].p = "R";
							}
						}
					}
		        }else{
		            _this.$message.error(res.message);
		        }
		    });
		},

		//保存前校验
		saveA359InfoValidate() {
			this.$refs['a359Form'].validate((valid) => {
				if (valid) {//校验通过保存数据
					this.saveA359Info();
				} else {
					return false;
				}
			});
 
		},

		//保存
		saveA359Info() {
			let _this = this;
			let url = _this.dataUrl + "/saveA359Info";
			let param = {
				a359EntityData: _this.a359EntityData,
			};
			_this.$api.commonPost(url, param).then(function (res) {
				if (res.status == "0") {
					_this.$message.success("保存成功！");
					//重新查询列表
					_this.queryA359List();
					//表单设置不可编辑并隐藏保存和取消按钮
					_this.booleanObj.isA359EditDisabled = true;
					//表单控件值重置
					_this.resetA359Fields();
				} else {
					//显示错误信息
					_this.$message.error(res.message);
					//重新查询列表
					_this.queryA359List();
				}
			});
		},
		
		// 删除
		handleA359Delete() {
			//已经确删除了，这里隐藏提示框
			this.dialog.visible = false
			let _this = this;
			//主键作为参数，根据主键删除
			let param = {
				a35900: this.dialog.a35900,
			};
			let url = _this.dataUrl + "/deleteA359ById";
			_this.$api.commonPost(url, param).then(function (res) {
				if (res.status == "0") {
					//重新加载列表
					_this.queryA359List();
					//表单控件值重置
					_this.resetA359Fields();
					//表单设置不可编辑并隐藏保存和取消按钮
					_this.booleanObj.isA359EditDisabled = true;
					_this.$message.success("删除成功！");
				} else {
					_this.$message.error(res.message);
					return false;
				}
			});
		},
		
		//清空表单
		resetA359Fields() {
			this.a359EntityData = JSON.parse(this.a359EntityDataText);
		},
		
		// 导出全部列表数据到excel
		exportA359Excel() {
			let _this = this;
			let url = _this.dataUrl + "/exportA359Excel";
			//请求参数
			let param = {
                pageInfo: this.a359PageInfo,
			};
			const loading = this.$loading({
				target: $('.info-page')[0],
				lock: true,
				text: '导出中...',
				spinner: 'el-icon-loading',
				background: 'rgba(0, 0, 0, 0.7)'
			});
			_this.$api.commonPost(url, param).then(function (res) {
				loading.close();
				if (res.status == "0") {
					//下载表格
					downLoadFile(res.data.path, res.data.filename);
				}else{
					_this.$message.error(res.message);
				}
			});
		},
        
	},
};
