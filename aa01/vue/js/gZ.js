

export default {
	name: "aa01-page",
	components: {
        
	},
	data() {
		return {
			dataUrl: '/business/aa01', //请求地址
			aa01TableData: [],//表格数据对象
			//单条数据对象 用于编辑
			aa01EntityData : {
				aaa001: "", /** 参数类别代码 */
				aaa002: "", /** 参数类别名称 */
				aaa005: "", /** 参数值 */
				aaa105: "", /** 参数值说明描述 */
				aaa104: "", /** 参数类别代码是否可维护标志代码 */
				active: "", /** 是否有效标志代码 */
				oldparam: "", /** 上一版本参数值 */
			},
			aa01EntityDataText: '',
			
			//这里放布尔类型的变量
			booleanObj: {
				isAa01EditDisabled: true,//表格是否可编辑
				isShowAa01Form:false,//是否显示编辑区域
			},
			//提示框对象
			dialog: {
				visible: false,//显示隐藏
				wintitle: '',//窗口左上角title
				msg: '',//提示信息
				aaa001 : '',//aa01主键
			},
			//表单校验规则，只生成不为空校验
			rules: {

			},

			//分页页面默认20行一页，若不分页默认显示500行
			aa01PageInfo: {
				total: 0, //总行数
				pageSize: 500, //每页显示数：不分页
				currentPage: 1, //当前页
			},

			//下拉选的代码
			codeTypes: {

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
		//sessionStorage.removeItem(this.dataUrl);
		//this.$store.dispatch("SET_PATH", this.dataUrl);
		//this.$store.dispatch("SET_INIT", param);
	},
	
	mounted() {
		// 查询列表数据
		this.queryAa01List();
		//将空对象序列化成文本，通过反序列化重置对象
		this.aa01EntityDataText = JSON.stringify(this.aa01EntityData);
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
		rowAa01Click(row, column, event) {
			//获取选中条的数据
			this.queryAa01Data(row.aaa001);
			//表单可编辑并显示保存和取消按钮
			this.booleanObj.isAa01EditDisabled = true;
			//显示表单
			this.booleanObj.isShowAa01Form = true;
		},
		
		// 表格行双击事件 用于双击编辑
		rowAa01DblClick(row, column, event) {
		    //获取选中条的数据
		    this.queryAa01Data(row.aaa001);
		    this.setEditable();
		},
       
		// 查询列表数据
		queryAa01List() {
			let _this = this;
			let url = _this.dataUrl + "/getAa01List";
			//请求参数
			let param = {
                pageInfo: this.aa01PageInfo,
			};
			_this.$api.commonPost(url, param).then(function (res) {
				if (res.status == "0") {
					//表格数据
					_this.aa01TableData = res.data.tableData;
					_this.aa01PageInfo = res.data.pageInfo;
				}
			});
		},
		
		//新增
		addAa01Info() {
			//表单控件值重置
			this.resetAa01Fields();
			//表单可编辑并显示保存和取消按钮
			this.setEditable();
		},

        // 编辑按钮
		handleAa01Edit(data) {
			//获取选中条的数据
		    this.queryAa01Data(data.aaa001);
		    //表单可编辑并显示保存和取消按钮
		    this.setEditable();
		},
		
		//设置可编辑
		setEditable(){
		    //表单可编辑并显示保存和取消按钮
		    this.booleanObj.isAa01EditDisabled = false;
		    //显示表单
		    this.booleanObj.isShowAa01Form = true;
		},
		
		// 根据主键id查询数据
		queryAa01Data(aaa001) {
		    let _this = this;
		    let url = _this.dataUrl + "/getAa01InfoById";
		    let param = {
		        aaa001,
		    };
		    _this.$api.commonPost(url, param).then(function (res) {
		        if (res.status == "0") {
		            _this.aa01EntityData = res.data;
					//弹出框是否禁用
					for (let x in _this.aa01EntityData) {
						if (_this.aa01EntityData[x].p) {
							if (_this.booleanObj.isAa01EditDisabled) {
								_this.aa01EntityData[x].p = "D";
							} else {
								_this.aa01EntityData[x].p = "R";
							}
						}
					}
		        }else{
		            _this.$message.error(res.message);
		        }
		    });
		},

		//保存前校验
		saveAa01InfoValidate() {
			this.$refs['aa01Form'].validate((valid) => {
				if (valid) {//校验通过保存数据
					this.saveAa01Info();
				} else {
					return false;
				}
			});
 
		},

		//保存
		saveAa01Info() {
			let _this = this;
			let url = _this.dataUrl + "/saveAa01Info";
			let param = {
				aa01EntityData: _this.aa01EntityData,
			};
			_this.$api.commonPost(url, param).then(function (res) {
				if (res.status == "0") {
					_this.$message.success("保存成功！");
					//重新查询列表
					_this.queryAa01List();
					//表单设置不可编辑并隐藏保存和取消按钮
					_this.booleanObj.isAa01EditDisabled = true;
					//表单控件值重置
					_this.resetAa01Fields();
				} else {
					//显示错误信息
					_this.$message.error(res.message);
					//重新查询列表
					_this.queryAa01List();
				}
			});
		},
		
		// 删除
		handleAa01Delete() {
			//已经确删除了，这里隐藏提示框
			this.dialog.visible = false
			let _this = this;
			//主键作为参数，根据主键删除
			let param = {
				aaa001: this.dialog.aaa001,
			};
			let url = _this.dataUrl + "/deleteAa01ById";
			_this.$api.commonPost(url, param).then(function (res) {
				if (res.status == "0") {
					//重新加载列表
					_this.queryAa01List();
					//表单控件值重置
					_this.resetAa01Fields();
					//表单设置不可编辑并隐藏保存和取消按钮
					_this.booleanObj.isAa01EditDisabled = true;
					_this.$message.success("删除成功！");
				} else {
					_this.$message.error(res.message);
					return false;
				}
			});
		},
		
		//清空表单
		resetAa01Fields() {
			this.aa01EntityData = JSON.parse(this.aa01EntityDataText);
		},
		
        
	},
};
