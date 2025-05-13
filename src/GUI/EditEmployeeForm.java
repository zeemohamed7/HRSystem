/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;
import Logic.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.Timer;
/**
 *
 * @author zainab
 */
public class EditEmployeeForm extends javax.swing.JFrame {
    
    private Employee selectedEmployee;
    private MainWindow main;
    private ArrayList<Department> departments;

    /**
     * Creates new form EditEmployeeForm
     */

    public EditEmployeeForm(MainWindow mainWindow, Employee employee) {
    this.main = mainWindow;  // Properly initialize main with the passed MainWindow
           this.selectedEmployee = employee;  // Set the selected employee to be edited
           this.departments = mainWindow.departments;  // Now access departments from the MainWindow
           
           
        initComponents();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
        populateDepartmentsComboBox();      // Populate the combo box
        populatePayLevelComboBox();         // Populate the pay level combo box



        
        
        firstNameEditField.setText(employee.getFirstName());
        lastNameEditField.setText(employee.getSurname());
        addressEditField.setText(employee.getAddress());
        
        // add gender
        if(employee.getGender() == 'M')
        {
            maleEditButton.setSelected(true);
        }
        
        else if(employee.getGender() == 'F')
        {
            femaleEditButton.setSelected(true);
        }
       
        // add department
        preSelectDepartment();   
        
        
        // add pay level 
        preSelectPayLevel();                
        
    }

    private void populateDepartmentsComboBox() {
        departmentEditCombo.removeAllItems();  

        departmentEditCombo.addItem("No Department");

        for (Department dept : departments) {
            departmentEditCombo.addItem(dept.getName());  
        }
    }

    private void preSelectDepartment() {
        // Get the department ID of the current employee
        Integer deptID = selectedEmployee.getDeptID();
        String departmentName = "No Department";  // Default if no department is assigned

        if (deptID != null) {
            // Look up the department name by its ID
            departmentName = Department.getDepartmentNameById(departments, deptID);
        }

        // Set the selected item in the combo box to the employee's current department
        departmentEditCombo.setSelectedItem(departmentName);
    }
    
    
    private void preSelectPayLevel() {
            int payLevel = selectedEmployee.getPayLevel();

            switch (payLevel) {
                case 1:
                    payLevelEditCombo.setSelectedItem("Level 1 - BHD 44,245.75");
                    break;
                case 2:
                    payLevelEditCombo.setSelectedItem("Level 2 - BHD 48,670.32");
                    break;
                case 3:
                    payLevelEditCombo.setSelectedItem("Level 3 - BHD 53,537.35");
                    break;
                case 4:
                    payLevelEditCombo.setSelectedItem("Level 4 - BHD 58,891.09");
                    break;
                case 5:
                    payLevelEditCombo.setSelectedItem("Level 5 - BHD 64,780.20");
                    break;
                case 6:
                    payLevelEditCombo.setSelectedItem("Level 6 - BHD 71,258.22");
                    break;
                case 7:
                    payLevelEditCombo.setSelectedItem("Level 7 - BHD 80,946.95");
                    break;
                case 8:
                    payLevelEditCombo.setSelectedItem("Level 8 - BHD 96,336.34");
                    break;
                default:
                    payLevelEditCombo.setSelectedItem("Select Annual Salary");
                    break;
            }
        }

