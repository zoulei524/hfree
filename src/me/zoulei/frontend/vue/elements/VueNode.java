package me.zoulei.frontend.vue.elements;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 2023年9月6日13:09:01   zoulei
 * vue节点对象
 */
@Data
public class VueNode {
	
	/**节点属性 */
	final private List<VueAttr> attrs = new ArrayList<VueAttr>();
	
	/**子节点 */
	final private List<VueNode> childNodes = new ArrayList<VueNode>();
	
	/**父节点 */
	private VueNode parentNode;
	
	/**节点名称 */
	private String name;
	
	/**节点注释 */
	private String comments="";
	
	/**节点注释开始符号 */
	final private String start_symbol = "<!--";
	
	/**节点注释结束符号 */
	final private String end_symbol = "-->";
	
	/**节点内容 */
	private String text="";
	
	
	
	/**
	 * 生成vue的标记语言
	 */
	public String toString() {
		//缩进
		String tabStr = this.getTabs();
		//输出vue标记语言
		StringBuilder vueStr = new StringBuilder();
		//注释
		if(this.getComments().length()>0) {
			vueStr.append(tabStr+this.getStart_symbol()).append(this.getComments()).append(this.getEnd_symbol()).append("\n");
		}
		//开始标签
		vueStr.append(tabStr+"<").append(this.name);
		//属性
		String attrStr = this.toAttrsString();
		if(this.attrs.size()>0) {
			if(this.attrs.size()==1) {
				vueStr.append(" " + attrStr);
				//开始标签结束
				vueStr.append(">\n");
			}else {
				vueStr.append(attrStr);
				//开始标签结束
				vueStr.append(tabStr+">\n");
			}
			
		}else {
			vueStr.append(">\n");
		}
		
		//标签内容
		if(this.text.length()>0) {
			vueStr.append(tabStr+"	").append(this.text).append("\n");
		}
		
		//子节点
		this.childNodes.forEach(cnode->{
			vueStr.append(cnode.toString());
		});
		//结束标签
		vueStr.append(tabStr+"</").append(this.name).append(">\n");
		return vueStr.toString();
	}
	
	/**
	 * 获取缩进
	 * @return
	 */
	public String getTabs() {
		StringBuilder tabStr = new StringBuilder();
		//缩进
		VueNode n = this;
		while((n = n.parentNode)!=null) {
			tabStr.append("	");
		}
		return tabStr.toString();
	}
	
	/**
	 * 生成属性
	 * @return
	 */
	public String toAttrsString() {
		StringBuilder attrsStr = new StringBuilder();
		//缩进
		String tabStr = this.getTabs();
		boolean isaddBlank = this.attrs.size()>1;
		this.attrs.forEach(attr->{
			attrsStr.append(attr.toString(tabStr,isaddBlank)+"\n");
		});
		
		if(this.attrs.size()>1) {//两个以上属性头尾都要换行
			return "\n" + attrsStr.toString();
		}else if(this.attrs.size()==1) {//只有一个属性就连标签一起不换号
			return attrsStr.deleteCharAt(attrsStr.length()-1).toString();
		}else{
			return attrsStr.toString();
		}
		
	}
	
	/**
	 * 增加属性
	 * @param attr
	 * @return 
	 */
	public VueNode addAttr(VueAttr attr) {
		this.attrs.add(attr);
		return this;
	}
	
	/**
	 * 增加子节点  返回子节点
	 * @param attr
	 * @return 
	 */
	public VueNode appendChild(VueNode node) {
		node.parentNode = this;
		this.childNodes.add(node);
		return node;
	}
	
	/**
	 * 增加子节点  返回当前节点
	 * @param attr
	 * @return 
	 */
	public VueNode append(VueNode node) {
		node.parentNode = this;
		this.childNodes.add(node);
		return this;
	}

	public VueNode(String name) {
		super();
		this.name = name;
	}
	public VueNode(String name,String comments) {
		super();
		this.name = name;
		this.comments = comments;
	}
	
	public VueNode(String name,String text,String comments) {
		super();
		this.name = name;
		this.text = text;
		this.comments = comments;
	}
	
}
