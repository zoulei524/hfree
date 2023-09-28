package me.zoulei.ui.components;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import dm.jdbc.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.zoulei.MainApp;
import me.zoulei.backend.entity.TableMetaDataConfig;
import me.zoulei.backend.jdbc.utils.CommQuery;
import me.zoulei.gencode.Gencode;
import me.zoulei.ui.frame.AutoCompletion;

/**
 * 2023年9月14日11:30:15  zoulei
 * 用于搜索数据库表的组件， 设置表后可以加载表格参数配置组件。  查询模式和表名
 */
public class SearchComponent {

	Item[] items;
	String[] items2 = new String[] {"sadsa","dsf","fsdfg","dsadsfx"};
	
	List<Component> removeComponents = new ArrayList<Component>();
	GridComponent grid;
	public void removeAll() {
		//第一次进来没渲染，不需要移除。 
		if(removeComponents.size()>0) {
			removeComponents.forEach(c->{
				MainApp.mainFrame.remove(c);
			});
			MainApp.mainFrame.remove(this.grid.getEditorGrid());
			MainApp.mainFrame.getContentPane().repaint();
		}
		
	}
	
	public void setComp() {
		//模式列表
		this.searchOwner();
		
		//表格组件
		
		this.grid = new GridComponent();
		
		JLabel  dslabel= new JLabel("选择模式: ", JLabel.LEFT);
		MainApp.mainFrame.add(dslabel);
		removeComponents.add(dslabel);
		dslabel.setBounds(10, 75, 60, 45);
		
		dslabel= new JLabel("表格的属性设置: ", JLabel.LEFT);
		dslabel.setFont(new Font("宋体", Font.PLAIN, 20));
		MainApp.mainFrame.add(dslabel);
		removeComponents.add(dslabel);
		dslabel.setBounds(10, 125, 200, 45);
		
		//模式名下拉
		JComboBox<String> cbx2 = new JComboBox<String>(items2);
		
		JScrollPane msmscr = new JScrollPane(cbx2);
		MainApp.mainFrame.add(msmscr);
		removeComponents.add(msmscr);
		msmscr.setBounds(75, 75, 150, 45);
		msmscr.setBorder(MainApp.lineBorder);
		//cbx2.setEditable(true);
		cbx2.setSelectedItem("HY_GBGL_ZZGB");
		
		//表名下拉
		search((String) cbx2.getSelectedItem());
		
		
		dslabel= new JLabel("输入表名: ", JLabel.CENTER);
		MainApp.mainFrame.add(dslabel);
		removeComponents.add(dslabel);
		dslabel.setBounds(235, 75, 60, 45);
		
		JComboBox<Item> cbx = new JComboBox<Item>(items);
		//设置下拉最多显示的选项
		cbx.setMaximumRowCount(30);
		//这个监听是不让弹出下拉选。
		/*
		cbx.addPopupMenuListener(new PopupMenuListener() {
			
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				new Thread() {
					@Override
					public void run() {
						for(;;) {
							if(cbx.isPopupVisible()) {
								cbx.setPopupVisible(false);
								break;
							}else {
								try {
									Thread.sleep(3);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								continue;
							}
						}
						super.run();
					}
				}.start();
				
			}
			
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				cbx.setPopupVisible(false);
			}
			
			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
				cbx.setPopupVisible(false);
			}
		});
		*/
		//生成数据库配置及代码按钮
		JButton genCodeBtn = new JButton("生成代码");
		//选择模式后事件  查询表名，将选择表名的下拉框选项重新设置
		cbx2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				search((String) cbx2.getSelectedItem());
				cbx.removeAllItems();
				for (Item item : items) {
					cbx.addItem(item);
	            }
				
				//直接显示下拉选框
				//cbx.setPopupVisible(true);
			}
		});
		JScrollPane controlPanel = new JScrollPane(cbx);
		MainApp.mainFrame.add(controlPanel);
		removeComponents.add(controlPanel);
		//下拉框
		controlPanel.setBounds(295, 75, 1200, 45);
		controlPanel.setBorder(MainApp.lineBorder);
		//实现搜索
		AutoCompletion.enable(cbx);
		//cbx.setEnabled(false);
        //cbx.setPopupVisible(true);
		
		
		
		
		//选择表名事件 选择后加载表格
		cbx.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Item item = (Item) cbx.getSelectedItem();
				if(item!=null) {
					grid.setComp(item.getKey(),(String) cbx2.getSelectedItem());
				}
			}
		});
		//第一次默认加载表
		Item item = (Item) cbx.getSelectedItem();
		if(item!=null) {
			grid.setComp(item.getKey(),(String) cbx2.getSelectedItem());
			genCodeBtn.setBounds(1510, 75, 100, 45);
		}
		
		
		MainApp.mainFrame.add(genCodeBtn);
		removeComponents.add(genCodeBtn);
		genCodeBtn.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {     
	             //生成配置
	        	 List<HashMap<String, String>> tmd = grid.editorGrid.genTableMetaData();
	        	 Item item = (Item) cbx.getSelectedItem();
	        	 try {
	        		 //生成代码
					new Gencode().gencode(new TableMetaDataConfig(item.getKey(),item.getValue(), tmd));
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(MainApp.mainFrame, e1.getMessage());    
					e1.printStackTrace();
				}
	            
	         }
	      });
		
		genCodeBtn.setBorder(MainApp.lineBorder);
		
	}
	//查询表
	public void search(String sch) {
		if(StringUtil.isEmpty(sch)) {
			items = new Item[] {};
			//return;
		}
		String sql = "select t.table_name,c.COMMENTS from All_TABLES t,SYS.ALL_TAB_COMMENTS c where t.TABLE_NAME=c.TABLE_NAME and t.owner = '"+sch.toUpperCase()+"' order by table_name ";
		CommQuery cq = new CommQuery();
		try {
			List<HashMap<String, String>> list = cq.getListBySQL2(sql);
			items = new Item[list.size()];
			for(int i=0;i<list.size();i++) {
				items[i] = new Item(list.get(i).get("table_name"),list.get(i).get("comments"));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(MainApp.mainFrame, e.getMessage());    
			e.printStackTrace();
		}
	}
	//查询模式
	public void searchOwner() {
		
		String sql = "select distinct owner from ALL_TABLES ";
		CommQuery cq = new CommQuery();
		try {
			List<HashMap<String, String>> list = cq.getListBySQL2(sql);
			items2 = new String[list.size()];
			for(int i=0;i<list.size();i++) {
				items2[i] = list.get(i).get("owner");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 表名选择下拉框
	 */
	@Data
	@AllArgsConstructor
	public class Item {
		private String key;
		private String value;
		public String toString(){
			return key + "("+(value==null?"":value)+")";
		}
	}

	
}
