

export default {
	name: "${tablename}-page",
	components: {
        
	},
	data() {
		return {
			tableData: [],//表格数据对象
			dataUrl: '', //请求地址
			//单条数据对象 用于编辑
${entityJSON},
            
		};

	},
	props:{
        
	},
	mounted() {
		this.queryList();
       
	},
	
	watch: {
        
	},
    
	methods: {
		// 行双击事件
		rowDblClick(row, column, event) {
            
		},
       
		// 查询列表数据
		queryList() {
			let _this = this;
			let url = _this.dataUrl + "/get${entity}List";
			//请求参数
			let param = {
                
			};
			_this.$api.commonPost(url, param).then(function (res) {
				if (res.status == "0") {
					_this.tableData = res.data;
				}
			});
		},
        
	},
};
