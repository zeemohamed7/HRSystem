/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import Logic.*;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.JFileChooser;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author zainab
 */
public class MainWindow extends javax.swing.JFrame {
    /**
     * Creates new form MainWindow
     */
    
    public static ArrayList<Employee> allEmployees;
    public static ArrayList<Department> departments;
    
    
    private Employee selectedEmployee;
    private Department selectedDepartment;
    private DefaultTableModel deptModel;


            
    public MainWindow() {
        initComponents();
              
        allEmployees = new ArrayList();
        departments = new ArrayList();
        

        
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
        
        // Add login and dashboard panel to main panel
        // Add generate pay report button
        JButton generatePayReportButton = new JButton("Generate Pay Report");
        generatePayReportButton.addActionListener(this::generatePayReportButtonActionPerformed);
        generatePayReportButton.setBackground(new Color(0, 102, 102));
        generatePayReportButton.setForeground(Color.WHITE);
        generatePayReportButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Add button to dashboard
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(255, 255, 255));
        buttonPanel.add(generatePayReportButton);
        
        // Add panels to main frame
        MainFrame.add(LoginPanel, "login");
        MainFrame.add(DashboardPanel, "dashboard");
        
       
        

        // DashboardPanel
        DashboardPanel.setLayout(new BorderLayout());

        // Sidebar
        sidemenuPanel.setPreferredSize(new Dimension(200, 0));
        DashboardPanel.add(sidemenuPanel, BorderLayout.WEST);

        // Right side (header + content)
        headerPanel.setPreferredSize(new Dimension(0, 60));
        rightPanel.setLayout(new BorderLayout()); 
        rightPanel.add(headerPanel, BorderLayout.NORTH);
        rightPanel.add(contentPanel, BorderLayout.CENTER);

        // Add right side to dashboard
        DashboardPanel.add(rightPanel, BorderLayout.CENTER);

        // Add employees, departments and payroll report to content panel
        contentPanel.add(employeesPanel, "employees");
        contentPanel.add(employeeDetailPanel, "employeeDetail");
        contentPanel.add(departmentsPanel, "departments");
        contentPanel.add(departmentDetailPanel, "departmentDetail");
        contentPanel.add(payrollPanel, "payroll");

        
        // all function init for data
        initialiseEmployeesTable();
        populateDepartmentsComboBox();

        
        
        String[] departmentColumnNames = {"ID", "Name", "Location", "Department Head"};

        // Create the table model
        deptModel = new DefaultTableModel(departmentColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };


