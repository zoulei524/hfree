package me.zoulei.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.AbstractListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import me.zoulei.backend.entity.TableMetaDataConfig;

/**
 * 2023年9月12日19:24:42  zoulei
 * 用户表格配置后读取参数
 */
public class EditorGrid extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public JTableHeader header;
    public JTable table;
    public JScrollPane scrollPane;
    public JPopupMenu renamePopup;
    public JTextArea text;
    public TableColumn column;
    private boolean headerEditable = false;
    private boolean tableEditable = false;

    public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int MIN_ROW_HEIGHT = (int)SCREEN_SIZE.getHeight()/36;
    public static final int MIN_ROW_WIDTH = (int)SCREEN_SIZE.getWidth()/108;
    TableModel model;
    
    List<HashMap<String, String>> tableMetaData;

    public EditorGrid(String tablename,String owner) {
    	try {
    		//* 传入表名tablename  获取数据库表的各种信息 字段名column_name 字段备注comments 字段类型data_type 字段长度data_length 主键p
    		tableMetaData = new TableMetaDataConfig(tablename,owner,"").getTableMetaData();
		} catch (Exception e) {
			e.printStackTrace();
		}
        init();
    }

    public void init() {
    	Object[][] tableDate = new Object[5][tableMetaData.size()];
    	String[] colnames = new String[tableMetaData.size()];
    	for(int i = 0; i<tableMetaData.size(); i++ ) {
    		HashMap<String, String> metaData = tableMetaData.get(i);
    		String comments = metaData.get("comments");
    		String column_name = metaData.get("column_name");
    		String data_type = metaData.get("data_type");
    		String data_length = metaData.get("data_length");
    		String p = metaData.get("p");
    		//这样可以自动换行
    		colnames[i] = this.formateColName(comments);
    		tableDate[0][i] = column_name;
    		tableDate[1][i] = data_type+":"+data_length+":"+p;
    		//设置默认值
    		tableDate[3][i] = "显示";
    		tableDate[4][i] = "center";
    	}
        table = new JTable(tableDate,colnames) {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;


            @Override
            public boolean isCellEditable(int row, int column) {
            	//第一行是字段名，第二行宽，通过拖拉改变，不可编辑。
            	if(row==0||row==1||row==2)return false;
                if (tableEditable) return true;
                return false;
            }
        };
        //table.putClientProperty("terminateEditOnFocusLost", true);
        this.model = table.getModel();
        
        //单元格居中
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table.setDefaultRenderer(Object.class, centerRenderer);//第一个参数填Object,因为表格数据使用的是二维Object数组
        
        //改变列宽是，其他列的列宽不自动变化
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        
        //默认选中第一个单元格
        table.changeSelection(0, 0, false, false);
        
        header = table.getTableHeader();
        //设置高度2行 设置了这个表头宽度就固定了，列宽变了后 总宽不会自动调整， 在列模型上加事件解决
        header.setPreferredSize(new Dimension(table.getColumnModel().getTotalColumnWidth(), 80));
        
        
        //添加双击更改表头名称事件
        header.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2 && headerEditable) {
                	
                    editColumnAt(event.getPoint());
                }
            }
            
        });
        
        
        text = new JTextArea();
        text.setBorder(null);
        text.setLineWrap(true);
        /*
        text.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (headerEditable) renameColumn();
            }
        });
        */
        //不允许选中行
        table.setRowSelectionAllowed(false);
        //可选中单元格
        table.setCellSelectionEnabled(true);

        //弹出菜单，里面放了一个文本框，用户改表头的名称，改完后更新表头
        renamePopup = new JPopupMenu();
        renamePopup.addPopupMenuListener(new  PopupMenuListener() {
			
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				
			}
			
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				
			}
			
			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
				if (headerEditable) renameColumn();
			}
		});
        renamePopup.setBorder(new MatteBorder(0, 1, 1, 1, Color.DARK_GRAY));
        renamePopup.add(text);

        
        
        JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		JLabel dslabel= new JLabel("表格的属性设置: ", JLabel.LEFT);
		dslabel.setFont(new Font("宋体", Font.PLAIN, 18));
		toolBar.add(dslabel);
		
		
        scrollPane = new JScrollPane( table );
        //scrollPane.setBorder(new EmptyBorder(0, 14, 0, 14));
        //设置表格行表头
        scrollPane.setRowHeaderView(buildRowHeader(table));
        
        table.setRowHeight(MIN_ROW_HEIGHT);
        TableColumnModel cm = table.getColumnModel();
        cm.addColumnModelListener(new TableColumnModelListener() {
			
			@Override
			public void columnSelectionChanged(ListSelectionEvent e) {
			}
			
			@Override
			public void columnRemoved(TableColumnModelEvent e) {
			}
			
			@Override
			public void columnMoved(TableColumnModelEvent e) {
			}
			
			@Override
			public void columnMarginChanged(ChangeEvent e) {
				header.setPreferredSize(new Dimension(table.getColumnModel().getTotalColumnWidth(), 80));
				//改变宽的值
				for(int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
					model.setValueAt(cm.getColumn(i).getWidth(), 2, i);
				}
		            
				
			}
			
			@Override
			public void columnAdded(TableColumnModelEvent e) {
			}
		});
        
        for(int i = 0; i < cm.getColumnCount(); i++) {
        	//setWidth没有用   PreferredWidth首选宽度
            cm.getColumn(i).setPreferredWidth(120);
            //设置编辑器
            JBoxTestCell jc = new JBoxTestCell();// 第四行第五行为下拉框，其余行为文本框
            cm.getColumn(i).setCellEditor(jc);
        }
        	
        table.setColumnModel(cm);
        setLayout(new BorderLayout());
        
        add(toolBar, BorderLayout.NORTH);
        add(scrollPane);
    }

    
    private String formateColName(String text) {
    	return "<html><div style='height:55px;'>" + text + "</div></html>";
    }
    
    private String getColNameText(String text) {
    	return text.replace("<html><div style='height:55px;'>", "").replace("</div></html>", "");
    }
    
    public void setHeaderEditable(boolean b) {
        headerEditable = b;
    }

    public boolean isHeaderEditable() {
        return headerEditable;
    }

    public void setTableEditable(boolean b) {
        tableEditable = b;
    }

    public boolean isTableEditable() {
        return tableEditable;
    }

    //添加双击更改表头名称
    private void editColumnAt(Point p) {
        int columnIndex = header.columnAtPoint(p);
        if (columnIndex != -1) { 
            column = header.getColumnModel().getColumn(columnIndex);
            Rectangle columnRectangle = header.getHeaderRect(columnIndex);
            //把html换掉  "<html><div style='height:72px;> " + comments + "</div></html>"
            text.setText(this.getColNameText(column.getHeaderValue().toString()));
            renamePopup.setPreferredSize(new Dimension(columnRectangle.width, columnRectangle.height - 1));
            renamePopup.show(header, columnRectangle.x, 0);

            text.requestFocusInWindow();
            text.selectAll();
        }
    }
    //表头名称改完后替换
    private void renameColumn() {
    	//把html加回去
        column.setHeaderValue(this.formateColName(text.getText()));
        renamePopup.setVisible(false);
        header.repaint();
    }

    /**
     * 生成行的表头标题  与table是独立的
     * @param table
     * @return
     */
    private static JList<Object> buildRowHeader(JTable table) {
        final Vector<String> headers = new Vector<String>();
        headers.add("字段名");
        headers.add("数据类型:长度:主键");
        headers.add("列宽");
        headers.add("是否显示");
        headers.add("水平对齐");

        ListModel<Object> lm = new AbstractListModel<Object>() {

            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            public int getSize() {
                return headers.size();
            }

            public Object getElementAt(int index) {
                return headers.get(index);
            }
        };

        //行表头
        final JList<Object> rowHeader = new JList<>(lm);
        rowHeader.setOpaque(true);
        //表头固定宽
        rowHeader.setFixedCellWidth(120);

        //行事件 可以拖动改变行高
        MouseInputAdapter mouseAdapter = new MouseInputAdapter() {
            Cursor oldCursor;
            //鼠标样式 用户调整行高
            Cursor RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
            int index = -1;
            int oldY = -1;

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                int previ = getLocationToIndex(new Point(e.getX(), e.getY() - 3));
                int nexti = getLocationToIndex(new Point(e.getX(), e.getY() + 3));
                if (previ != -1 && previ != nexti) {
                    if (!isResizeCursor()) {
                        oldCursor = rowHeader.getCursor();
                        rowHeader.setCursor(RESIZE_CURSOR);
                        index = previ;
                    }
                } else if (isResizeCursor()) {
                    rowHeader.setCursor(oldCursor);
                }
            }

            private int getLocationToIndex(Point point) {
                int i = rowHeader.locationToIndex(point);
                if (!rowHeader.getCellBounds(i, i).contains(point)) {
                    i = -1;
                }
                return i;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (isResizeCursor()) {
                    rowHeader.setCursor(oldCursor);
                    index = -1;
                    oldY = -1;
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (isResizeCursor() && index != -1) {
                    int y = e.getY();
                    if (oldY != -1) {
                        int inc = y - oldY;
                        int oldRowHeight = table.getRowHeight(index);
                        int oldNextRowHeight = table.getRowHeight(index+1);
                        if (oldRowHeight > MIN_ROW_HEIGHT || inc > 0) {
                            int rowHeight = Math.max(MIN_ROW_HEIGHT, oldRowHeight + inc);
                            table.setRowHeight(index, rowHeight);
                            if (rowHeader.getModel().getSize() > index + 1) {
                                int rowHeight1 = table.getRowHeight(index + 1) - inc;
                                rowHeight1 = Math.max(MIN_ROW_HEIGHT, rowHeight1);
                                table.setRowHeight(index + 1, rowHeight1);
                            }
                        }
                        if (table.getRowCount()>index+1)
                            table.setRowHeight(1+index, oldNextRowHeight);
                        else System.out.println("HI");
                    }
                    oldY = y;
                }
            }

            private boolean isResizeCursor() {
                return rowHeader.getCursor() == RESIZE_CURSOR;
            }
        };
        rowHeader.addMouseListener(mouseAdapter);
        rowHeader.addMouseMotionListener(mouseAdapter);
        rowHeader.addMouseWheelListener(mouseAdapter);

        rowHeader.setCellRenderer(new RowHeaderRenderer(table));
        rowHeader.setBackground(table.getBackground());
        rowHeader.setForeground(table.getForeground());
        return rowHeader;
    }

    static class RowHeaderRenderer extends JLabel implements ListCellRenderer<Object> {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private JTable table;

        RowHeaderRenderer(JTable table) {
            this.table = table;
            JTableHeader header = this.table.getTableHeader();
            setOpaque(true);
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            setHorizontalAlignment(CENTER);
            setForeground(header.getForeground());
            setBackground(header.getBackground());
            setFont(header.getFont());
            setDoubleBuffered(true);
        }

        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            setText((value == null) ? "" : value.toString());
            setPreferredSize(null);
            setPreferredSize(new Dimension((int) getPreferredSize().getWidth(), table.getRowHeight(index)));
            list.firePropertyChange("cellRenderer", 0, 1);
            return this;
        }
    }
