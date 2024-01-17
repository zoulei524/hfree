

export default {
	name: "a360-page",
	components: {
        
	},
	data() {
		return {
			dataUrl: '/Test', //请求地址
			a360TableData: [],//表格数据对象
			//单条数据对象 用于编辑
			a360EntityData : {
				a36000: "", /** 主键 */
				a0000: "", /** 人员唯一标识符 */
				a36001: {key: "", value: "", p: "E", codetype: "ZB01"}, /** 援派地区 */
				a36002: "", /** 援派部职别 */
				a36003: {key: "", value: "", p: "E", codetype: ""}, /** 开始时间 */
				a36004: {key: "", value: "", p: "E", codetype: ""}, /** 结束时间 */
				a36091: "",
				a36091_str: "", /** 创建时间 */
				a36092: "", /** 创建用户编码 */
				a36094: "",
				a36094_str: "", /** 修改时间 */
				a36095: "", /** 更新用户编码 */
			},
			a360EntityDataText: '',
			
			//这里放布尔类型的变量
			booleanObj: {
				isA360EditDisabled: true,//表格是否可编辑
				isShowA360Form:false,//是否显示编辑区域
			},
			//提示框对象
			dialog: {
				visible: false,//显示隐藏
				wintitle: '',//窗口左上角title
				msg: '',//提示信息
				a36000 : '',//a360主键
			},
			//表单校验规则，只生成不为空校验
			rules: {
				a36001: [
					{ required: true, message: '请输入援派地区', trigger: 'blur' },
				],
				a36002: [
					{ required: true, message: '请输入援派部职别', trigger: 'blur' },
				],
				a36003: [
					{ required: true, message: '请输入开始时间', trigger: 'blur' },
				],
				a36004: [
					{ required: true, message: '请输入结束时间', trigger: 'blur' },
				],

			},

			//分页页面默认20行一页，若不分页默认显示500行
			a360PageInfo: {
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
		this.queryA360List();
		//将空对象序列化成文本，通过反序列化重置对象
		this.a360EntityDataText = JSON.stringify(this.a360EntityData);
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
		rowA360Click(row, column, event) {
			//获取选中条的数据
			this.queryA360Data(row.a36000);
			//表单可编辑并显示保存和取消按钮
			this.booleanObj.isA360EditDisabled = true;
			//显示表单
			this.booleanObj.isShowA360Form = true;
		},
		
		// 表格行双击事件 用于双击编辑
		rowA360DblClick(row, column, event) {
		    //获取选中条的数据
		    this.queryA360Data(row.a36000);
		    this.setEditable();
		},
       
		// 查询列表数据
		queryA360List() {
			let _this = this;
			let url = _this.dataUrl + "/getA360List";
			//请求参数
			let param = {
                pageInfo: this.a360PageInfo,
			};
			_this.$api.commonPost(url, param).then(function (res) {
				if (res.status == "0") {
					//表格数据
					_this.a360TableData = res.data.tableData;
					_this.a360PageInfo = res.data.pageInfo;
				}
			});
		},
		
		//新增
		addA360Info() {
			//表单控件值重置
			this.resetA360Fields();
			//表单可编辑并显示保存和取消按钮
			this.setEditable();
		},

        // 编辑按钮
		handleA360Edit(data) {
			//获取选中条的数据
		    this.queryA360Data(data.a36000);
		    //表单可编辑并显示保存和取消按钮
		    this.setEditable();
		},
		
		//设置可编辑
		setEditable(){
		    //表单可编辑并显示保存和取消按钮
		    this.booleanObj.isA360EditDisabled = false;
		    //显示表单
		    this.booleanObj.isShowA360Form = true;
		},
		
		// 根据主键id查询数据
		queryA360Data(a36000) {
		    let _this = this;
		    let url = _this.dataUrl + "/getA360InfoById";
		    let param = {
		        a36000,
		    };
		    _this.$api.commonPost(url, param).then(function (res) {
		        if (res.status == "0") {
		            _this.a360EntityData = res.data;
					//弹出框是否禁用
					for (let x in _this.a360EntityData) {
						if (_this.a360EntityData[x].p) {
							if (_this.booleanObj.isA360EditDisabled) {
								_this.a360EntityData[x].p = "D";
							} else {
								_this.a360EntityData[x].p = "R";
							}
						}
					}
		        }else{
		            _this.$message.error(res.message);
		        }
		    });
		},

		//保存前校验
		saveA360InfoValidate() {
			this.$refs['a360Form'].validate((valid) => {
				if (valid) {//校验通过保存数据
					this.saveA360Info();
				} else {
					return false;
				}
			});
 
		},

		//保存
		saveA360Info() {
			let _this = this;
			let url = _this.dataUrl + "/saveA360Info";
			let param = {
				a360EntityData: _this.a360EntityData,
			};
			_this.$api.commonPost(url, param).then(function (res) {
				if (res.status == "0") {
					_this.$message.success("保存成功！");
					//重新查询列表
					_this.queryA360List();
					//表单设置不可编辑并隐藏保存和取消按钮
					_this.booleanObj.isA360EditDisabled = true;
					//表单控件值重置
					_this.resetA360Fields();
				} else {
					//显示错误信息
					_this.$message.error(res.message);
					//重新查询列表
					_this.queryA360List();
				}
			});
		},
		
		// 删除
		handleA360Delete() {
			//已经确删除了，这里隐藏提示框
			this.dialog.visible = false
			let _this = this;
			//主键作为参数，根据主键删除
			let param = {
				a36000: this.dialog.a36000,
			};
			let url = _this.dataUrl + "/deleteA360ById";
			_this.$api.commonPost(url, param).then(function (res) {
				if (res.status == "0") {
					//重新加载列表
					_this.queryA360List();
					//表单控件值重置
					_this.resetA360Fields();
					//表单设置不可编辑并隐藏保存和取消按钮
					_this.booleanObj.isA360EditDisabled = true;
					_this.$message.success("删除成功！");
				} else {
					_this.$message.error(res.message);
					return false;
				}
			});
		},
		
		//清空表单
		resetA360Fields() {
			this.a360EntityData = JSON.parse(this.a360EntityDataText);
		},
		
        
	},
};