        // Set model to the department table
        departmentsTable.setModel(deptModel);


    }
    
    private void initialiseEmployeesTable() {
    String[] columnNames = {"ID", "Full Name", "Department", "Gender", "Pay Level"};
    
    // Create table with model
    DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    

    // Populate the table with employee data
    for (Employee emp : allEmployees) {
        int id = emp.getEmployeeId();
        String fullName = emp.getFirstName() + " " + emp.getSurname();  // Concatenate first and last name
        String department = Department.getDepartmentNameById(departments, emp.getDeptID());
        char gender = emp.getGender();
        int payLevel = emp.getPayLevel();

        // Add the row to the model
        Object[] row = {id, fullName, department, gender, payLevel};
        model.addRow(row);
    }

    // Set model to employees table
    employeesTable.setModel(model);
    
}
    


    // show employee detail 
    private void showEmployeeDetails(Employee employee) {
        selectedEmployee = employee;
        // show employee detail page
        selectedEmployee = employee;
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "employeeDetail");
        
        //display data
        idDetailPage.setText(Integer.toString(employee.getEmployeeId()));
        firstNameDetailPage.setText(employee.getFirstName());
        surnameDetailPage.setText(employee.getSurname());
        genderDetailPage.setText(employee.getGender() == 'M' ? "Male" : "Female");
        payLevelDetailPage.setText(Integer.toString(employee.getPayLevel()));
        addressDetailPage.setText(employee.getAddress());
        String department = "No Department";  
        if (employee.getDeptID() != null) {
            department = Department.getDepartmentNameById(departments, employee.getDeptID());
        }
        departmentDetailPage.setText(department);        
        
    }
    
    
    // show department detail 
    private void showDepartmentDetails(Department dept) {
        selectedDepartment = dept;  
        // show employee detail page
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "departmentDetail");
        
        // display data

        departmentNameDetailPage.setText(dept.getName());
        idDepartmentDetailPage.setText(Integer.toString(dept.getDeptID()));
        locationDetailPage.setText(dept.getLocation());
       
        // if there is a department head (later)
        
        
        // display employees in department
            // Assuming you have a list of employees (e.g., allEmployees)
    StringBuilder employeeDetails = new StringBuilder();

    for (Employee employee : allEmployees) {
        if (employee.getDeptID() == (Integer) dept.getDeptID()) {
            // Add employee details to a display container
            employeeDetails.append("ID: ").append(employee.getEmployeeId())
                           .append(", Name: ").append(employee.getFirstName())
                           .append(" ").append(employee.getSurname())
                           .append("\n");
        }
    }

    // Assuming you have a JTextArea or JLabel for displaying employee details
    employeeListArea.setText(employeeDetails.toString());
        
    }
    


    
    
    // pagination function
    public void updatePagination(int totalPages, int currentPage) {
    paginationPanel.removeAll();

    // prev button
    JButton prevButton = new JButton("Previous");
    prevButton.setEnabled(currentPage > 1);
//    prevButton.addActionListener(e -> goToPage(currentPage - 1));
    paginationPanel.add(prevButton);

    // page number buttons
    for (int i = 1; i <= totalPages; i++) {
        JButton pageButton = new JButton(String.valueOf(i));
        if (i == currentPage) {
            pageButton.setEnabled(false); // Highlight current page
        }
        int page = i;
//        pageButton.addActionListener(e -> goToPage(page));
        paginationPanel.add(pageButton);
    }

    // next button
    JButton nextButton = new JButton("Next");
    nextButton.setEnabled(currentPage < totalPages);
//    nextButton.addActionListener(e -> goToPage(currentPage + 1));
    paginationPanel.add(nextButton);

    paginationPanel.revalidate();
    paginationPanel.repaint();
}

    
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

    private void populateDepartmentsComboBox() {
        departmentsListSelect.removeAllItems();
        departmentsListSelect.addItem("All Departments");
        for (Department dept : departments) {
            departmentsListSelect.addItem(dept.getName());
        }
    }


    
        /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        AddDepartmentForm = new javax.swing.JDialog();
        jLabel9 = new javax.swing.JLabel();
        departmentNameField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        locationField = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        departmentHeadSelect = new javax.swing.JComboBox<>();
        cancelButton2 = new javax.swing.JButton();
        addDepartmentConfirmButton = new javax.swing.JButton();
        ConfirmDelete = new javax.swing.JDialog();
        jLabel16 = new javax.swing.JLabel();
        cancelButton3 = new javax.swing.JButton();
        confirmDeleteButton = new javax.swing.JButton();
        MainFrame = new javax.swing.JPanel();
        LoginPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        loginButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButton2 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        DashboardPanel = new javax.swing.JPanel();
        sidemenuPanel = new javax.swing.JPanel();
        employeesButton = new javax.swing.JButton();
        departmentsButton = new javax.swing.JButton();
        payrollReportButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        rightPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        logoPanel = new javax.swing.JPanel();
        contentPanel = new javax.swing.JPanel();
        employeesPanel = new javax.swing.JPanel();
        searchEmployeesTextField = new javax.swing.JTextField();
        jScrollPane = new javax.swing.JScrollPane();
        employeesTable = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        paginationPanel = new javax.swing.JPanel();
        employeeDetailPanel = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        editEmployeeButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        idDetailPage = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        firstNameDetailPage = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        genderDetailPage = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        surnameDetailPage = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        departmentDetailPage = new javax.swing.JTextField();
        payLevelDetailPage = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        addressDetailPage = new javax.swing.JTextArea();
        backToEmployeesButton = new javax.swing.JButton();
        departmentsListSelect = new javax.swing.JComboBox<>();
        addEmployeeButton = new javax.swing.JButton();
        departmentsPanel = new javax.swing.JPanel();
        searchDepartmentsField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        departmentsTable = new javax.swing.JTable();
        addDepartmentButton = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        paginationPanel1 = new javax.swing.JPanel();
        departmentDetailPanel = new javax.swing.JPanel();
        backToDepartmentsButton = new javax.swing.JButton();
        editDepartmentButton = new javax.swing.JButton();
        deleteButton1 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        imageDepartmentPanel = new javax.swing.JPanel();
        idDepartmentDetailPage = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        locationDetailPage = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        deptHeadDetail = new javax.swing.JTextField();
        departmentNameDetailPage = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        employeeListArea = new javax.swing.JTextPane();
        payrollPanel = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        generateReportButton = new javax.swing.JButton();
        reportPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        reportTextPane = new javax.swing.JTextPane();

        jLabel9.setText("Department Name");

        departmentNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                departmentNameFieldActionPerformed(evt);
            }
        });

        jLabel10.setText("Location");

        locationField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locationFieldActionPerformed(evt);
            }
        });

        jLabel15.setText("Department Head");

        departmentHeadSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select department head", "Item 2", "Item 3", "Item 4" }));

        cancelButton2.setText("Cancel");
        cancelButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButton2ActionPerformed(evt);
            }
        });

        addDepartmentConfirmButton.setText("Add Department");
        addDepartmentConfirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDepartmentConfirmButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout AddDepartmentFormLayout = new javax.swing.GroupLayout(AddDepartmentForm.getContentPane());
        AddDepartmentForm.getContentPane().setLayout(AddDepartmentFormLayout);
        AddDepartmentFormLayout.setHorizontalGroup(
            AddDepartmentFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddDepartmentFormLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(AddDepartmentFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(AddDepartmentFormLayout.createSequentialGroup()
                        .addComponent(cancelButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addDepartmentConfirmButton))
                    .addGroup(AddDepartmentFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel9)
                        .addComponent(departmentNameField)
                        .addComponent(jLabel10)
                        .addComponent(locationField, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                        .addComponent(jLabel15)
                        .addComponent(departmentHeadSelect, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        AddDepartmentFormLayout.setVerticalGroup(
            AddDepartmentFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddDepartmentFormLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(departmentNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(locationField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(departmentHeadSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(AddDepartmentFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton2)
                    .addComponent(addDepartmentConfirmButton))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jLabel16.setText("Are you sure you want to delete this? This action cannot be undone.");

        cancelButton3.setText("Cancel");
        cancelButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButton3ActionPerformed(evt);
            }
        });

        confirmDeleteButton.setText("Delete");

        javax.swing.GroupLayout ConfirmDeleteLayout = new javax.swing.GroupLayout(ConfirmDelete.getContentPane());
        ConfirmDelete.getContentPane().setLayout(ConfirmDeleteLayout);
        ConfirmDeleteLayout.setHorizontalGroup(
            ConfirmDeleteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ConfirmDeleteLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel16)
                .addContainerGap(25, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ConfirmDeleteLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cancelButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(confirmDeleteButton)
                .addGap(25, 25, 25))
        );
        ConfirmDeleteLayout.setVerticalGroup(
            ConfirmDeleteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ConfirmDeleteLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ConfirmDeleteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton3)
                    .addComponent(confirmDeleteButton))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        MainFrame.setPreferredSize(new java.awt.Dimension(1380, 1000));
        MainFrame.setLayout(new java.awt.CardLayout());

        LoginPanel.setBackground(new java.awt.Color(250, 244, 222));
        LoginPanel.setForeground(new java.awt.Color(250, 235, 187));
        LoginPanel.setToolTipText("");
        LoginPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("HRConnect");
        jLabel1.setToolTipText("");
        jLabel1.setAlignmentY(0.0F);
        jLabel1.setAutoscrolls(true);
        LoginPanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 90, -1, -1));

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel2.setText("Employee Login");
        LoginPanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 130, -1, -1));

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 0, 12)); // NOI18N
        jLabel3.setText("Username");
        LoginPanel.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 180, -1, -1));

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        LoginPanel.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 200, 319, -1));

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 0, 12)); // NOI18N
        jLabel4.setText("Password");
        LoginPanel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 240, -1, -1));

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        LoginPanel.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 260, 319, -1));

        jLabel5.setText("Forgot password?");
        LoginPanel.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 340, -1, -1));

        jCheckBox1.setText("Remember me");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        LoginPanel.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 330, -1, -1));

        loginButton.setBackground(new java.awt.Color(0, 51, 102));
        loginButton.setForeground(new java.awt.Color(255, 255, 255));
        loginButton.setText("Log in");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });
        LoginPanel.add(loginButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 370, -1, -1));
        LoginPanel.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 400, 318, 5));

        jButton2.setBackground(new java.awt.Color(136, 156, 176));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Load Default Data");
        LoginPanel.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 420, -1, -1));

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 0, 8)); // NOI18N
        jLabel6.setText("Initialize the system with sample data from startup file");
        LoginPanel.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 450, -1, 17));

        MainFrame.add(LoginPanel, "login");

        employeesButton.setText("Employees");
        employeesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeesButtonActionPerformed(evt);
            }
        });

        departmentsButton.setText("Departments");
        departmentsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                departmentsButtonActionPerformed(evt);
            }
        });

        payrollReportButton.setText("Payroll Report");
        payrollReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payrollReportButtonActionPerformed(evt);
            }
        });

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout sidemenuPanelLayout = new javax.swing.GroupLayout(sidemenuPanel);
        sidemenuPanel.setLayout(sidemenuPanelLayout);
        sidemenuPanelLayout.setHorizontalGroup(
            sidemenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidemenuPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(sidemenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(departmentsButton)
                    .addComponent(employeesButton)
                    .addComponent(payrollReportButton)
                    .addComponent(exitButton))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        sidemenuPanelLayout.setVerticalGroup(
            sidemenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidemenuPanelLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(employeesButton)
                .addGap(18, 18, 18)
                .addComponent(departmentsButton)
                .addGap(18, 18, 18)
                .addComponent(payrollReportButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 351, Short.MAX_VALUE)
                .addComponent(exitButton)
                .addGap(286, 286, 286))
        );

        jLabel7.setText("HRConnect");

        jLabel8.setText("John Doe");

        javax.swing.GroupLayout logoPanelLayout = new javax.swing.GroupLayout(logoPanel);
        logoPanel.setLayout(logoPanelLayout);
        logoPanelLayout.setHorizontalGroup(
            logoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 51, Short.MAX_VALUE)
        );
        logoPanelLayout.setVerticalGroup(
            logoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 37, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(logoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 390, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(178, 178, 178))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addGap(29, 29, 29))
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(logoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        contentPanel.setLayout(new java.awt.CardLayout());

        searchEmployeesTextField.setText("Search Employees..");
        searchEmployeesTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchEmployeesTextFieldFocusGained(evt);
            }
        });
        searchEmployeesTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchEmployeesTextFieldActionPerformed(evt);
            }
        });

        employeesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                employeesTableMouseClicked(evt);
            }
        });
        jScrollPane.setViewportView(employeesTable);

        jLabel12.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel12.setText("Employees");

        jLabel11.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N

        javax.swing.GroupLayout paginationPanelLayout = new javax.swing.GroupLayout(paginationPanel);
        paginationPanel.setLayout(paginationPanelLayout);
        paginationPanelLayout.setHorizontalGroup(
            paginationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 222, Short.MAX_VALUE)
        );
        paginationPanelLayout.setVerticalGroup(
            paginationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
        );

        jLabel17.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel17.setText("Employee Detail");

        editEmployeeButton.setText("Edit");
        editEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editEmployeeButtonActionPerformed(evt);
            }
        });

        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        idDetailPage.setEditable(false);
        idDetailPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idDetailPageActionPerformed(evt);
            }
        });

        jLabel33.setText("ID");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33)
                    .addComponent(idDetailPage, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(idDetailPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        jLabel27.setText("First Name");

        firstNameDetailPage.setEditable(false);

        jLabel28.setText("Gender");

        genderDetailPage.setEditable(false);
        genderDetailPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                genderDetailPageActionPerformed(evt);
            }
        });

        jLabel29.setText("Paylevel");

        jLabel30.setText("Surname");

        surnameDetailPage.setEditable(false);

        jLabel31.setText("Department");

        departmentDetailPage.setEditable(false);

        payLevelDetailPage.setEditable(false);

        jLabel32.setText("Address");

        addressDetailPage.setEditable(false);
        addressDetailPage.setColumns(20);
        addressDetailPage.setRows(5);
        jScrollPane2.setViewportView(addressDetailPage);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(payLevelDetailPage, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(firstNameDetailPage, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel28)
                                    .addComponent(jLabel27)
                                    .addComponent(genderDetailPage, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(55, 55, 55)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(surnameDetailPage, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel31)
                                    .addComponent(jLabel30)
                                    .addComponent(departmentDetailPage, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel32))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(jLabel30))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(firstNameDetailPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(surnameDetailPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(jLabel31))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(genderDetailPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(departmentDetailPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(payLevelDetailPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        backToEmployeesButton.setText("Back to list");
        backToEmployeesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backToEmployeesButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout employeeDetailPanelLayout = new javax.swing.GroupLayout(employeeDetailPanel);
        employeeDetailPanel.setLayout(employeeDetailPanelLayout);
        employeeDetailPanelLayout.setHorizontalGroup(
            employeeDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeDetailPanelLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(employeeDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(employeeDetailPanelLayout.createSequentialGroup()
                        .addGroup(employeeDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(backToEmployeesButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(editEmployeeButton)
                        .addGap(18, 18, 18)
                        .addComponent(deleteButton)
                        .addGap(31, 31, 31))))
        );
        employeeDetailPanelLayout.setVerticalGroup(
            employeeDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeDetailPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel17)
                .addGap(28, 28, 28)
                .addGroup(employeeDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editEmployeeButton)
                    .addComponent(deleteButton)
                    .addComponent(backToEmployeesButton))
                .addGap(34, 34, 34)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        departmentsListSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Departments", "Item 2", "Item 3", "Item 4" }));
        departmentsListSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                departmentsListSelectActionPerformed(evt);
            }
        });

        addEmployeeButton.setText("+ Add Employee");
        addEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEmployeeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout employeesPanelLayout = new javax.swing.GroupLayout(employeesPanel);
        employeesPanel.setLayout(employeesPanelLayout);
        employeesPanelLayout.setHorizontalGroup(
            employeesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeesPanelLayout.createSequentialGroup()
                .addGroup(employeesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(employeesPanelLayout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(paginationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(employeesPanelLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(employeesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addGroup(employeesPanelLayout.createSequentialGroup()
                                .addGroup(employeesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(employeesPanelLayout.createSequentialGroup()
                                        .addComponent(searchEmployeesTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(employeesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(departmentsListSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(addEmployeeButton)))
                                    .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 792, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(employeeDetailPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(1333, Short.MAX_VALUE))
            .addGroup(employeesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(employeesPanelLayout.createSequentialGroup()
                    .addGap(477, 477, 477)
                    .addComponent(jLabel11)
                    .addContainerGap(2154, Short.MAX_VALUE)))
        );
        employeesPanelLayout.setVerticalGroup(
            employeesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeesPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(employeesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(employeesPanelLayout.createSequentialGroup()
                        .addGroup(employeesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(employeesPanelLayout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(searchEmployeesTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(employeesPanelLayout.createSequentialGroup()
                                .addComponent(addEmployeeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(departmentsListSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(employeeDetailPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(paginationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(427, 427, 427))
            .addGroup(employeesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(employeesPanelLayout.createSequentialGroup()
                    .addGap(254, 254, 254)
                    .addComponent(jLabel11)
                    .addContainerGap(802, Short.MAX_VALUE)))
        );

        contentPanel.add(employeesPanel, "card4");

        searchDepartmentsField.setText("Search Departments..");
        searchDepartmentsField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchDepartmentsFieldFocusGained(evt);
            }
        });
        searchDepartmentsField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchDepartmentsFieldActionPerformed(evt);
            }
        });

        departmentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Location", "Department Head"
            }
        ));
        departmentsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                departmentsTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(departmentsTable);
        if (departmentsTable.getColumnModel().getColumnCount() > 0) {
            departmentsTable.getColumnModel().getColumn(3).setResizable(false);
        }

        addDepartmentButton.setText("+ Add Department");
        addDepartmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDepartmentButtonActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel13.setText("Departments");

        jLabel14.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N

        javax.swing.GroupLayout paginationPanel1Layout = new javax.swing.GroupLayout(paginationPanel1);
        paginationPanel1.setLayout(paginationPanel1Layout);
        paginationPanel1Layout.setHorizontalGroup(
            paginationPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 222, Short.MAX_VALUE)
        );
        paginationPanel1Layout.setVerticalGroup(
            paginationPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
        );

        backToDepartmentsButton.setText("Back to list");
        backToDepartmentsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backToDepartmentsButtonActionPerformed(evt);
            }
        });

        editDepartmentButton.setText("Edit");
        editDepartmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editDepartmentButtonActionPerformed(evt);
            }
        });

        deleteButton1.setText("Delete");
        deleteButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButton1ActionPerformed(evt);
            }
        });

        jLabel18.setText("Employees in Department");

        javax.swing.GroupLayout imageDepartmentPanelLayout = new javax.swing.GroupLayout(imageDepartmentPanel);
        imageDepartmentPanel.setLayout(imageDepartmentPanelLayout);
        imageDepartmentPanelLayout.setHorizontalGroup(
            imageDepartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 74, Short.MAX_VALUE)
        );
        imageDepartmentPanelLayout.setVerticalGroup(
            imageDepartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 61, Short.MAX_VALUE)
        );

        idDepartmentDetailPage.setEditable(false);
        idDepartmentDetailPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idDepartmentDetailPageActionPerformed(evt);
            }
        });

        jLabel34.setText("ID:");

        jLabel35.setText("Location");

        locationDetailPage.setEditable(false);
        locationDetailPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locationDetailPageActionPerformed(evt);
            }
        });

        jLabel38.setText("Department Head");

        deptHeadDetail.setEditable(false);

        departmentNameDetailPage.setText("Department Name Placeholder");

        employeeListArea.setEditable(false);
        jScrollPane3.setViewportView(employeeListArea);

        javax.swing.GroupLayout departmentDetailPanelLayout = new javax.swing.GroupLayout(departmentDetailPanel);
        departmentDetailPanel.setLayout(departmentDetailPanelLayout);
        departmentDetailPanelLayout.setHorizontalGroup(
            departmentDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(departmentDetailPanelLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(backToDepartmentsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(editDepartmentButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deleteButton1)
                .addGap(20, 20, 20))
            .addGroup(departmentDetailPanelLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(departmentDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addGroup(departmentDetailPanelLayout.createSequentialGroup()
                        .addGroup(departmentDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(departmentDetailPanelLayout.createSequentialGroup()
                                .addComponent(imageDepartmentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, departmentDetailPanelLayout.createSequentialGroup()
                                .addComponent(jLabel34)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addComponent(idDepartmentDetailPage, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(departmentDetailPanelLayout.createSequentialGroup()
                        .addGroup(departmentDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel35)
                            .addComponent(locationDetailPage, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(41, 41, 41)
                        .addGroup(departmentDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(deptHeadDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel38))))
                .addGap(0, 67, Short.MAX_VALUE))
            .addGroup(departmentDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(departmentDetailPanelLayout.createSequentialGroup()
                    .addGap(125, 125, 125)
                    .addComponent(departmentNameDetailPage)
                    .addContainerGap(233, Short.MAX_VALUE)))
        );
        departmentDetailPanelLayout.setVerticalGroup(
            departmentDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(departmentDetailPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(departmentDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backToDepartmentsButton)
                    .addComponent(editDepartmentButton)
                    .addComponent(deleteButton1))
                .addGap(18, 18, 18)
                .addGroup(departmentDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(departmentDetailPanelLayout.createSequentialGroup()
                        .addComponent(imageDepartmentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, departmentDetailPanelLayout.createSequentialGroup()
                        .addGroup(departmentDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(idDepartmentDetailPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34))
                        .addGap(18, 18, 18)))
                .addGroup(departmentDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(jLabel38))
                .addGap(8, 8, 8)
                .addGroup(departmentDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(locationDetailPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deptHeadDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addComponent(jLabel18)
                .addGap(33, 33, 33)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(83, Short.MAX_VALUE))
            .addGroup(departmentDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(departmentDetailPanelLayout.createSequentialGroup()
                    .addGap(98, 98, 98)
                    .addComponent(departmentNameDetailPage)
                    .addContainerGap(603, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout departmentsPanelLayout = new javax.swing.GroupLayout(departmentsPanel);
        departmentsPanel.setLayout(departmentsPanelLayout);
        departmentsPanelLayout.setHorizontalGroup(
            departmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(departmentsPanelLayout.createSequentialGroup()
                .addGroup(departmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(departmentsPanelLayout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(paginationPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(departmentsPanelLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(departmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(searchDepartmentsField, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, departmentsPanelLayout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 792, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(departmentDetailPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(addDepartmentButton, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap(1263, Short.MAX_VALUE))
            .addGroup(departmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(departmentsPanelLayout.createSequentialGroup()
                    .addGap(477, 477, 477)
                    .addComponent(jLabel14)
                    .addContainerGap(2154, Short.MAX_VALUE)))
        );
        departmentsPanelLayout.setVerticalGroup(
            departmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(departmentsPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel13)
                .addGap(17, 17, 17)
                .addComponent(addDepartmentButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(searchDepartmentsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(departmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(departmentsPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(departmentsPanelLayout.createSequentialGroup()
                        .addGap(121, 121, 121)
                        .addComponent(departmentDetailPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(paginationPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(departmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(departmentsPanelLayout.createSequentialGroup()
                    .addGap(254, 254, 254)
                    .addComponent(jLabel14)
                    .addContainerGap(802, Short.MAX_VALUE)))
        );

        contentPanel.add(departmentsPanel, "card4");

        jLabel36.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel36.setText("Payroll Report");

        generateReportButton.setText("Generate Report");
        generateReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateReportButtonActionPerformed(evt);
            }
        });

        jScrollPane4.setViewportView(reportTextPane);

        javax.swing.GroupLayout reportPanelLayout = new javax.swing.GroupLayout(reportPanel);
        reportPanel.setLayout(reportPanelLayout);
        reportPanelLayout.setHorizontalGroup(
            reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 642, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        reportPanelLayout.setVerticalGroup(
            reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 433, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout payrollPanelLayout = new javax.swing.GroupLayout(payrollPanel);
        payrollPanel.setLayout(payrollPanelLayout);
        payrollPanelLayout.setHorizontalGroup(
            payrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(payrollPanelLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(payrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(reportPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(payrollPanelLayout.createSequentialGroup()
                        .addComponent(jLabel36)
                        .addGap(385, 385, 385)
                        .addComponent(generateReportButton)))
                .addContainerGap(1946, Short.MAX_VALUE))
        );
        payrollPanelLayout.setVerticalGroup(
            payrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(payrollPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(payrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(generateReportButton))
                .addGap(26, 26, 26)
                .addComponent(reportPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(536, Short.MAX_VALUE))
        );

        contentPanel.add(payrollPanel, "card4");

        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2655, Short.MAX_VALUE)
            .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(rightPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(rightPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(contentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1554, Short.MAX_VALUE)
            .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(rightPanelLayout.createSequentialGroup()
                    .addGap(33, 33, 33)
                    .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(1449, Short.MAX_VALUE)))
            .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(rightPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(contentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout DashboardPanelLayout = new javax.swing.GroupLayout(DashboardPanel);
        DashboardPanel.setLayout(DashboardPanelLayout);
        DashboardPanelLayout.setHorizontalGroup(
            DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DashboardPanelLayout.createSequentialGroup()
                .addGap(136, 136, 136)
                .addComponent(rightPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(sidemenuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        DashboardPanelLayout.setVerticalGroup(
            DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DashboardPanelLayout.createSequentialGroup()
                .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(DashboardPanelLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(rightPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(DashboardPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(sidemenuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        MainFrame.add(DashboardPanel, "dashboard");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(MainFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 1168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(MainFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) MainFrame.getLayout();
        cl.show(MainFrame, "dashboard");

    }//GEN-LAST:event_loginButtonActionPerformed

    private void employeesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeesButtonActionPerformed
        // TODO add your handling code here:
        CardLayout cl = (CardLayout)(contentPanel.getLayout());
        cl.show(contentPanel, "employees");

    }//GEN-LAST:event_employeesButtonActionPerformed

    private void addEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEmployeeButtonActionPerformed
        // TODO add your handling code here:
    // Create and show the AddEmployeeForm
    // Hide the main window
    setVisible(false);

    // Open the AddEmployeeForm
    AddEmployeeForm addEmployeeForm = new AddEmployeeForm(this);
    addEmployeeForm.setVisible(true);

           
    }//GEN-LAST:event_addEmployeeButtonActionPerformed

    private void searchEmployeesTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchEmployeesTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchEmployeesTextFieldActionPerformed

    private void searchEmployeesTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchEmployeesTextFieldFocusGained
        // TODO add your handling code here:
        searchEmployeesTextField.setText("");
    }//GEN-LAST:event_searchEmployeesTextFieldFocusGained

    private void departmentsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_departmentsButtonActionPerformed
        // TODO add your handling code here:
        CardLayout cl = (CardLayout)(contentPanel.getLayout());
        cl.show(contentPanel, "departments");
    }//GEN-LAST:event_departmentsButtonActionPerformed

    private void addDepartmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDepartmentButtonActionPerformed
        // TODO add your handling code here:
        AddDepartmentForm.setTitle("Add Department");
        AddDepartmentForm.setSize(450,300);
        AddDepartmentForm.setLocationRelativeTo(null);
        AddDepartmentForm.setModal(true); 
        AddDepartmentForm.setVisible(true);
    }//GEN-LAST:event_addDepartmentButtonActionPerformed

    private void searchDepartmentsFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchDepartmentsFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchDepartmentsFieldActionPerformed

    private void searchDepartmentsFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchDepartmentsFieldFocusGained
        // TODO add your handling code here:
        searchDepartmentsField.setText("");
    }//GEN-LAST:event_searchDepartmentsFieldFocusGained

    private void departmentNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_departmentNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_departmentNameFieldActionPerformed

    private void locationFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locationFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_locationFieldActionPerformed

    private void cancelButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButton2ActionPerformed
        // TODO add your handling code here:
        AddDepartmentForm.dispose();

    }//GEN-LAST:event_cancelButton2ActionPerformed

    private void addDepartmentConfirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDepartmentConfirmButtonActionPerformed
        // Get values from form fields
        String departmentName = departmentNameField.getText().trim();
        String location = locationField.getText().trim();
        
        // Validate input
        if (departmentName.isEmpty()) {
            showErrorToast(this, "Department name is required");
            departmentNameField.requestFocus();
            return;
        }
        
        if (location.isEmpty()) {
            showErrorToast(this, "Location is required");
            locationField.requestFocus();
            return;
        }
        
        // Create new department
        Department newDepartment = new Department(departmentName, location);
        
        // Add to departments list
        departments.add(newDepartment);
        
        // Update departments table
        DefaultTableModel model = (DefaultTableModel) departmentsTable.getModel();
        model.addRow(new Object[]{
            newDepartment.getDeptID(),
            newDepartment.getName(),
            newDepartment.getLocation(),
            "Not Assigned"
        });
        
        // Clear form fields
        departmentNameField.setText("");
        locationField.setText("");
        
        // Show success message
        showSuccessToast(this, "Department added successfully");
        
        // Close the form
        AddDepartmentForm.dispose();
        
        populateDepartmentsComboBox(); //refresh combo box
        refreshDepartmentsComboBox();
    }//GEN-LAST:event_addDepartmentConfirmButtonActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    private void generatePayReportButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // Check if there are any employees
        

    }

    private void cancelButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cancelButton3ActionPerformed

    private void employeesTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_employeesTableMouseClicked
        // TODO add your handling code here:

        int selectedRow = employeesTable.getSelectedRow();
        // check if row selected
       if (selectedRow != -1) {
           // get employee id
           Object employeeId = employeesTable.getValueAt(selectedRow, 0);

           Employee employee = null;
           // find employee by ID to send over to detail page
           for (Employee emp : allEmployees) {
               if (emp.getEmployeeId() == (Integer) employeeId) { // cast to compare
                   employee = emp;
                   break;
               }
           }

           if (employee != null) {
               showEmployeeDetails(employee);
           } else {
               JOptionPane.showMessageDialog(null, "Employee not found");
           }
       }
    }//GEN-LAST:event_employeesTableMouseClicked

    private void editEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editEmployeeButtonActionPerformed
        // TODO add your handling code here:
        
        EditEmployeeForm editForm = new EditEmployeeForm(this, selectedEmployee);
        editForm.setVisible(true);



    }//GEN-LAST:event_editEmployeeButtonActionPerformed

    private void idDetailPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idDetailPageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idDetailPageActionPerformed

    private void departmentsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_departmentsTableMouseClicked
        // TODO add your handling code here:
        // fake data to test detail page
        // fake data
      
        
        int selectedRow = departmentsTable.getSelectedRow();
        // check if row selected
       if (selectedRow != -1) {
           // get employee id
           Object deptId = departmentsTable.getValueAt(selectedRow, 0);

           Department selectedDepartment = null;
           // find employee by ID to send over to detail page
           for (Department dept : departments) {
               if (dept.getDeptID() == (Integer) deptId) { // cast to compare
                   selectedDepartment = dept;
                   break;
               }
           }

           if (selectedDepartment != null) {
               showDepartmentDetails(selectedDepartment);
           } else {
               JOptionPane.showMessageDialog(null, "Employee not found");
           }
       }


    }//GEN-LAST:event_departmentsTableMouseClicked

    private void genderDetailPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_genderDetailPageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_genderDetailPageActionPerformed

    private void backToEmployeesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backToEmployeesButtonActionPerformed
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "employees");

    }//GEN-LAST:event_backToEmployeesButtonActionPerformed

    private void backToDepartmentsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backToDepartmentsButtonActionPerformed
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "departments");
    }//GEN-LAST:event_backToDepartmentsButtonActionPerformed

    private void editDepartmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editDepartmentButtonActionPerformed
        // TODO add your handling code here:
        EditDepartmentForm editForm = new EditDepartmentForm(this, selectedDepartment);
        editForm.setVisible(true);

    }//GEN-LAST:event_editDepartmentButtonActionPerformed

    private void idDepartmentDetailPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idDepartmentDetailPageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idDepartmentDetailPageActionPerformed

    private void locationDetailPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locationDetailPageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_locationDetailPageActionPerformed

    private void generateReportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateReportButtonActionPerformed
        // Check if there are any employees
        if (allEmployees == null || allEmployees.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No employees found to generate payroll report.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Determine the file path for the payroll report
            String userHome = System.getProperty("user.home");
            File payrollDir = new File(userHome, "HRSystemReports");
            if (!payrollDir.exists()) {
                if (!payrollDir.mkdirs()) {
                    throw new IOException("Failed to create reports directory: " + payrollDir.getAbsolutePath());
                }
            }
            
            File payrollFile = new File(payrollDir, "payroll.txt");
            
            // Ensure file can be created and written
            if (payrollFile.exists()) {
                if (!payrollFile.delete()) {
                    throw new IOException("Cannot overwrite existing file: " + payrollFile.getAbsolutePath());
                }
            }
            
            if (!payrollFile.createNewFile()) {
                throw new IOException("Cannot create payroll report file: " + payrollFile.getAbsolutePath());
            }
            
            // Additional check for write permissions
            if (!payrollFile.canWrite()) {
                // Try to set writable
                if (!payrollFile.setWritable(true)) {
                    throw new IOException("No write permissions for file: " + payrollFile.getAbsolutePath());
                }
            }

            try (PrintWriter writer = new PrintWriter(payrollFile)) {
                // Company-wide total
                double companyTotal = 0.0;

                // Sort departments to ensure consistent ordering
                ArrayList<Department> sortedDepartments = new ArrayList<>(departments);
                sortedDepartments.sort((d1, d2) -> d1.getName().compareTo(d2.getName()));

                // Iterate through departments
                for (Department dept : sortedDepartments) {
                    // Department header
                    writer.println("Department: " + dept.getName());
                    writer.println("-----------------------------------");

                    // Department total
                    double departmentTotal = 0.0;

                    // Find and write employees in this department
                    for (Employee emp : allEmployees) {
                        if (emp.getDeptID() != null && emp.getDeptID() == dept.getDeptID()) {
                            // Calculate 2-week pay (1/26th of annual salary)
                            double biweeklyPay = calculateBiweeklyPay(emp.getPayLevel());
                            
                            // Write employee details
                            writer.printf("Employee ID: %d\n", emp.getEmployeeId());
                            writer.printf("Name: %s %s\n", emp.getFirstName(), emp.getSurname());
                            writer.printf("2-Week Pay: BHD %.2f\n\n", biweeklyPay);

                            // Update totals
                            departmentTotal += biweeklyPay;
                            companyTotal += biweeklyPay;
                        }
                    }

                    // Department total
                    writer.printf("Department Total: BHD %.2f\n\n", departmentTotal);
                }

                // Company total
                writer.printf("COMPANY TOTAL: BHD %.2f\n", companyTotal);
            }

            // Update report text pane
            try (BufferedReader reader = new BufferedReader(new FileReader(payrollFile))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                reportTextPane.setText(content.toString());
            }

            // Get the project directory
            String projectDir = System.getProperty("user.dir");
            File downloadFile = new File(projectDir, "payroll.txt");
            
            try {
                // Copy the file to project directory
                Files.copy(payrollFile.toPath(), downloadFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
                JOptionPane.showMessageDialog(
                    this, 
                    "Payroll report saved to:\n" + downloadFile.getAbsolutePath(), 
                    "Report Saved", 
                    JOptionPane.INFORMATION_MESSAGE
                );
            } catch (IOException downloadEx) {
                JOptionPane.showMessageDialog(
                    this, 
                    "Error saving file:\n" + downloadEx.getMessage(), 
                    "Save Error", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (IOException e) {
            // Print full stack trace for detailed debugging
            e.printStackTrace();
            
            // Log error details
            System.err.println("Payroll Report Generation Error: " + e.getMessage());
            System.err.println("File Path: " + System.getProperty("user.home") + "/HRSystemReports/payroll.txt");
            System.err.println("Current Working Directory: " + System.getProperty("user.dir"));
            
            // Show user-friendly error message
            JOptionPane.showMessageDialog(this, 
                "Error generating payroll report:\n" + 
                e.getMessage() + 
                "\n\nPlease check file permissions and try again.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            // Print full stack trace for unexpected errors
            e.printStackTrace();
            
            // Log unexpected error details
            System.err.println("Unexpected Error: " + e.getMessage());
            
            // Show user-friendly error message
            JOptionPane.showMessageDialog(this, 
                "Unexpected error:\n" + e.getMessage() + 
                "\n\nPlease contact support.", 
                "Critical Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_generateReportButtonActionPerformed

    private void payrollReportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payrollReportButtonActionPerformed
        // TODO add your handling code here:
        CardLayout cl = (CardLayout)(contentPanel.getLayout());
        cl.show(contentPanel, "payroll");
    }//GEN-LAST:event_payrollReportButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure that you want to delete this employee?", 
                "DELETE Employee", 2, JOptionPane.YES_NO_OPTION);
      
       if(confirm == JOptionPane.YES_OPTION){
       
       //remove the employee from the data source
       deleteEmployee(selectedEmployee);
       JOptionPane.showMessageDialog(this, "Employee deleted succefully.");
       
       employeeDetailPanel.setVisible(false);
       
    // Switch back to Employee Panel 
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "employeePanel");
       
       }
        
    }//GEN-LAST:event_deleteButtonActionPerformed
    private void deleteEmployee(Employee employee){
    allEmployees.remove(employee);
    refreshEmployeeTable();
    }
    
    
    private void deleteButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButton1ActionPerformed
        // TODO add your handling code here:
         int confirm = JOptionPane.showConfirmDialog(this, "Are you sure that you want to delete this department?",
                 "DELETE Department", 2);
        
        if(confirm == JOptionPane.YES_OPTION){
       
       //remove the department 
       deleteDepartment(selectedDepartment);
       JOptionPane.showMessageDialog(this, "Department deleted succefully.");
       
              departmentDetailPanel.setVisible(false);
       
    // Switch back to Employee Panel 
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "departmentPanel");
       }
        refreshEmployeeTable();
        refreshDepartmentTable();
    }//GEN-LAST:event_deleteButton1ActionPerformed

    private void departmentsListSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_departmentsListSelectActionPerformed
// TODO add your handling code here:
    String selectedDepartment = (String) departmentsListSelect.getSelectedItem();
    
    if (selectedDepartment == null || selectedDepartment.equals("All Departments")) {
        // Show all employees (including those with no department)
        selectedDepartment = "All Departments";
    }

    DefaultTableModel model = (DefaultTableModel) employeesTable.getModel();
    model.setRowCount(0); // Clear the table

    for (Employee emp : allEmployees) {
        Integer deptID = emp.getDeptID();
        String department = "No Department"; // Default for employees with no department

        // If deptID is not null, fetch the department name
        if (deptID != null) {
            department = Department.getDepartmentNameById(departments, deptID);
        }

        // Check if "All Departments" is selected or the department matches
        if (selectedDepartment.equals("All Departments") || department.equals(selectedDepartment)) {
            int id = emp.getEmployeeId();
            String fullName = emp.getFirstName() + " " + emp.getSurname();
            char gender = emp.getGender();
            int payLevel = emp.getPayLevel();

            Object[] row = {id, fullName, department, gender, payLevel};
            model.addRow(row);
        }
    }

    }//GEN-LAST:event_departmentsListSelectActionPerformed
    private void deleteDepartment(Department department){
        departments.remove(department);
        refreshDepartmentsComboBox();
    }
    
    public void refreshDepartmentsTable() {
        // Clear the current table data
        deptModel.setRowCount(0);

        // Iterate through the list of departments and add them to the table
        for (Department dept : departments) {
            // Check if department has a head, otherwise display blank
            String headName = (dept.getDepartmentHead() != null) ? dept.getDepartmentHead().getFirstName() + " " + dept.getDepartmentHead().getSurname() : "";
            
            Object[] rowData = { 
                dept.getDeptID(), 
                dept.getName(), 
                dept.getLocation(), 
                headName  // Display department head or leave blank
            };
            
            deptModel.addRow(rowData);
        }
    }
 
    public void refreshEmployeeTable() {
        // Get the selected department from the combo box
        String selectedDepartment = (String) departmentsListSelect.getSelectedItem();

        // Default to "All Departments" if selectedDepartment is null
        if (selectedDepartment == null) {
            selectedDepartment = "All Departments";
        }

        DefaultTableModel model = (DefaultTableModel) employeesTable.getModel();
        model.setRowCount(0);  // Clear existing rows

        // Loop through all employees
        for (Employee emp : allEmployees) {
            int id = emp.getEmployeeId();
            String fullName = emp.getFirstName() + " " + emp.getSurname();  // Concatenate first and last name
            Integer deptId = emp.getDeptID();
            String department = "No Department";  // Default value if no department is assigned

            if (deptId != null) {
                department = Department.getDepartmentNameById(departments, deptId);
            }

            // Filter employees based on the selected department
            if (selectedDepartment.equals("All Departments") || department.equals(selectedDepartment)) {
                char gender = emp.getGender();
                int payLevel = emp.getPayLevel();

                // Add the row to the model
                Object[] row = {id, fullName, department, gender, payLevel};
                model.addRow(row);
            }
        }
    }

        public void refreshDepartmentTable() {
            // Assuming you have a JTable named departmentTable
            DefaultTableModel model = (DefaultTableModel) departmentsTable.getModel();

            // Clear existing rows
            model.setRowCount(0);

            // Populate the table with updated department data
            for (Department dept : departments) {  // Assuming you have a method to get departments
                // Add department data as rows
                model.addRow(new Object[]{
                    dept.getDeptID(), 
                    dept.getName(), 
                    dept.getLocation(),
                    dept.getDepartmentHead() != null ? dept.getDepartmentHead().getFirstName() + " " + dept.getDepartmentHead().getSurname() : "No Head"
                });
            }
        }

        private void refreshDepartmentsComboBox() {
        departmentsListSelect.removeAllItems();  // Clear all existing items

        // Add "All Departments" option at the top
        departmentsListSelect.addItem("All Departments");
        

        // Now re-populate the combo box with the updated list of departments
        for (Department dept : departments) {
            departmentsListSelect.addItem(dept.getName());  // Add department names
        }
        
        refreshEmployeeTable();
}
        
        public void updateEmployeeDetails(Employee updatedEmployee) {
    if (selectedEmployee != null && selectedEmployee.getEmployeeId() == updatedEmployee.getEmployeeId()) {
        // Update the displayed information
        firstNameDetailPage.setText(updatedEmployee.getFirstName());
        surnameDetailPage.setText(updatedEmployee.getSurname());
        addressDetailPage.setText(updatedEmployee.getAddress());
        genderDetailPage.setText(updatedEmployee.getGender() == 'M' ? "Male" : "Female");

        // Find and display the department name
        String departmentName = "No Department";
        for (Department dept : departments) {
            if(updatedEmployee.getDeptID() != null) {
                departmentName = Department.getDepartmentNameById(departments, updatedEmployee.getDeptID());
            }
        }
        departmentDetailPage.setText(departmentName);

        // Pay Level
        payLevelDetailPage.setText(Integer.toString(updatedEmployee.getPayLevel()));
    }
}

    // Helper method to calculate biweekly pay based on pay level
    private double calculateBiweeklyPay(int payLevel) {
        // Pay levels and their corresponding annual salaries
        double[] payLevelSalaries = {
            0.0,     // Level 0 (placeholder)
            45000.0, // Level 1
            54000.0, // Level 2
            63000.0, // Level 3
            72000.0, // Level 4
            81000.0, // Level 5
            71258.22, // Level 6
            80946.95, // Level 7
            96336.34  // Level 8
        };

        // Validate pay level
        if (payLevel < 1 || payLevel >= payLevelSalaries.length) {
            return 0.0; // Default to 0 if pay level is invalid
        }

        // Calculate biweekly pay (1/26th of annual salary)
        return payLevelSalaries[payLevel] / 26.0;
    }

    public void updateDepartmentDetails(Department updatedDepartment) {
        if (selectedDepartment != null && selectedDepartment.getDeptID() == updatedDepartment.getDeptID()) {
            // Update the department name
            departmentNameDetailPage.setText(updatedDepartment.getName());

            // Update the department ID
            idDepartmentDetailPage.setText(Integer.toString(updatedDepartment.getDeptID()));

            // Update the department location
            locationDetailPage.setText(updatedDepartment.getLocation());

            // Update the department head, if any
            if (updatedDepartment.getDepartmentHead() != null) {
                deptHeadDetail.setText(updatedDepartment.getDepartmentHead().getFirstName() + " " + updatedDepartment.getDepartmentHead().getSurname());
            } else {
                deptHeadDetail.setText("No Head"); // Or leave it blank
            }
        }
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
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog AddDepartmentForm;
    private javax.swing.JDialog ConfirmDelete;
    private javax.swing.JPanel DashboardPanel;
    private javax.swing.JPanel LoginPanel;
    private javax.swing.JPanel MainFrame;
    private javax.swing.JButton addDepartmentButton;
    private javax.swing.JButton addDepartmentConfirmButton;
    private javax.swing.JButton addEmployeeButton;
    private javax.swing.JTextArea addressDetailPage;
    private javax.swing.JButton backToDepartmentsButton;
    private javax.swing.JButton backToEmployeesButton;
    private javax.swing.JButton cancelButton2;
    private javax.swing.JButton cancelButton3;
    private javax.swing.JButton confirmDeleteButton;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton deleteButton1;
    private javax.swing.JTextField departmentDetailPage;
    private javax.swing.JPanel departmentDetailPanel;
    private javax.swing.JComboBox<String> departmentHeadSelect;
    private javax.swing.JLabel departmentNameDetailPage;
    private javax.swing.JTextField departmentNameField;
    private javax.swing.JButton departmentsButton;
    private javax.swing.JComboBox<String> departmentsListSelect;
    private javax.swing.JPanel departmentsPanel;
    private javax.swing.JTable departmentsTable;
    private javax.swing.JTextField deptHeadDetail;
    private javax.swing.JButton editDepartmentButton;
    private javax.swing.JButton editEmployeeButton;
    private javax.swing.JPanel employeeDetailPanel;
    private javax.swing.JTextPane employeeListArea;
    private javax.swing.JButton employeesButton;
    private javax.swing.JPanel employeesPanel;
    private javax.swing.JTable employeesTable;
    private javax.swing.JButton exitButton;
    private javax.swing.JTextField firstNameDetailPage;
    private javax.swing.JTextField genderDetailPage;
    private javax.swing.JButton generateReportButton;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JTextField idDepartmentDetailPage;
    private javax.swing.JTextField idDetailPage;
    private javax.swing.JPanel imageDepartmentPanel;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField locationDetailPage;
    private javax.swing.JTextField locationField;
    private javax.swing.JButton loginButton;
    private javax.swing.JPanel logoPanel;
    private javax.swing.JPanel paginationPanel;
    private javax.swing.JPanel paginationPanel1;
    private javax.swing.JTextField payLevelDetailPage;
    private javax.swing.JPanel payrollPanel;
    private javax.swing.JButton payrollReportButton;
    private javax.swing.JPanel reportPanel;
    private javax.swing.JTextPane reportTextPane;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JTextField searchDepartmentsField;
    private javax.swing.JTextField searchEmployeesTextField;
    private javax.swing.JPanel sidemenuPanel;
    private javax.swing.JTextField surnameDetailPage;
    // End of variables declaration//GEN-END:variables
}
