package me.zoulei.frontend.vue.templete;

import lombok.Data;
import me.zoulei.frontend.vue.elements.VueAttr;
import me.zoulei.frontend.vue.elements.VueNode;

/**
 * 2023年9月6日18:40:33 zoulei
 * 表格模板 el-table
 * <div class="info-box">
    <template>
        <div class="common-table">
          <div class="table">
            <el-table
              @row-click="rowClick"
              :data="tableData"
              class="tableBox"
              border
              height="370"
              stripe
              style="width: 100%"
            >
 */
@Data
public class ElTableTpl {
	/**表格对象*/
	VueNode eltable;
	/**需要继续构造的对象*/
	VueNode eltableCONST;
	
	public ElTableTpl(){
		this.createNode();
	}
	
	public VueNode createNode() {
		VueNode div1 = new VueNode("div","表格");
		VueAttr attr = new VueAttr("class","info-box");
		div1.addAttr(attr);
		VueNode template1 = new VueNode("template");
		
		VueNode div2 = new VueNode("div");
		attr = new VueAttr("class","common-table");
		div2.addAttr(attr);
		
		VueNode div3 = new VueNode("div");
		attr = new VueAttr("class","table");
		div3.addAttr(attr);
		
		VueNode div4 = new VueNode("el-table");
		attr = new VueAttr("class","tableBox");
		div4.addAttr(attr)
			.addAttr(new VueAttr("@row-click","rowClick"))
			.addAttr(new VueAttr(":data","tableData"))
			.addAttr(new VueAttr("height","370"))
			.addAttr(new VueAttr("style","width: 100%"))
			.addAttr(new VueAttr("border"))
			.addAttr(new VueAttr("stripe"));
		
		div1.appendChild(template1)
			.appendChild(div2)
			.appendChild(div3)
			.appendChild(div4);
		
		this.eltable = div1;
		this.eltableCONST = div4;
		
		return div1;
	}
	
}









