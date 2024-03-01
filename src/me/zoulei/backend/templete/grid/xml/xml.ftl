	<!--${tablename}（${tablecomment}）列表查询-->
	<sql method="get${entity}List" sqlid="get${entity}List">
		<oracle>
			${listDataSQL}
		</oracle>
		<mysql>
			${listDataSQL}
		</mysql>
		<dm>
			${listDataSQL}
		</dm>
		<KingBase></KingBase>
		<gbase></gbase>
	</sql>
	
	<!--获取代码-->
	<sql method="initCodeType" sqlid="initCodeType">
		<oracle>
			select code_value key, code_name value from code_value where code_type='<#noparse>${codeType}</#noparse>'
			<#noparse>${filter}</#noparse>
		</oracle>
		<mysql>
			select code_value `key`, code_name `value` from code_value where code_type='<#noparse>${codeType}</#noparse>'
			<#noparse>${filter}</#noparse>
		</mysql>
		<dm>
			select code_value key, code_name value from code_value where code_type='<#noparse>${codeType}</#noparse>'
			<#noparse>${filter}</#noparse>
		</dm>
		<KingBase></KingBase>
		<gbase></gbase>
	</sql>