    // Populate pay level combo box
    private void populatePayLevelComboBox() {
        payLevelEditCombo.removeAllItems();  // Clear all existing items

        payLevelEditCombo.addItem("Select Annual Salary");
        payLevelEditCombo.addItem("Level 1 - BHD 44,245.75");
        payLevelEditCombo.addItem("Level 2 - BHD 48,670.32");
        payLevelEditCombo.addItem("Level 3 - BHD 53,537.35");
        payLevelEditCombo.addItem("Level 4 - BHD 58,891.09");
        payLevelEditCombo.addItem("Level 5 - BHD 64,780.20");
        payLevelEditCombo.addItem("Level 6 - BHD 71,258.22");
        payLevelEditCombo.addItem("Level 7 - BHD 80,946.95");
        payLevelEditCombo.addItem("Level 8 - BHD 96,336.34");
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        editGenderGroup = new javax.swing.ButtonGroup();
        jLabel22 = new javax.swing.JLabel();
        departmentEditCombo = new javax.swing.JComboBox<>();
        cancelButton1 = new javax.swing.JButton();
        maleEditButton = new javax.swing.JRadioButton();
        lastNameEditField = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        addressEditField = new javax.swing.JTextArea();
        jLabel25 = new javax.swing.JLabel();
        firstNameEditField = new javax.swing.JTextField();
        femaleEditButton = new javax.swing.JRadioButton();
        jLabel26 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        payLevelEditCombo = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        saveEditButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel22.setText("First Name");

        departmentEditCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                departmentEditComboActionPerformed(evt);
            }
        });

        cancelButton1.setText("Cancel");
        cancelButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButton1ActionPerformed(evt);
            }
        });

        editGenderGroup.add(maleEditButton);
        maleEditButton.setText("Male");
        maleEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maleEditButtonActionPerformed(evt);
            }
        });

        lastNameEditField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastNameEditFieldActionPerformed(evt);
            }
        });

        jLabel24.setText("Last Name");

        addressEditField.setColumns(20);
        addressEditField.setRows(5);
        jScrollPane3.setViewportView(addressEditField);

        jLabel25.setText("Department");

        firstNameEditField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstNameEditFieldActionPerformed(evt);
            }
        });

        editGenderGroup.add(femaleEditButton);
        femaleEditButton.setText("Female");

        jLabel26.setText("Gender");

        jLabel19.setText("Pay Level");

        payLevelEditCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select pay level", "Item 2", "Item 3", "Item 4" }));
        payLevelEditCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payLevelEditComboActionPerformed(evt);
            }
        });

        jLabel21.setText("Address");

        saveEditButton.setText("Save Changes");
        saveEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveEditButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(268, 268, 268)
                        .addComponent(cancelButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(saveEditButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addGap(146, 146, 146)
                                .addComponent(jLabel24))
                            .addComponent(jLabel26)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(maleEditButton)
                                .addGap(18, 18, 18)
                                .addComponent(femaleEditButton))
                            .addComponent(jLabel19)
                            .addComponent(jLabel25)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21)
                            .addComponent(departmentEditCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(firstNameEditField, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lastNameEditField, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(payLevelEditCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lastNameEditField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(firstNameEditField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(jLabel22))
                        .addGap(29, 29, 29)))
                .addGap(18, 18, 18)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(maleEditButton)
                    .addComponent(femaleEditButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(departmentEditCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(payLevelEditCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton1)
                    .addComponent(saveEditButton))
                .addGap(23, 23, 23))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButton1ActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_cancelButton1ActionPerformed

    private void maleEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maleEditButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_maleEditButtonActionPerformed

    private void lastNameEditFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastNameEditFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lastNameEditFieldActionPerformed

    private void firstNameEditFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstNameEditFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_firstNameEditFieldActionPerformed

    private void saveEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveEditButtonActionPerformed
        // TODO add your handling code here:
        
                
        // Get the updated employee information
        String firstName = firstNameEditField.getText();
        String surname = lastNameEditField.getText();
        String address = addressEditField.getText();
        char gender = ' ';
        if (maleEditButton.isSelected()) {
            gender = 'M';
        } else if (femaleEditButton.isSelected()) {
            gender = 'F';
        }

    String selectedDepartment = (String) departmentEditCombo.getSelectedItem();
    Integer deptID = null;  // Default to no department

    if (selectedDepartment != null && !selectedDepartment.equals("No Department")) {
        for (Department dept : departments) {
            if (dept.getName().equals(selectedDepartment)) {
                deptID = dept.getDeptID();
                break;
            }
        }
    }

        // Validate and set pay level
        String selectedPayLevel = (String) payLevelEditCombo.getSelectedItem();
        int payLevel = -1;  // Default to invalid level

        if (selectedPayLevel != null && !selectedPayLevel.equals("Select Annual Salary")) {
            String[] parts = selectedPayLevel.split(" - ");
            if (parts.length == 2) {
                try {
                    payLevel = Integer.parseInt(parts[0].replace("Level ", "").trim());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid pay level selected.");
                    return;
                }
            }
        }

        // Validate that all required fields are filled
        if (firstName.isEmpty() || surname.isEmpty() || gender == ' ' || payLevel == -1) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields and select valid options.");
            return;
        }

        // Update the selected employee object
        selectedEmployee.setFirstName(firstName);
        selectedEmployee.setSurname(surname);
        selectedEmployee.setGender(gender);
        selectedEmployee.setAddress(address);
        selectedEmployee.setDeptID(deptID);
        selectedEmployee.setPayLevel(payLevel);
        
       // Update the allEmployees list 
        for (int i = 0; i < main.allEmployees.size(); i++) {
            if (main.allEmployees.get(i).getEmployeeId() == selectedEmployee.getEmployeeId()) {
                main.allEmployees.set(i, selectedEmployee);
                break;
            }
        }

        JOptionPane.showMessageDialog(this, "Employee information updated successfully!");

        // refresh detail page and table
        main.updateEmployeeDetails(selectedEmployee);
        main.refreshEmployeeTable(); 
        main.setEnabled(true);  
        this.dispose();
