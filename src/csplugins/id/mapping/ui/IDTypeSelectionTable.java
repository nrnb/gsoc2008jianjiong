/* File: IDTypeSelectionTable.java

 Copyright (c) 2006, 2007, The Cytoscape Consortium (www.cytoscape.org)

 The Cytoscape Consortium is:
 - Institute for Systems Biology
 - University of California San Diego
 - Memorial Sloan-Kettering Cancer Center
 - Institut Pasteur
 - Agilent Technologies

 This library is free software; you can redistribute it and/or modify it
 under the terms of the GNU Lesser General Public License as published
 by the Free Software Foundation; either version 2.1 of the License, or
 any later version.

 This library is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF
 MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  The software and
 documentation provided hereunder is on an "as is" basis, and the
 Institute for Systems Biology and the Whitehead Institute
 have no obligations to provide maintenance, support,
 updates, enhancements or modifications.  In no event shall the
 Institute for Systems Biology and the Whitehead Institute
 be liable to any party for direct, indirect, special,
 incidental or consequential damages, including lost profits, arising
 out of the use of this software and its documentation, even if the
 Institute for Systems Biology and the Whitehead Institute
 have been advised of the possibility of such damage.  See
 the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License
 along with this library; if not, write to the Free Software Foundation,
 Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 */

package csplugins.id.mapping.ui;

import cytoscape.Cytoscape;

import java.util.List;
import java.util.Vector;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.table.TableCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;

/**
 * Table for selecting which attribute to use for matching nodes
 * 
 * 
 */
public class IDTypeSelectionTable extends JTable{
    private IDTypeSelectionTableModel model;
    private Frame frame;
    private AttributeBasedIDMappingFilePanel parent;

    private Set<String> supportedSrcIDType;

    private List<String[]> listNetIDTitleAttr;

    public IDTypeSelectionTable(final Frame frame,
                                final AttributeBasedIDMappingFilePanel parent) {
        super();

        this.frame = frame;
        this.parent = parent;

        initNetworks();

        supportedSrcIDType = new TreeSet<String>();

        model = new IDTypeSelectionTableModel();
        setModel(model);
        setRowHeight(20);

        //addMouseClickListener();

    }

    public void setSupportedSrcIDType(final Set<String> types) {
            supportedSrcIDType = new TreeSet<String>(types);
            setColumnEditorAndCellRenderer();
    }

    protected void setColumnEditorAndCellRenderer() {
        int nc = getColumnCount();
        TableColumn column = getColumnModel().getColumn(nc-1);

        // set up editor
        RowTableCellEditor rowEditor = new RowTableCellEditor(this);
        int nr = this.getRowCount();
        List<CheckComboBox> combos = new Vector<CheckComboBox>(nr);
        for (int ir=0; ir<nr; ir++) {
                String net = (String) this.getValueAt(ir, 0);
                String attr = (String) this.getValueAt(ir, 1);
                CheckComboBox cc = new  CheckComboBox(net,attr);
                combos.add(cc);
                rowEditor.setEditorAt(ir, new  DefaultCellEditor(cc));
        }
        column.setCellEditor(rowEditor);
        //column.setCellEditor(new ComboBoxTableCellEditor(this));

        // set up renderer
        column.setCellRenderer(new ComboBoxTableCellRenderer(combos));
    }

        private void initNetworks() {
            Map<String,Map<String,Set<String>>> selectedNetworkAttributeIDType = parent.getSrcTypes();

            List<String> netTitles = new Vector<String>();
            List<String> netIDs = new Vector<String>();
            int size=0;
            Iterator<String> it = selectedNetworkAttributeIDType.keySet().iterator();
            while (it.hasNext()) {
                String netID = it.next();
                String netName = Cytoscape.getNetwork(netID).getTitle();
                int index = 0;
                while (index<size && netTitles.get(index).compareToIgnoreCase(netName)<0) index++;

                netIDs.add(index,netID);
                netTitles.add(index,netName);
                size++;
            }

            listNetIDTitleAttr = new Vector<String[]>();

            int n = netIDs.size();
            for (int i=0; i<n; i++) {
                    String id = netIDs.get(i);
                    Iterator<String> itAttr = selectedNetworkAttributeIDType.get(id).keySet().iterator();
                    while (itAttr.hasNext()) {
                            String attr = itAttr.next();
                            listNetIDTitleAttr.add(new String[]{id,netTitles.get(i),attr});
                    }
            }
       }