/*
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tabley");

        TableyTable table = new TableyTable(100, 1878);
        table.setHeaderEditable(true);
        table.setTableEditable(true);

        frame.add(table);
        frame.setSize(new Dimension(1700, 500));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

*/

    
    /**
     * 获取表格配置
     */
    public List<HashMap<String, String>> genTableMetaData(){
    	List<HashMap<String, String>> tmd = new ArrayList<HashMap<String,String>>();
    	//* 传入表名tablename  获取数据库表的各种信息 字段名column_name 字段备注comments 字段类型data_type 字段长度data_length 主键p
    	HashMap<String, String> field = null;
    	//int rowCount = this.model.getRowCount();
    	int colCount = this.model.getColumnCount();
    	for(int c=0; c<colCount; c++) {
    		//字段配置
    		field = new HashMap<String, String>();
    		tmd.add(field);
    		//字段备注
    		String comments = this.getColNameText(this.header.getColumnModel().getColumn(c).getHeaderValue().toString());
    		field.put("comments", comments);
    		//原备注
    		field.put("comments2", this.getColNameText(this.model.getColumnName(c)));
    		
    		//字段名
    		String column_name = this.model.getValueAt(0, c).toString();
    		field.put("column_name", column_name);
    		//其他信息
    		String orther = this.model.getValueAt(1, c).toString();
    		String[] orthers = orther.split(":");
    		field.put("data_type", orthers[0]);
    		field.put("data_length", orthers[1]);
    		field.put("p", orthers[2]);
    		//列宽
    		String width = this.model.getValueAt(2, c).toString();
    		field.put("width", width);
    		//是否显示
    		String visible = this.model.getValueAt(3, c).toString();
    		field.put("visible", visible);
    		//水平对齐
    		String align = this.model.getValueAt(4, c).toString();
    		field.put("align", align);
    	}
    	return tmd;
    }
    
    
    
}











/**
 * 自定义celleditor实现
 * 指定单元格设置下拉框，其他单元格设置文本框
 *
 */
class JBoxTestCell extends AbstractCellEditor implements TableCellEditor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int row;
	int column;
	private JComboBox<String> jbox2;//第四行 
	private JComboBox<String> jbox3;//第五行 
	private JTextField textfield;

	public JBoxTestCell() {
		//是否显示列配置
		jbox2 = new JComboBox<String>(new String[] {"显示","不显示"});
		//水平位置配置
		jbox3 = new JComboBox<String>(new String[] {"left","center","right"});
		jbox3.setSelectedIndex(1);
	}

	@Override
	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}

	

	public Object getCellEditorValue() {
		switch (this.row) {
		
			case 3:
				String v2 = jbox2.getSelectedItem().toString();
				return v2;
			case 4:
				String v3 = jbox3.getSelectedItem().toString();
				return v3;
			default:
				return this.textfield.getText().toString();
		}
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.row = row;
		this.column = column;
		
		switch (this.row) {
		
			case 3:
				return this.jbox2;
			case 4:
				return this.jbox3;
			default:
				JTextField result = new JTextField();
				result.setText(value==null?"":value.toString());   
				this.textfield = result;
				return result;
		}
	}
}