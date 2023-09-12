package me.zoulei.frontend.css;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
/**
 * 2023年9月7日10:14:06 zoulei
 * css对象
 */
@Data
public class CSSClassNameNode{

	public CSSClassNameNode(String name, String comments) {
		this.name = name;
		this.comments = comments;
	}

	public CSSClassNameNode(String name) {
		this.name = name;
	}
	
	/**节点名称 */
	private String name;
	
	/**节点注释 */
	private String comments="";
	
	/**节点注释开始符号 */
	final private String start_symbol = "<!--";
	
	/**节点注释结束符号 */
	final private String end_symbol = "-->";
	
	/**节点属性 */
	final private List<CSSAttr> attrs = new ArrayList<CSSAttr>();
	
	/**子节点 */
	final private List<CSSClassNameNode> childNodes = new ArrayList<CSSClassNameNode>();
	
	/**父节点 */
	private CSSClassNameNode parentNode;
	
	
	
	/**
	 * 获取缩进
	 * @return
	 */
	public String getTabs() {
		StringBuilder tabStr = new StringBuilder();
		//缩进
		CSSClassNameNode n = this;
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
		this.attrs.forEach(attr->{
			attrsStr.append(attr.toString(tabStr)+"\n");
		});
		if(this.attrs.size()==0) {
			return "\n";
		}
		return attrsStr.toString();
		
	}
	
	
	/**
	 * 生成vue的css脚本
	 */
	public String toString() {
		//缩进
		String tabStr = this.getTabs();
		//输出vue标记语言
		StringBuilder cssStr = new StringBuilder();
		//注释
		if(this.getComments().length()>0) {
			cssStr.append(tabStr+this.getStart_symbol()).append(this.getComments()).append(this.getEnd_symbol()).append("\n");
		}
		//开始样式
		cssStr.append(tabStr).append("."+this.name+"{").append("\n");
		//属性
		String attrStr = this.toAttrsString();
		cssStr.append(attrStr);
		
		//子节点
		this.childNodes.forEach(cnode->{
			cssStr.append(cnode.toString());
		});
		//结束
		cssStr.append(tabStr+"}\n");
		
		
		return cssStr.toString();
	}
	
	/**
	 * 增加属性
	 * @param attr
	 * @return 
	 */
	public CSSClassNameNode addAttr(CSSAttr attr) {
		this.attrs.add(attr);
		return this;
	}
	
	/**
	 * 增加子节点  返回子节点
	 * @param attr
	 * @return 
	 */
	public CSSClassNameNode appendChild(CSSClassNameNode node) {
		node.parentNode = this;
		this.childNodes.add(node);
		return node;
	}
	
	/**
	 * 增加子节点  返回当前节点
	 * @param attr
	 * @return 
	 */
	public CSSClassNameNode append(CSSClassNameNode node) {
		node.parentNode = this;
		this.childNodes.add(node);
		return this;
	}

}