    void fireTableDataChanged() {
            model.fireTableDataChanged();
    }

    private class IDTypeSelectionTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Network","Attribute","ID Type(s)"};

        @Override
        public int getColumnCount() {
            return getRowCount()==0?0:3; // network; attribute; id types
        }

        @Override
        public int getRowCount() {
            return listNetIDTitleAttr.size();
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public String getValueAt(int row, int col) {
            String[] strs = listNetIDTitleAttr.get(row);
            String netID = strs[0];
            String netTitle = strs[1];
            String attr = strs[2];
            switch (col) {
                case 0:
                    return netTitle;
                case 1:
                    return attr;
                case 2:
                    return parent.getSrcTypes().get(netID).get(attr).toString();
                default:
                    throw new java.lang.IndexOutOfBoundsException();
            }
        }

        @Override
        public Class getColumnClass(int c) {
            return String.class;
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return col==this.getColumnCount()-1;
        }

    }


    // render checkcombobox
    class ComboBoxTableCellRenderer implements TableCellRenderer {
                private List<CheckComboBox> combos;
                public ComboBoxTableCellRenderer(List<CheckComboBox> combos) {
                        this.combos = combos;
                }

                @Override
                public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                        if (column==table.getColumnCount()-1) {
                                return combos.get(row);
                        } else {
                                return new DefaultTableCellRenderer();
                        }
                }
        }

        // combobox contains checkboxes
        class CheckComboBox extends JComboBox {
               private List<JCheckBox> cbs;
               private String net;
               private String attr;

               public CheckComboBox(String net, String attr) {
                       //super(new JCheckBox[supportedSrcIDType.size()]);
                       this.net = net;
                       this.attr = attr;
                       initCBs();

                       this.addItem(new String());
                       for (JCheckBox cb : cbs) {
                               this.addItem(cb);
                       }

                       setRenderer(new CheckBoxRenderer(net,attr,cbs));
                       addActionListener(this);
               }

               private void initCBs() {
                        Set<String> selectedTypes = parent.getSrcTypes().get(net).get(attr);
                        cbs = new Vector<JCheckBox>();
                        JCheckBox cb;
                        for (String type : supportedSrcIDType) {
                                cb = new JCheckBox(type);
                                cb.setSelected(selectedTypes.contains(type));
                                cbs.add(cb);
                        }

                        cb = new JCheckBox("Select all");
                        cb.setSelected(selectedTypes.size()==supportedSrcIDType.size());
                        cbs.add(cb);

                        cb = new JCheckBox("Select none");
                        cb.setSelected(selectedTypes.isEmpty());
                        cbs.add(cb);
                }

                private void checkBoxSelectionChanged(int index) {
                        int n = cbs.size();
                        if (index<0 || index>=n) return;

                        Set<String> selectedTypes = parent.getSrcTypes().get(net).get(attr);
                        if (index<n-2) {
                                JCheckBox cb = cbs.get(index);
                                if (cb.isSelected()) {
                                        cb.setSelected(false);
                                        selectedTypes.remove(cb.getText());
                                        
                                        cbs.get(n-2).setSelected(false); //Select all
                                        cbs.get(n-1).setSelected(selectedTypes.isEmpty());
                                } else {
                                        cb.setSelected(true);
                                        selectedTypes.add(cb.getText());

                                        cbs.get(n-2).setSelected(selectedTypes.size()==supportedSrcIDType.size()); // Select all
                                        cbs.get(n-1).setSelected(false);
                                }
                        } else if (index==n-2) {
                                selectedTypes.addAll(supportedSrcIDType);
                                for (int i=0; i<n-1; i++) {
                                        cbs.get(i).setSelected(true);
                                }
                                cbs.get(n-1).setSelected(false);
                        } else { // if (index==n-1)
                                selectedTypes.clear();
                                for (int i=0; i<n-1; i++) {
                                        cbs.get(i).setSelected(false);
                                }
                                cbs.get(n-1).setSelected(true);
                        }

                        parent.updateGoButtonEnable();
                }

                @Override
                public void actionPerformed(ActionEvent e) {
                        int sel = getSelectedIndex();

                        if (sel == 0) {
                                getUI().setPopupVisible(this, false);
                        } else if (sel > 0) {
                                checkBoxSelectionChanged(sel-1);
                        }

                        this.setSelectedIndex(-1); // clear selection
                }

                @Override
                public void setPopupVisible(boolean flag)
                {
                        int i=1;
                        //TODO this not work, fix it
                        // Not code here prevents the populist from closing
                }
        }

        // checkbox renderer for combobox
        class CheckBoxRenderer implements ListCellRenderer {
                private DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
                private List<JCheckBox> cbs;
                private String net;
                private String attr;

                public CheckBoxRenderer(String net, String attr, List<JCheckBox> cbs) {
                        setOpaque(true);
                        this.cbs = cbs;
                        this.net = net;
                        this.attr = attr;
                }

                @Override
                public Component getListCellRendererComponent(
                        JList list,
                        Object value,
                        int index,
                        boolean isSelected,
                        boolean cellHasFocus) {
                                if (index > 0) {
                                        JCheckBox cb = cbs.get(index-1);
                                        cb.setBackground(isSelected ? Color.blue : Color.white);
                                        cb.setForeground(isSelected ? Color.white : Color.black);

                                        return cb;
                                }

                                Set<String> types = new TreeSet<String>(parent.getSrcTypes().get(net).get(attr));

                                return defaultRenderer.getListCellRendererComponent(list, types.toString(), index, isSelected, cellHasFocus);


                }
        } 

}

