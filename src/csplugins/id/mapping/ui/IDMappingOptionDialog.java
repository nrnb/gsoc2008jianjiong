/* File: IDMappingOptionDialog.java

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

/**
 *
 * @author gjj
 */
public class IDMappingOptionDialog extends javax.swing.JDialog {

    /** Creates new form IDMappingOptionDialog */
    public IDMappingOptionDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
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

                javax.swing.JTabbedPane tabbedPane = new javax.swing.JTabbedPane();
                fileBasedPanel = new javax.swing.JPanel();
                delemiterTypePanel = new javax.swing.JPanel();
                tabTypeCheckBox = new javax.swing.JCheckBox();
                commaTypeCheckBox = new javax.swing.JCheckBox();
                semiTypeCheckBox = new javax.swing.JCheckBox();
                spaceTypeCheckBox = new javax.swing.JCheckBox();
                otherTypeCheckBox = new javax.swing.JCheckBox();
                otherTypeTextField = new javax.swing.JTextField();
                delemiterIDPanel = new javax.swing.JPanel();
                tabIDCheckBox = new javax.swing.JCheckBox();
                commaIDCheckBox = new javax.swing.JCheckBox();
                semiIDCheckBox = new javax.swing.JCheckBox();
                spaceIDCheckBox = new javax.swing.JCheckBox();
                otherIDCheckBox = new javax.swing.JCheckBox();
                otherIDTextField = new javax.swing.JTextField();
                btnPanel = new javax.swing.JPanel();
                applyBtn = new javax.swing.JButton();
                okBtn = new javax.swing.JButton();
                cancelBtn = new javax.swing.JButton();

                setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                setTitle("ID Mapping Options");
                getContentPane().setLayout(new java.awt.GridBagLayout());

                fileBasedPanel.setLayout(new java.awt.GridBagLayout());

                delemiterTypePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Select delimiter between IDs of different types"));
                delemiterTypePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

                tabTypeCheckBox.setSelected(true);
                tabTypeCheckBox.setText("Tab");
                tabTypeCheckBox.setEnabled(false);
                delemiterTypePanel.add(tabTypeCheckBox);

                commaTypeCheckBox.setText("Comma");
                commaTypeCheckBox.setEnabled(false);
                delemiterTypePanel.add(commaTypeCheckBox);

                semiTypeCheckBox.setText("Semicolon");
                semiTypeCheckBox.setEnabled(false);
                delemiterTypePanel.add(semiTypeCheckBox);

                spaceTypeCheckBox.setText("Space");
                spaceTypeCheckBox.setEnabled(false);
                delemiterTypePanel.add(spaceTypeCheckBox);

                otherTypeCheckBox.setText("Other");
                otherTypeCheckBox.setEnabled(false);
                delemiterTypePanel.add(otherTypeCheckBox);

                otherTypeTextField.setEnabled(false);
                otherTypeTextField.setMinimumSize(new java.awt.Dimension(60, 20));
                otherTypeTextField.setPreferredSize(new java.awt.Dimension(60, 20));
                delemiterTypePanel.add(otherTypeTextField);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 1;
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                fileBasedPanel.add(delemiterTypePanel, gridBagConstraints);

                delemiterIDPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Select delimiter between IDs of the same type"));
                delemiterIDPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

                tabIDCheckBox.setText("Tab");
                tabIDCheckBox.setEnabled(false);
                delemiterIDPanel.add(tabIDCheckBox);

                commaIDCheckBox.setSelected(true);
                commaIDCheckBox.setText("Comma");
                commaIDCheckBox.setEnabled(false);
                delemiterIDPanel.add(commaIDCheckBox);

                semiIDCheckBox.setSelected(true);
                semiIDCheckBox.setText("Semicolon");
                semiIDCheckBox.setEnabled(false);
                delemiterIDPanel.add(semiIDCheckBox);

                spaceIDCheckBox.setText("Space");
                spaceIDCheckBox.setEnabled(false);
                delemiterIDPanel.add(spaceIDCheckBox);

                otherIDCheckBox.setText("Other");
                otherIDCheckBox.setEnabled(false);
                delemiterIDPanel.add(otherIDCheckBox);

                otherIDTextField.setEnabled(false);
                otherIDTextField.setMinimumSize(new java.awt.Dimension(60, 20));
                otherIDTextField.setPreferredSize(new java.awt.Dimension(60, 20));
                delemiterIDPanel.add(otherIDTextField);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 2;
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                fileBasedPanel.add(delemiterIDPanel, gridBagConstraints);

                tabbedPane.addTab("File based", fileBasedPanel);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.weighty = 1.0;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                getContentPane().add(tabbedPane, gridBagConstraints);

                btnPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

                applyBtn.setText("Apply");
                applyBtn.setEnabled(false);
                btnPanel.add(applyBtn);

                okBtn.setText("  OK  ");
                okBtn.setEnabled(false);
                btnPanel.add(okBtn);

                cancelBtn.setText("Cancel");
                cancelBtn.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                cancelBtnActionPerformed(evt);
                        }
                });
                btnPanel.add(cancelBtn);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 1;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                getContentPane().add(btnPanel, gridBagConstraints);

                pack();
        }// </editor-fold>//GEN-END:initComponents

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
            this.setVisible(false);
    }//GEN-LAST:event_cancelBtnActionPerformed



        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton applyBtn;
        private javax.swing.JPanel btnPanel;
        private javax.swing.JButton cancelBtn;
        private javax.swing.JCheckBox commaIDCheckBox;
        private javax.swing.JCheckBox commaTypeCheckBox;
        private javax.swing.JPanel delemiterIDPanel;
        private javax.swing.JPanel delemiterTypePanel;
        private javax.swing.JPanel fileBasedPanel;
        private javax.swing.JButton okBtn;
        private javax.swing.JCheckBox otherIDCheckBox;
        private javax.swing.JTextField otherIDTextField;
        private javax.swing.JCheckBox otherTypeCheckBox;
        private javax.swing.JTextField otherTypeTextField;
        private javax.swing.JCheckBox semiIDCheckBox;
        private javax.swing.JCheckBox semiTypeCheckBox;
        private javax.swing.JCheckBox spaceIDCheckBox;
        private javax.swing.JCheckBox spaceTypeCheckBox;
        private javax.swing.JCheckBox tabIDCheckBox;
        private javax.swing.JCheckBox tabTypeCheckBox;
        // End of variables declaration//GEN-END:variables

}