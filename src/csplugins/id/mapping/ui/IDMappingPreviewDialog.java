/* File: IDMappingPreviewDialog.java

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

import csplugins.id.mapping.model.AttributeBasedIDMappingData;

import java.util.Vector;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * 
 */
public class IDMappingPreviewDialog extends javax.swing.JDialog {
    private final AttributeBasedIDMappingData idMapping;
    

    /** Creates new form IDMappingPreviewDialog */
    public IDMappingPreviewDialog(java.awt.Frame parent, boolean modal, AttributeBasedIDMappingData idMapping) {
        super(parent, modal);
        this.idMapping = idMapping;
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {
                java.awt.GridBagConstraints gridBagConstraints;

                javax.swing.JPanel prevewTablePanel = new javax.swing.JPanel();
                javax.swing.JScrollPane previewScrollPane = new javax.swing.JScrollPane();
                previewTable = new javax.swing.JTable();
                javax.swing.JPanel closePanel = new javax.swing.JPanel();
                closeButton = new javax.swing.JButton();
                javax.swing.JPanel opPanel = new javax.swing.JPanel();
                clearButton = new javax.swing.JButton();

                setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                setTitle("ID mapping preview");
                getContentPane().setLayout(new java.awt.GridBagLayout());

                prevewTablePanel.setLayout(new javax.swing.BoxLayout(prevewTablePanel, javax.swing.BoxLayout.LINE_AXIS));

                tableModel = new PreviewTableModel();
                previewTable.setModel(tableModel);
                previewScrollPane.setViewportView(previewTable);

                prevewTablePanel.add(previewScrollPane);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.weighty = 1.0;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                getContentPane().add(prevewTablePanel, gridBagConstraints);

                closeButton.setText("  Close  ");
                closeButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                closeButtonActionPerformed(evt);
                        }
                });
                closePanel.add(closeButton);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 1;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                getContentPane().add(closePanel, gridBagConstraints);

                clearButton.setText("Clear all");
                clearButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                clearButtonActionPerformed(evt);
                        }
                });
                opPanel.add(clearButton);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 1;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                getContentPane().add(opPanel, gridBagConstraints);

                pack();
        }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
            this.setVisible(false);
}//GEN-LAST:event_closeButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
            final int ioption = JOptionPane.showConfirmDialog(getParent(),
                                        "Are you sure to delete all the ID mapping data?",
                                        "Warning: ALL ID mapping would be removed",
                                        JOptionPane.YES_NO_OPTION );
            if (ioption==JOptionPane.YES_OPTION) {
                    idMapping.clear();
                    tableModel.resetData();
            }
    }//GEN-LAST:event_clearButtonActionPerformed


        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton clearButton;
        private javax.swing.JButton closeButton;
        private javax.swing.JTable previewTable;
        private PreviewTableModel tableModel;
        // End of variables declaration//GEN-END:variables

        private class PreviewTableModel extends AbstractTableModel {
                Vector<String> columnNames;
                Vector<String[]> data;

                public PreviewTableModel() {
                        resetData();
                }

                public void resetData() {
                    Map<String,Set<String>> mapGOAttrs = idMapping.getMapGOAttrs();

                    Set<String> idTypes = new TreeSet<String>(idMapping.getIDTypes());
                    columnNames = new Vector<String>();
                    columnNames.add("Node/Edge");
                    columnNames.add("Attribute");
                    columnNames.addAll(idTypes);
                    int nColumns = columnNames.size();

                    data = new Vector();

                    Iterator<Map.Entry<String,Set<String>>> itEntryGOAttrs = mapGOAttrs.entrySet().iterator();
                    while (itEntryGOAttrs.hasNext()) {
                            Map.Entry<String,Set<String>> entryGOAttrs = itEntryGOAttrs.next();
                            String node = entryGOAttrs.getKey();
                            Set<String> attrs = entryGOAttrs.getValue();

                            Iterator<String> itAttr = attrs.iterator();
                            while (itAttr.hasNext()) {
                                    String attr = itAttr.next();

                                    String[] strvec = new String[nColumns];
                                    data.add(strvec);

                                    strvec[0] = node;
                                    strvec[1] = attr;

                                    for (int i=2; i<nColumns; i++) {
                                            Set<String> tgtIDs = idMapping.getTgtIDs(node, attr, columnNames.get(i));
                                            if (tgtIDs==null||tgtIDs.isEmpty()) {
                                                    strvec[i] = null;
                                            } else {
                                                    strvec[i] = tgtIDs.toString();
                                            }
                                    }
                            }
                      }

                    this.fireTableDataChanged();
                }


                @Override
                public int getColumnCount() {
                    return columnNames.size();
                }

                @Override
                public int getRowCount() {
                    return data.size();
                }

                @Override
                public String getColumnName(int col) {
                    return columnNames.get(col);
                }

                @Override
                public String getValueAt(int row, int col) {
                    return data.get(row)[col];
                }

                @Override
                public Class getColumnClass(int c) {
                    return String.class;
                }

                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }

        }
}
