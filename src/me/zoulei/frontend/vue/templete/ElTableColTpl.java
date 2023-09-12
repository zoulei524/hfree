package me.zoulei.frontend.vue.templete;

import me.zoulei.frontend.vue.elements.VueAttr;
import me.zoulei.frontend.vue.elements.VueNode;

/**
 * 2023年9月6日18:39:35  zoulei
 * 表格中的列 el-table-column
 * <el-table-column
    prop="yb001"
    label="序号"
    width="90"
    align="center"
   >
 */
public class ElTableColTpl {
	
	public static VueNode getTPL() {
		VueNode div1 = new VueNode("el-table-column","表格列");
		div1.addAttr(new VueAttr("width","90"));
		div1.addAttr(new VueAttr("align","center"));
		return div1;
	}
	
}
