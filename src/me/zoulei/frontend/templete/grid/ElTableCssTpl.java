package me.zoulei.frontend.templete.grid;

import lombok.Data;
import me.zoulei.frontend.node.Node;
import me.zoulei.frontend.node.css.CSSAttr;
import me.zoulei.frontend.node.css.CSSClassNameNode;
import me.zoulei.frontend.node.vue.VueAttr;
import me.zoulei.frontend.node.vue.VueNode;

/**
 * 
* @author zoulei 
* @date 2023年9月28日 下午3:26:19 
* @description 生成表格相关的样式
 */

@Data
public class ElTableCssTpl {
	
	
	private CSSClassNameNode info_page;
	private CSSClassNameNode titles;
	private CSSClassNameNode info_box;
	private Node styleTag;

	public ElTableCssTpl(){
		this.createNode();
	}
	
	public void createNode() {
		VueNode styleTag = (VueNode) new VueNode("style").addAttr(new VueAttr("lang","less")).addAttr(new VueAttr("scoped"));
		styleTag.setAttrNotNewLine(true);
		this.styleTag = styleTag;
		CSSClassNameNode info_page = new CSSClassNameNode("info-page");
		styleTag.append(info_page);
		info_page.addAttr(new CSSAttr("padding", "0px 20px"));
		info_page.addAttr(new CSSAttr("overflow", "hidden"));
		info_page.addAttr(new CSSAttr("width", "100%"));
		this.info_page = info_page;
		
		CSSClassNameNode titles = new CSSClassNameNode("titles");
		info_page.append(titles);
		titles.addAttr(new CSSAttr("height", "50px"));
		titles.addAttr(new CSSAttr("line-height", "50px"));
		titles.addAttr(new CSSAttr("color", "#4098ff"));
		titles.addAttr(new CSSAttr("border-bottom", "1px solid #4098ff;"));
		titles.addAttr(new CSSAttr("font-size", "16px"));
		titles.addAttr(new CSSAttr("display", "flex","”弹性布局”，用来为盒状模型提供最大的灵活性"));
		titles.addAttr(new CSSAttr("justify-content", "space-between","两端对齐，项目之间的间隔都相等。"));
		titles.addAttr(new CSSAttr("align-items", "center","垂直中点对齐"));
		this.titles = titles;
		
		CSSClassNameNode p = new CSSClassNameNode("p");
		p.setPrefix("");//前缀不要加“.”了
		titles.append(p);
		p.addAttr(new CSSAttr("display", "flex"));
		p.addAttr(new CSSAttr("align-items", "center"));
		
		CSSClassNameNode before = new CSSClassNameNode("before");
		before.setPrefix("&::");//前缀不要加“.”了
		p.append(before);
		before.addAttr(new CSSAttr("content", ""));
		before.addAttr(new CSSAttr("display", "block"));
		before.addAttr(new CSSAttr("width", "5px"));
		before.addAttr(new CSSAttr("height", "18px"));
		before.addAttr(new CSSAttr("border-radius", "3px"));
		before.addAttr(new CSSAttr("background", "#4098ff"));
		before.addAttr(new CSSAttr("margin-right", "10px"));
		
		CSSClassNameNode info_box = new CSSClassNameNode("info-box");
		this.info_box = info_box;
		info_page.append(info_box);
		CSSClassNameNode headerCell = new CSSClassNameNode("headerCell","表头样式");
		headerCell.setPrefix("/deep/.");//前缀不要加“.”了
		info_box.append(headerCell);
		headerCell.addAttr(new CSSAttr("padding", "0 0 0 0 !important"));
		headerCell.addAttr(new CSSAttr("height", "40px"));
		headerCell.addAttr(new CSSAttr("color", "#555"));
		headerCell.addAttr(new CSSAttr("font-weight", "normal"));
		headerCell.addAttr(new CSSAttr("background-image", "linear-gradient(180deg, #ffffff 0%, #ededed 100% ) !important"));
		
	}
}
/*
<style lang="less" scoped>

.info-page {
  padding: 0px 20px;
  overflow: hidden;
  width: 100%;
  .titles {
    height: 50px;
    line-height: 50px;
    color: #4098ff;
    border-bottom: 1px solid #4098ff;
    font-size: 16px;
    //”弹性布局”，用来为盒状模型提供最大的灵活性
    display: flex;
    //两端对齐，项目之间的间隔都相等。
    justify-content: space-between;
    // 垂直中点对齐
    align-items: center;
    p {
      display: flex;
      align-items: center;
      &::before {
        content: "";
        display: block;
        width: 5px;
        height: 18px;
        border-radius: 3px;
        background: #4098ff;
        margin-right: 10px;
      }
    }
  }
  .info-box{
  	//表头样式
	/deep/.headerCell {
	  padding: 0 0 0 0 !important;
	  height: 40px;
	  color: #555;
	  font-weight: normal;
	  background-image: linear-gradient(
	    180deg,
	    #ffffff 0%,
	    #ededed 100%
	  ) !important;
	}
  }
  
}
</style>



*/