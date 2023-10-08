package me.zoulei.frontend.templete.grid;

import lombok.Data;
import me.zoulei.Constants;
import me.zoulei.frontend.node.Node;
import me.zoulei.frontend.node.vue.VueAttr;
import me.zoulei.frontend.node.vue.VueNode;

/**
 * 2023年9月6日18:40:33 zoulei
 * 表格模板 el-table
 * 
<template>
  <div class="info-page">
    <div class="titles">
      <p>参会领导信息库</p>
    </div>
    <div class="info-box">
      <template>
            <el-table
              @row-click="rowDblClick"
              :data="tableData"
              header-cell-class-name="headerCell"
              border
              height="450"
              stripe
            >
 */
@Data
public class ElTableTpl {
	/**页面对象，用于生产页面*/
	VueNode page;
	/**表格对象*/
	VueNode el_table;
	/**页面元素对象*/
	Node info_page;
	/**标题内容对象 标题 可往里加按钮*/
	Node title;
	
	public ElTableTpl(){
		this.createNode();
	}
	
	public void createNode() {
		VueNode page = new VueNode("template");
		Node info_page = new VueNode("div").addAttr(new VueAttr("class","info-page"));
		
		Node titles = new VueNode("div").addAttr(new VueAttr("class","titles"));
		VueNode titlesP = new VueNode("p","标题名称","");
		
		page.appendChild(info_page).appendChild(titles).append(titlesP);
		
		Node info_box = new VueNode("div").addAttr(new VueAttr("class","info-box"));
		
		VueNode table = new VueNode("template");
		VueNode el_table = new VueNode("el-table");
		el_table.setAttrNotNewLine(Constants.ATTR_NEW_LINE);
		el_table.addAttr(new VueAttr("@row-click","rowClick"))
		.addAttr(new VueAttr(":data","tableData"))
		.addAttr(new VueAttr("height","450"))
		.addAttr(new VueAttr("header-cell-class-name","headerCell"))
		.addAttr(new VueAttr("border"))
		.addAttr(new VueAttr("stripe"));
		info_box.appendChild(table).append(el_table);
		
		info_page.append(info_box);
		
		
		this.page = page;
		this.el_table = el_table;
		this.info_page = info_page;
		this.title = titles;
		
	}
	
}









