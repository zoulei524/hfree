package me.zoulei.dbc.ui.components.center;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 * 2023年11月24日17:00:18 zoulei
 * 放结果日志的文本域
 */
public class ResultsLog extends JPanel{
	
	private static final long serialVersionUID = -7587632831245041902L;

	/**
	 * 日志：
	 */
	public RSyntaxTextArea textAreaLog;

	/**
	 * n库1中多的表和字段
	 */
	public RSyntaxTextArea textAreaDDL2;

	/**
	 * 库2中多的表和字段
	 */
	public RSyntaxTextArea textAreaDDL1;
	
	public ResultsLog() {
		//this.setBackground(Color.red);
//		TitledBorder  blackline = BorderFactory.createTitledBorder("");
//		blackline.setTitleFont(new Font("黑体", Font.PLAIN,18));
//		blackline.setBorder(new LineBorder(new Color(184, 207, 229),2));
//		this.setBorder(blackline);
		
		GridBagConstraints constraints=new GridBagConstraints();
		GridBagLayout gbaglayout=new GridBagLayout(); 
		this.setLayout(gbaglayout);
		//放日志的文本域
		this.textAreaLog = new RSyntaxTextArea("/*\n日志：\n*/\n");
		//textAreaLog.setTextMode(RTextArea.INSERT_MODE);
		this.textAreaLog.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
		RTextScrollPane pLog = new RTextScrollPane(textAreaLog);
		constraints.fill = GridBagConstraints.BOTH;    //组件填充显示区域
        //constraints.weightx=0.0;    //恢复默认值
        //constraints.gridwidth = GridBagConstraints.REMAINDER;    //结束行
		constraints.weightx=1;    // 指定组件的分配区域
        constraints.weighty=1;
        constraints.gridwidth=1;
        gbaglayout.setConstraints(pLog, constraints);
		this.add(pLog);
		
		//放库1的文本域
		this.textAreaDDL1 = new RSyntaxTextArea("/*\n库1中多的表和字段：\n*/\n");
		//textAreaDDL1.setTextMode(RTextArea.INSERT_MODE);
		this.textAreaDDL1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
		RTextScrollPane pDDL1 = new RTextScrollPane(textAreaDDL1);
		
        gbaglayout.setConstraints(pDDL1, constraints);
		this.add(pDDL1);
		
		
		//放库2的文本域
		this.textAreaDDL2 = new RSyntaxTextArea("/*\n库2中多的表和字段：\n*/\n");
		//textAreaDDL2.setTextMode(RTextArea.INSERT_MODE);
		this.textAreaDDL2.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
		RTextScrollPane pDDL2 = new RTextScrollPane(textAreaDDL2);
		gbaglayout.setConstraints(pDDL2, constraints);
		this.add(pDDL2);
		
		
	}
	
	
}