// support different editors for each row in a column
class RowTableCellEditor implements TableCellEditor {
  protected HashMap editors;

  protected TableCellEditor editor, defaultEditor;

  JTable table;

  /**
   * Constructs a EachRowEditor. create default editor
   *
   * @see TableCellEditor
   * @see DefaultCellEditor
   */
  public RowTableCellEditor(JTable table) {
    this.table = table;
    editors = new HashMap();
    defaultEditor = new DefaultCellEditor(new JTextField());
  }

  /**
   * @param row
   *            table row
   * @param editor
   *            table cell editor
   */
  public void setEditorAt(int row, TableCellEditor editor) {
    editors.put(new Integer(row), editor);
  }

  public Component getTableCellEditorComponent(JTable table, Object value,
      boolean isSelected, int row, int column) {
    return editor.getTableCellEditorComponent(table, value, isSelected,
        row, column);
  }

  public Object getCellEditorValue() {
    return editor.getCellEditorValue();
  }

  public boolean stopCellEditing() {
    return editor.stopCellEditing();
  }

  public void cancelCellEditing() {
    editor.cancelCellEditing();
  }

  public boolean isCellEditable(EventObject anEvent) {
    selectEditor((MouseEvent) anEvent);
    return editor.isCellEditable(anEvent);
  }

  public void addCellEditorListener(CellEditorListener l) {
    editor.addCellEditorListener(l);
  }

  public void removeCellEditorListener(CellEditorListener l) {
    editor.removeCellEditorListener(l);
  }

  public boolean shouldSelectCell(EventObject anEvent) {
    selectEditor((MouseEvent) anEvent);
    return editor.shouldSelectCell(anEvent);
  }

  protected void selectEditor(MouseEvent e) {
    int row;
    if (e == null) {
      row = table.getSelectionModel().getAnchorSelectionIndex();
    } else {
      row = table.rowAtPoint(e.getPoint());
    }
    editor = (TableCellEditor) editors.get(new Integer(row));
    if (editor == null) {
      editor = defaultEditor;
    }
  }
}
