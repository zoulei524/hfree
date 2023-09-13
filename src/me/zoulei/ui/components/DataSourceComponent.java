package me.zoulei.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import me.zoulei.backend.jdbc.datasource.DataSource;
import me.zoulei.exception.myException;

public class DataSourceComponent {

	public void setComp(JFrame mainFrame) {
		JLabel  dslabel= new JLabel("数据库: ", JLabel.LEFT);
		JTextField dsText = new JTextField("达梦",5);
		
		JLabel  urllabel= new JLabel("jdbc-URL: ", JLabel.LEFT);
		JTextField urlText = new JTextField("jdbc:dm://10.32.184.100:5236?useServerPrepStmts=true",35);
		
		JLabel  namelabel= new JLabel("用户名: ", JLabel.RIGHT);
		JTextField userText = new JTextField("HY_GBGL_ZZGB",12);
		
		JLabel  passwordLabel = new JLabel("密码: ", JLabel.CENTER);
		JTextField passwordText = new JTextField("HY_GBGL_ZZGB123",12);      

		JButton loginButton = new JButton("连接测试");
		loginButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {     
	            //测试数据库连接
	            DataSource.dsprop.setProperty("user", userText.getText());
	            DataSource.dsprop.setProperty("password", passwordText.getText());
	            DataSource.dsprop.setProperty("url", urlText.getText());
	            try {
					DataSource.testDMConn();
					JOptionPane.showMessageDialog(mainFrame, "连接成功！");    
				} catch (myException e1) {
					JOptionPane.showMessageDialog(mainFrame, "连接失败："+e1.getMessage());    
				}
	            
	         }
	      }); 
		
		JPanel controlPanel = new JPanel();
	    controlPanel.setLayout(new FlowLayout());//FlowLayout是默认布局，它以方向流布局组件。
	    controlPanel.add(dslabel);
	    controlPanel.add(dsText);
	    controlPanel.add(urllabel);
	    controlPanel.add(urlText);
	    controlPanel.add(namelabel);
	    controlPanel.add(userText);
	    controlPanel.add(passwordLabel);       
	    controlPanel.add(passwordText);
	    controlPanel.add(loginButton);
	    //位置及大小
	    controlPanel.setBounds(0, 5, 1700, 35);
	    controlPanel.setBorder(new LineBorder(Color.red));
	    
	    Container contentPane = mainFrame.getContentPane();
	    contentPane.add(controlPanel,BorderLayout.PAGE_START);
	}
	
}