//        showSuccessToast(this,"Employee added successfully!");

    }//GEN-LAST:event_saveEditButtonActionPerformed

    
    private void departmentEditComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_departmentEditComboActionPerformed
        // TODO add your handling code here:
        
        // loop through and add all departments
        
        // save employee to department
        
        // remove employee from previous department
    }//GEN-LAST:event_departmentEditComboActionPerformed

    private void payLevelEditComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payLevelEditComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_payLevelEditComboActionPerformed

    
    
    public void showSuccessToast(JFrame parent, String message) {
        JWindow toast = new JWindow(parent); 
        toast.setLayout(new BorderLayout());

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(60, 179, 113)); 
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        toast.add(label, BorderLayout.CENTER);
        toast.pack();

        // Get location relative to parent window
        int x = parent.getX() + parent.getWidth() - toast.getWidth() - 20;
        int y = parent.getY() + parent.getHeight() - toast.getHeight() - 50;
        toast.setLocation(x, y);

        toast.setVisible(true);

        // Auto-close after 3 seconds
        new Timer(3000, e -> toast.dispose()).start();
    }
    public void showErrorToast(JFrame parent, String message) {
        JWindow toast = new JWindow(parent); 
        toast.setLayout(new BorderLayout());

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(220, 53, 69)); 
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        toast.add(label, BorderLayout.CENTER);
        toast.pack();

        // Get location relative to parent window
        int x = parent.getX() + parent.getWidth() - toast.getWidth() - 20;
        int y = parent.getY() + parent.getHeight() - toast.getHeight() - 50;
        toast.setLocation(x, y);

        toast.setVisible(true);

        // Auto-close after 3 seconds
        new Timer(3000, e -> toast.dispose()).start();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EditEmployeeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditEmployeeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditEmployeeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditEmployeeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new EditEmployeeForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea addressEditField;
    private javax.swing.JButton cancelButton1;
    private javax.swing.JComboBox<String> departmentEditCombo;
    private javax.swing.ButtonGroup editGenderGroup;
    private javax.swing.JRadioButton femaleEditButton;
    private javax.swing.JTextField firstNameEditField;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField lastNameEditField;
    private javax.swing.JRadioButton maleEditButton;
    private javax.swing.JComboBox<String> payLevelEditCombo;
    private javax.swing.JButton saveEditButton;
    // End of variables declaration//GEN-END:variables
}
