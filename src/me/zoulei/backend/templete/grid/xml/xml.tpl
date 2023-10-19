	<!--${tablename}（${tablecomment}）列表查询-->
	<sql method="get${entity}List" sqlid="get${entity}List">
		<oracle></oracle>
		<mysql></mysql>
		<dm>
			${listDataSQL}
		</dm>
		<KingBase></KingBase>
		<gbase></gbase>
	</sql>