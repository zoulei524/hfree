

export default {
	name: "${tablename}-page",
	components: {
        
	},
	data() {
		return {
			dataUrl: '${config.dataUrl}', //请求地址
			${tablename}TableData: [],//表格数据对象
			//单条数据对象 用于编辑
${entityJSON},
			${tablename}EntityDataText: '',
			
<#if config.iscrud>
			//这里放布尔类型的变量
			booleanObj: {
				is${entity}EditDisabled: true,//表格是否可编辑
			},
			//提示框对象
			dialog: {
				visible: false,//显示隐藏
				wintitle: '',//窗口左上角title
				msg: '',//提示信息
				${config.pk} : '',//${tablename}主键
			},
			//表单校验规则，只生成不为空校验
			rules: {
${config.rules}
			},
</#if>      

			//分页页面默认20行一页，若不分页默认显示500行
			${tablename}PageInfo: {
				total: 0, //总行数
				pageSize: ${config.pagination?then(20,500)}, //每页显示数：${config.pagination?then('分页','不分页')}
				currentPage: 1, //当前页
			}
      
                
		};

	},
	
	props:{
        
	},
	
	mounted() {
		// 查询列表数据
		this.query${entity}List();
<#if config.iscrud>
		//将空对象序列化成文本，通过反序列化重置对象
		this.${tablename}EntityDataText = JSON.stringify(this.${tablename}EntityData);
		//这里数据维护若超出边界，可以设置高度使其换行
		//let ef = $('.elform');
		//ef.height(ef.parent().parent().parent().parent().height()-565)
</#if>      
	},
	
	destroyed(){
		//页面关闭调用父页面的方法  需要调用则去掉注释。 父页面组件传入@closeStMsgwin="XXXfn"
		//this.$emit('closeStMsgwin')
	},
	
	watch: {
        
	},
    
	methods: {
		// 表格行单击事件
		row${entity}Click(row, column, event) {
            
		},
       
		// 查询列表数据
		query${entity}List() {
			let _this = this;
			let url = _this.dataUrl + "/get${entity}List";
			//请求参数
			let param = {
                pageInfo: this.${tablename}PageInfo,
			};
			_this.$api.commonPost(url, param).then(function (res) {
				if (res.status == "0") {
					//表格数据
					_this.${tablename}TableData = res.data.tableData;
					_this.${tablename}PageInfo = res.data.pageInfo;
				}
			});
		},
		
<#if config.iscrud>		
		//新增
		add${entity}Info() {
			//表单控件值重置
			this.reset${entity}Fields();
			//表单可编辑并显示保存和取消按钮
			this.booleanObj.is${entity}EditDisabled = false;
		},

        // 编辑按钮
		handle${entity}Edit(data) {
			//获取选中条的数据
		    this.query${entity}Data(data.${config.pk});
		    //表单可编辑并显示保存和取消按钮
		    this.booleanObj.is${entity}EditDisabled = false;
		},
		
		// 根据主键id查询数据
		query${entity}Data(${config.pk}) {
		    let _this = this;
		    let url = _this.dataUrl + "/get${entity}InfoById";
		    let param = {
		        ${config.pk},
		    };
		    _this.$api.commonPost(url, param).then(function (res) {
		        if (res.status == "0") {
		            _this.${tablename}EntityData = res.data;
		        }else{
		            _this.$message.error(res.message);
		        }
		    });
		},

		//保存前校验
		save${entity}InfoValidate() {
			this.$refs['${tablename}Form'].validate((valid) => {
				if (valid) {//校验通过保存数据
					this.save${entity}Info();
				} else {
					return false;
				}
			});
 
		},

		//保存
		save${entity}Info() {
			let _this = this;
			let url = _this.dataUrl + "/save${entity}Info";
			let param = {
				${tablename}EntityData: _this.${tablename}EntityData,
			};
			_this.$api.commonPost(url, param).then(function (res) {
				if (res.status == "0") {
					_this.$message.success("保存成功！");
					//重新查询列表
					_this.query${entity}List();
					//表单设置不可编辑并隐藏保存和取消按钮
					_this.booleanObj.is${entity}EditDisabled = true;
					//表单控件值重置
					_this.reset${entity}Fields();
				} else {
					//显示错误信息
					_this.$message.error(res.message);
					//重新查询列表
					_this.query${entity}List();
				}
			});
		},
		
		// 删除
		handle${entity}Delete() {
			//已经确删除了，这里隐藏提示框
			this.dialog.visible = false
			let _this = this;
			//主键作为参数，根据主键删除
			let param = {
				${config.pk}: this.dialog.${config.pk},
			};
			let url = _this.dataUrl + "/delete${entity}ById";
			_this.$api.commonPost(url, param).then(function (res) {
				if (res.status == "0") {
					//重新加载列表
					_this.query${entity}List();
					//表单控件值重置
					_this.reset${entity}Fields();
					//表单设置不可编辑并隐藏保存和取消按钮
					_this.booleanObj.is${entity}EditDisabled = true;
					_this.$message.success("删除成功！");
				} else {
					_this.$message.error(res.message);
					return false;
				}
			});
		},
		
		//清空表单
		reset${entity}Fields() {
            this.${tablename}EntityData = JSON.parse(this.${tablename}EntityDataText);
        },
</#if> 		
		
        
	},
};
