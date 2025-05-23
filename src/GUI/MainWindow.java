
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JFileChooser;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.formdev.flatlaf.FlatIntelliJLaf;

/**
 *
 * Name: MainWindow Purpose: It is the primary JFrame for managing employees,
 * departments, and payroll. It handles GUI initialization, data loading, and
 * user interactions.
 *
 * @author Zainab
 * @version 1.0
 */
public class MainWindow extends javax.swing.JFrame {

    /**
     * Creates new form MainWindow
     */
    /**
     * List of all employees in the company.
     */
    public static ArrayList<Employee> allEmployees;

    /**
     * List of all departments within the organization.
     */
    public static ArrayList<Department> departments;

    /**
     * List of available pay levels/salary grades.
     */
    public static ArrayList<String> payLevels;

    /**
     * Static variable to generate unique Employee IDs.
     */
    public static int staticEmployeeID = 0;

    /**
     * Static variable to generate unique Department IDs.
     */
    public static int staticDeptID = 0;

    /**
     * Stores the currently selected Employee in the UI.
     */
    private Employee selectedEmployee;

    /**
     * Stores the currently selected Department in the UI.
     */
    private Department selectedDepartment;

    /**
     * Name: MainWindow
     *
     *
     * Purpose: Constructs the main window frame, initializes all GUI
     * components, loads initial data, and sets up listeners. Input: None
     * Output: None Effect: Initializes the frame and prepares the GUI for user
     * interaction.
     */
    public MainWindow() {

        // Set up theme for application
        try {
            FlatIntelliJLaf.setup();
            UIManager.put("Button.arc", 15);               // Rounded corners
            UIManager.put("Component.focusWidth", 2);      // Focus border thickness
            UIManager.put("Component.focusColor", new Color(0x3399FF));  // Focus border color

            // Increase padding inside buttons (top, left, bottom, right)
            UIManager.put("Button.padding", new Insets(20, 24, 20, 24));

            // Make font a bit bigger or bolder
            UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 12));

            // Change background color for buttons (use your base colors)
            UIManager.put("Button.background", new Color(21, 61, 102));
            UIManager.put("Button.foreground", Color.WHITE);

            // Shadow or border effects 
            UIManager.put("Button.borderWidth", 2);
            UIManager.put("Button.borderColor", new Color(140, 174, 201));

        } catch (Exception e) {
            e.printStackTrace();
        }
        initComponents();
        initSearchListener();
        initDepartmentSearchListener();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);

        allEmployees = new ArrayList<>();
        departments = new ArrayList<>();
        payLevels = new ArrayList<>();

        // Predefined pay levels
        payLevels.add("Select Annual Salary");
        payLevels.add("Level 1 - BHD 44,245.75");
        payLevels.add("Level 2 - BHD 48,670.32");
        payLevels.add("Level 3 - BHD 53,537.35");
        payLevels.add("Level 4 - BHD 58,891.09");
        payLevels.add("Level 5 - BHD 64,780.20");
        payLevels.add("Level 6 - BHD 71,258.22");
        payLevels.add("Level 7 - BHD 80,946.95");
        payLevels.add("Level 8 - BHD 96,336.34");

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
        MainFrame.setBackground(new Color(163, 193, 214));

        // Add panels to main frame
        MainFrame.add(LoginPanel, "login");
        MainFrame.add(DashboardPanel, "dashboard");

        // DashboardPanel
        DashboardPanel.setLayout(new BorderLayout());

        // Sidebar
        sidemenuPanel.setPreferredSize(new Dimension(200, 0));

        DashboardPanel.add(sidemenuPanel, BorderLayout.WEST);

        // Right side (header + content)
        headerPanel.setPreferredSize(new Dimension(0, 80));
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
        populateDepartmentsComboBox();
        refreshDepartmentsComboBox();

    }

    /**
     * Name: loadFromStartupFile
     *
     * @author Zainab Purpose: Loads department and employee data from the
     * "startup.txt" file. Reads the number of departments, each department’s
     * details, the number of employees per department, and each employee’s
     * details. Populates the in-memory lists of departments and employees
     * accordingly. Shows a success message dialog if loading completes without
     * errors.
     *
     * @throws FileNotFoundException if "startup.txt" file is missing.
     * @throws NumberFormatException if numeric data in the file is invalid.
     */
    private void loadFromStartupFile() {

// * Structure for reference
// * 1. Number of departments (integer)
// * 2. For each department:
// *    - Department name (string)
// *    - Department location (string)
// *    - Number of employees in the department (integer)
// *    - For each employee:
// *       * First name (string)
// *       * Surname (string)
// *       * Gender (string)
// *       * Address (string)
// *       * Pay level (integer)
//         
        File startupFile = new File("startup.txt");

        try (Scanner scanner = new Scanner(startupFile)) {

            // Read number of departments
            int numDepartments = Integer.parseInt(scanner.nextLine());

            // Read departments info
            for (int i = 0; i < numDepartments; i++) {
                String deptName = scanner.nextLine();
                String deptLocation = scanner.nextLine();
                Department department = new Department(++staticDeptID, deptName, deptLocation);
                departments.add(department);

                // Read number of employees in the department
                int numEmployees = Integer.parseInt(scanner.nextLine());

                for (int j = 0; j < numEmployees; j++) {
                    // Read employee info
                    String firstName = scanner.nextLine();
                    String surname = scanner.nextLine();
                    char gender = scanner.nextLine().charAt(0); // Stored as char
                    String address = scanner.nextLine();
                    int payLevel = Integer.parseInt(scanner.nextLine());

                    // Create employee
                    Employee employee = new Employee(++staticEmployeeID, firstName, surname, gender, address, payLevel, department.getDeptID());
                    allEmployees.add(employee);
                }
            }

            JOptionPane.showMessageDialog(this, "Data loaded successfully from startup.txt");

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "startup.txt file not found.", "File Not Found", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format in startup.txt.", "Data Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Name: loadSerializedData
     *
     * @author Zahraa Purpose: Loads the serialized department and employee data
     * from a file (e.g., "data.ser") and populates the in-memory lists of
     * departments and employees. Handles file not found, IO, and class not
     * found exceptions.
     *
     * @throws IOException if an I/O error occurs while reading the file.
     * @throws ClassNotFoundException if the serialized class definitions cannot
     * be found.
     */
    private void loadSerializedData() {
        File file = new File("HRSystem.dat");

        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "Serialized data file not found.", "Load Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) { //  try-with-resources, ensures automatic closing
            allEmployees = (ArrayList<Employee>) ois.readObject();
            departments = (ArrayList<Department>) ois.readObject();
            staticEmployeeID = (Integer) ois.readObject();
            staticDeptID = (Integer) ois.readObject();

            // Update static IDs to avoid duplicates
            updateStaticEmployeeID();
            updateStaticDeptID();

            JOptionPane.showMessageDialog(this, "Data loaded successfully from HRSystem.dat", "Load Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading serialized data: " + e.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Class not found during deserialization: " + e.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Name: getAnnualPayByLevel
     *
     * @author Zainab
     *
     * Purpose: Returns the salary string for a given pay level.
     *
     * @param payLevel The integer pay level from 1 to 8.
     * @return The corresponding salary string or "Not Available" if invalid.
     */
    private String getAnnualPayByLevel(int payLevel) {
        switch (payLevel) {
            case 1:
                return "BHD 44,245.75";
            case 2:
                return "BHD 48,670.32";
            case 3:
                return "BHD 53,537.35";
            case 4:
                return "BHD 58,891.09";
            case 5:
                return "BHD 64,780.20";
            case 6:
                return "BHD 71,258.22";
            case 7:
                return "BHD 80,946.95";
            case 8:
                return "BHD 96,336.34";
            default:
                return "Not Available"; // If pay level is out of range
        }
    }

    /**
     * Name: initialiseEmployeesTable
     *
     * @author Zainab
     *
     * Purpose: Populates the employees table with current employee data by
     * iterating through the list of employees and adding rows to the table
     * model. Handles potential exceptions related to null data, invalid pay
     * levels, and invalid gender values.
     *
     * @throws NullPointerException if any employee data or department
     * information is unexpectedly null.
     * @throws ClassCastException if the gender value is not of the expected
     * type.
     * @throws NumberFormatException if the pay level value cannot be parsed as
     * an integer.
     */
    private void initialiseEmployeesTable() {
        String[] columnNames = {"ID", "Full Name", "Department", "Gender", "Annual Pay"};

        // Create table with model
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { // Make cell uneditable
                return false;
            }
        };

        try {
            // Populate the table with employee data
            for (Employee emp : allEmployees) {
                try {
                    int id = emp.getEmployeeId();
                    String fullName = emp.getFirstName() + " " + emp.getSurname();

                    String department = "No Department";
                    Integer deptID = emp.getDeptID();
                    if (deptID != null) {
                        try {
                            department = Department.getDepartmentNameById(departments, deptID);
                        } catch (NullPointerException e) {
                            JOptionPane.showMessageDialog(this, "Departments list is null. Cannot resolve department for employee ID: " + id, "Department Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    char gender = 'U';  // Default to 'U' for undefined
                    try {
                        gender = emp.getGender();
                    } catch (ClassCastException e) {
                        JOptionPane.showMessageDialog(this, "Invalid gender value for employee ID: " + id + ". Defaulting to 'U'.", "Gender Error", JOptionPane.WARNING_MESSAGE);
                    }

                    int payLevel = 0;
                    try {
                        payLevel = emp.getPayLevel();
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Invalid pay level for employee ID: " + id + ". Defaulting to 0.", "Pay Level Error", JOptionPane.WARNING_MESSAGE);
                    }

                    // Get annual pay from pay level
                    String annualPay = getAnnualPayByLevel(payLevel);

                    // Add the row to the model
                    Object[] row = {id, fullName, department, gender, annualPay};
                    model.addRow(row);

                } catch (NullPointerException e) {
                    JOptionPane.showMessageDialog(this, "Null data encountered for an employee. Skipping entry.", "Data Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error initializing employee table: " + e.getMessage(), "Initialization Error", JOptionPane.ERROR_MESSAGE);
        }

        // Set model to employees table
        employeesTable.setModel(model);
        refreshEmployeeTable();
    }

    /**
     * Name: initialiseDepartmentsTable
     *
     * @author Zainab Purpose: Populates the departments table with current
     * department data, including department head information. Iterates through
     * the list of departments and updates the table model with department
     * details.
     *
     * @throws NullPointerException if the departments list or department head
     * data is null.
     * @throws ClassCastException if the department head is not of the expected
     * type.
     * @throws Exception if an error occurs while accessing the department data.
     */
    private void initialiseDepartmentsTable() {
        String[] departmentColumnNames = {"ID", "Name", "Location", "Department Head"};

        // Create the table model for departments
        DefaultTableModel deptModel = new DefaultTableModel(departmentColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try {
            for (Department dept : departments) {
                try {
                    Integer deptID = dept.getDeptID();
                    String name = dept.getName();
                    String location = dept.getLocation();

                    String departmentHead = "No Head";
                    try {
                        if (dept.getDepartmentHead() != null) {
                            departmentHead = dept.getDepartmentHead().getFirstName() + " " + dept.getDepartmentHead().getSurname();
                        }
                    } catch (NullPointerException e) {
                        JOptionPane.showMessageDialog(this, "Null department head data for department ID: " + deptID, "Data Error", JOptionPane.ERROR_MESSAGE);
                    } catch (ClassCastException e) {
                        JOptionPane.showMessageDialog(this, "Invalid data type for department head in department ID: " + deptID, "Data Type Error", JOptionPane.ERROR_MESSAGE);
                    }

                    deptModel.addRow(new Object[]{deptID, name, location, departmentHead});

                } catch (NullPointerException e) {
                    JOptionPane.showMessageDialog(this, "Null data encountered for a department. Skipping entry.", "Data Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error initializing department table: " + e.getMessage(), "Initialization Error", JOptionPane.ERROR_MESSAGE);
        }

        departmentsTable.setModel(deptModel);
        refreshDepartmentTable();
    }

    /**
     * Name: showEmployeeDetails
     *
     * @author Zahraa
     *
     * Purpose: Populates and displays the employee detail view with data from
     * the given employee object. Updates the UI to show the selected employee's
     * information.
     *
     * @param employee The employee object containing the details to display.
     * @throws NullPointerException if the employee object or any of its
     * attributes are null.
     * @throws ClassCastException if the gender value is not of the expected
     * type.
     * @throws NumberFormatException if the pay level value cannot be converted
     * to a string.
     */
    public void showEmployeeDetails(Employee employee) {

        if (employee == null) {
            JOptionPane.showMessageDialog(this, "No employee selected.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        selectedEmployee = employee;

        // Show employee detail page
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "employeeDetail");

        try {
            // Display data
            idDetailPage.setText(Integer.toString(employee.getEmployeeId()));

            try {
                firstNameDetailPage.setText(employee.getFirstName());
                surnameDetailPage.setText(employee.getSurname());
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(this, "Employee name information is missing.", "Data Error", JOptionPane.WARNING_MESSAGE);
            }

            try {
                char gender = employee.getGender();
                genderDetailPage.setText(gender == 'M' ? "Male" : "Female");
            } catch (ClassCastException e) {
                JOptionPane.showMessageDialog(this, "Invalid gender value for employee ID: " + employee.getEmployeeId(), "Data Type Error", JOptionPane.WARNING_MESSAGE);
            }

            try {
                int payLevel = employee.getPayLevel();
                String payLevelText = "Level " + payLevel + " " + getAnnualPayByLevel(payLevel);
                payLevelDetailPage.setText(payLevelText);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid pay level value for employee ID: " + employee.getEmployeeId(), "Data Error", JOptionPane.WARNING_MESSAGE);
            }

            try {
                addressDetailPage.setText(employee.getAddress());
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(this, "Employee address is missing for employee ID: " + employee.getEmployeeId(), "Data Error", JOptionPane.WARNING_MESSAGE);
            }

            try {
                String department = "No Department";
                if (employee.getDeptID() != null) {
                    department = Department.getDepartmentNameById(departments, employee.getDeptID());
                }
                departmentDetailPage.setText(department);
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(this, "Error retrieving department information for employee ID: " + employee.getEmployeeId(), "Data Error", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error displaying employee details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Name: deleteEmployee
     *
     * @author Raghad Purpose: Removes a specified employee from the
     * allEmployees list and updates the employee table display.
     *
     * @param employee The Employee object to be removed from the list.
     */
    private void deleteEmployee(Employee employee) {
        allEmployees.remove(employee);
        refreshEmployeeTable();
    }

    /**
     * /** Name: deleteDepartment
     *
     * @author Raghad Purpose/description: Deletes the given department from the
     * list and refreshes related UI components.
     * @param department - the department object to be removed.
     * @return void - this method does not return any value.
     */
    private void deleteDepartment(Department department) {
        departments.remove(department);
        refreshDepartmentsComboBox();
    }

    /**
     * Name: showDepartmentDetails Purpose: To populate and display the
     * department detail view with data from the given department object.
     *
     * @author Zahraa
     *
     * Input: Department dept - the department whose details are to be
     * displayed.
     *
     * Output: None. Effect: Updates the UI to show the selected department's
     * information including its head and employees.
     *
     * @param dept The department object containing the details to display.
     */
    public void showDepartmentDetails(Department dept) {
        selectedDepartment = dept;

        if (dept == null) {
            JOptionPane.showMessageDialog(this, "No department selected.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Show department detail page
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "departmentDetail");

        try {
            // Display department data
            departmentNameDetailPage.setText(dept.getName());
            idDepartmentDetailPage.setText(Integer.toString(dept.getDeptID()));
            locationDetailPage.setText(dept.getLocation());
            deptHeadDetail.setText(null);

            // Display department head, if applicable
            // Check if the selected department is not null before accessing it
            if (selectedDepartment != null) {
                Employee headEmployee = selectedDepartment.getDepartmentHead();

                // Display department head, if applicable
                if (headEmployee != null) {
                    deptHeadDetail.setText(headEmployee.getFirstName() + " " + headEmployee.getSurname());
                } else {
                    deptHeadDetail.setText(null);
                }
            } else {
                deptHeadDetail.setText(null);
            }
            // Display employees in the department
            StringBuilder employeeDetails = new StringBuilder();
            boolean hasEmployees = false;
            int counter = 0;

            for (Employee employee : allEmployees) {
                if (employee.getDeptID() != null && employee.getDeptID() == dept.getDeptID()) {
                    hasEmployees = true;
                    int payLevel = employee.getPayLevel();
                    String annualPay = getAnnualPayByLevel(payLevel);
                    counter++;

                    employeeDetails.append("Employee " + counter).append("\nID: ").append(employee.getEmployeeId())
                            .append("\nName: ").append(employee.getFirstName())
                            .append(" ").append(employee.getSurname());

                    if (employee.isIsHead()) {
                        employeeDetails.append(" (Head of Department)");
                    }

                    employeeDetails.append("\nAnnual Pay: ").append(annualPay)
                            .append("\n\n");

                }
            }

            if (!hasEmployees) {
                employeeDetails.append("No employees in this department.");
            }

            employeeListArea.setText(employeeDetails.toString());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error displaying department details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Name: populateDepartmentsComboBox
     *
     * @author Maryam Purpose: To update the department dropdown list with
     * current department names. Input: None. Output: None. Effect: Clears and
     * repopulates the combo box with department names plus a default "All
     * Departments" option.
     */
    public void populateDepartmentsComboBox() {
        departmentsListSelect.removeAllItems();
        departmentsListSelect.addItem("All Departments");
        for (Department dept : departments) {
            departmentsListSelect.addItem(dept.getName());
        }
    }

    /**
     * Name: refreshEmployeeTable
     *
     * @author Hajar Purpose/description: Refreshes the employee table based on
     * the selected department in the combo box. Input: none Output: none
     * Effect: Clears the employee table and repopulates it with employees
     * filtered by selected department, displaying their annual pay.
     *
     */
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
            String fullName = emp.getFirstName() + " " + emp.getSurname();
            Integer deptId = emp.getDeptID();
            String department = "No Department";

            if (deptId != null) {
                department = Department.getDepartmentNameById(departments, deptId);
            }

            // Filter employees based on the selected department
            if (selectedDepartment.equals("All Departments") || department.equals(selectedDepartment)) {
                char gender = emp.getGender();
                int payLevel = emp.getPayLevel();

                // Get the annual pay based on the pay level
                String annualPay = getAnnualPayByLevel(payLevel);

                // Add the row to the model
                Object[] row = {id, fullName, department, gender, annualPay};
                model.addRow(row);
            }
        }
    }

    /**
     * Name: refreshDepartmentTable
     *
     * @author Hajar Purpose/description: Refreshes the departments table with
     * the current list of departments. Input: none Output: none Effect: Clears
     * the departments table and repopulates it with the latest department data
     * including head info.
     *
     */
    public void refreshDepartmentTable() {
        // Assuming you have a JTable named departmentTable
        DefaultTableModel model = (DefaultTableModel) departmentsTable.getModel();

        // Clear existing rows
        model.setRowCount(0);

        // Populate table
        for (Department dept : departments) {
            // Add department data as rows
            model.addRow(new Object[]{
                dept.getDeptID(),
                dept.getName(),
                dept.getLocation(),
                dept.getDepartmentHead() != null ? dept.getDepartmentHead().getFirstName() + " " + dept.getDepartmentHead().getSurname() : "No Head"
            });
        }
    }

    /**
     * Name: refreshDepartmentsComboBox Purpose/description: Refreshes the
     * departments combo box with updated department names. Input: none Output:
     * none Effect: Clears and repopulates the combo box, adding "All
     * Departments" option and refreshing the employee table afterwards.
     *
     */
    public void refreshDepartmentsComboBox() {
        departmentsListSelect.removeAllItems();  // Clear all existing items

        // Add "All Departments" option at the top
        departmentsListSelect.addItem("All Departments");

        // Now re-populate the combo box with the updated list of departments
        for (Department dept : departments) {
            departmentsListSelect.addItem(dept.getName());  // Add department names
        }

        refreshEmployeeTable();
    }

    /**
     * Name: calculateBiweeklyPay
     *
     * @author Hajar Purpose/description: Calculates the biweekly pay amount
     * based on the pay level. Input: payLevel - the pay level of the employee.
     * Output: biweekly pay amount as double. Effect: Returns the biweekly
     * salary calculated from predefined annual salaries.
     *
     * @param payLevel - integer representing employee's pay level.
     * @return double - calculated biweekly pay amount.
     */
    private double calculateBiweeklyPay(int payLevel) {
        // Pay levels and their corresponding annual salaries
        double[] payLevelSalaries = {
            0.0, // Level 0 (placeholder)
            45000.0, // Level 1
            54000.0, // Level 2
            63000.0, // Level 3
            72000.0, // Level 4
            81000.0, // Level 5
            71258.22, // Level 6
            80946.95, // Level 7
            96336.34 // Level 8
        };

        // Validate pay level
        if (payLevel < 1 || payLevel >= payLevelSalaries.length) {
            return 0.0; // Default to 0 if pay level is invalid
        }

        // Calculate biweekly pay (1/26th of annual salary)
        return payLevelSalaries[payLevel] / 26.0;
    }

    /**
     * Name: initSearchListener
     *
     * @author Maryam Purpose: Initializes a document listener on the employee
     * search text field to enable real-time search functionality as the user
     * types characters. Input: None Output: None Effect: Attaches listeners to
     * the text field that trigger employee search logic
     */
    private void initSearchListener() {
//     Add a document listener to the search field to track typing events
        searchEmployeesTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchEmployee();  // Called when text is inserted
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchEmployee();  // Called when text is removed
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchEmployee();  // Called for other changes (e.g., formatting)
            }
        });
    }

    /**
     * Name: searchEmployee
     *
     * @author Hajar & Maryam Purpose: Filters the employee list based on user
     * input in the search field and updates the display table. Input: None
     * Output: None Effect: Shows matching employees in the table based on first
     * name or surname prefix.
     *
     *
     */
    private void searchEmployee() {
        String query = searchEmployeesTextField.getText().trim().toLowerCase();
        ArrayList<Employee> searchResults = new ArrayList<>();

        if (query.isEmpty()) {
            // Show all employees if the search field is empty
            searchResults.addAll(allEmployees);
        } else {
            // First try to search by ID
            try {
                int employeeId = Integer.parseInt(query);
                for (Employee emp : allEmployees) {
                    if (emp.getEmployeeId() == employeeId) {
                        searchResults.add(emp);
                        break; // Stop searching once the ID match is found
                    }
                }
            } catch (NumberFormatException e) {
                // If not a number, search by name
                // Split the query by space to handle first name + surname search
                String[] queryParts = query.split("\\s+");
                boolean isFullNameQuery = queryParts.length == 2;

                for (Employee employee : allEmployees) {
                    String firstName = employee.getFirstName().toLowerCase();
                    String surname = employee.getSurname().toLowerCase();
                    String fullName = firstName + " " + surname;

                    if (isFullNameQuery) {
                        // Full name search
                        if (fullName.startsWith(query)) {
                            searchResults.add(employee);
                        }
                    } else {
                        // Single word search (either first name or surname)
                        if (firstName.startsWith(query) || surname.startsWith(query)) {
                            searchResults.add(employee);
                        }
                    }
                }
            }
        }

        // Clear the current rows
        DefaultTableModel model = (DefaultTableModel) employeesTable.getModel();
        model.setRowCount(0);  // Clears the table

        // Populate the table with the filtered search results
        for (Employee emp : searchResults) {
            // Combine first name and surname to create Full Name
            String fullName = emp.getFirstName().trim() + " " + emp.getSurname().trim();
            // Handle case where deptID might be null
            String departmentName = (emp.getDeptID() != null)
                    ? Department.getDepartmentNameById(departments, emp.getDeptID()) : "No Department";

            // Add the employee details to the table
            model.addRow(new Object[]{
                emp.getEmployeeId(), // Assuming you have employee ID
                fullName, // Full Name (First + Surname)
                departmentName, // Department
                emp.getGender(), // Gender
                getAnnualPayByLevel(emp.getPayLevel()) // Pay Level (Annual Salary)
            });
        }
    }

    /**
     * Name: initDepartmentSearchListener
     *
     * @author Maryam Purpose: Initializes a document listener on the department
     * search text field to enable real-time search functionality as the user
     * types characters. Input: None Output: None Effect: Attaches listeners to
     * the text field that trigger department search logic
     */
    private void initDepartmentSearchListener() {
        //     Add a document listener to the search field to track typing events
        searchDepartmentsField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchDepartment();// Called when text is inserted
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchDepartment(); // Called when text is removed
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchDepartment();// Called for other changes (e.g., formatting)
            }
        });
    }

    /**
     * Name: searchDepartments
     *
     * @author Hajar & Maryam
     *
     * Purpose: Filters the department list based on user input in the search
     * field and updates the display table. Input: None Output: None Effect:
     * Displays departments whose ID, name, location, or department head's name
     * matches the user's query (case-insensitive).
     *
     * @author:
     */
    private void searchDepartment() {
        String query = searchDepartmentsField.getText().trim().toLowerCase();
        ArrayList<Department> searchResults = new ArrayList<>();

        if (query.isEmpty()) {
            // If the search query is empty, show all departments
            searchResults.addAll(departments);
        } else {
            // First try to search by ID
            try {
                int deptId = Integer.parseInt(query);
                for (Department dept : departments) {
                    if (dept.getDeptID() == deptId) {
                        searchResults.add(dept);
                        break; // Stop searching once the ID match is found
                    }
                }
            } catch (NumberFormatException e) {
                // If not a number, search by name
                for (Department dept : departments) {
                    String deptName = dept.getName().toLowerCase();
                    if (deptName.startsWith(query)) {
                        searchResults.add(dept);
                    }
                }
            }
        }

        // Get the table model of the departments table to modify its data
        DefaultTableModel model = (DefaultTableModel) departmentsTable.getModel();

        // Clear all current rows from the table before adding new filtered results
        model.setRowCount(0);

        // Add each department in the filtered results list to the table
        for (Department dept : searchResults) {
            Employee head = dept.getDepartmentHead(); // Get the department head, if any
            String headName = (head != null) ? head.getFirstName() + " " + head.getSurname() : "No Head";

            // Add the employee details to the table
            model.addRow(new Object[]{
                dept.getDeptID(), //Assuming you have department ID
                dept.getName(), // Departmnet name
                dept.getLocation(), // Departmnet Location
                headName //Departmnet head
            });
        }
    }

    /**
     * Name: generatePayReportButtonActionPerformed Purpose: Stub for future
     * implementation of pay report generation.
     *
     * @param evt The ActionEvent triggered by clicking the Generate Pay Report
     * button.
     */
    private void generatePayReportButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // Check if there are any employees

    }

    /**
     * Name: updateStaticEmployeeID
     *
     * @author Zainab
     *
     * Purpose: Updates the staticEmployeeID to the highest employee ID present
     * in the allEmployees list. Input: None Output: None Effect: Ensures that
     * new Employee objects will receive unique IDs greater than any existing
     * employee.
     */
    private void updateStaticEmployeeID() {
        int maxID = 0;
        for (Employee emp : allEmployees) {
            if (emp.getEmployeeId() > maxID) {
                maxID = emp.getEmployeeId();
            }
        }
        staticEmployeeID = maxID;
    }

    /**
     * Name: updateStaticDeptID
     *
     * @author Zainab
     *
     * Purpose: Updates the staticDeptID to the highest department ID present in
     * the departments list. Input: None Output: None Effect: Ensures that new
     * Department objects will receive unique IDs greater than any existing
     * department.
     */
    private void updateStaticDeptID() {
        int maxID = 0;
        for (Department dept : departments) {
            if (dept.getDeptID() > maxID) {
                maxID = dept.getDeptID();
            }
        }
        staticDeptID = maxID;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainFrame = new javax.swing.JPanel();
        DashboardPanel = new javax.swing.JPanel();
        sidemenuPanel = new javax.swing.JPanel();
        departmentsButton = new javax.swing.JButton();
        payrollReportButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        employeesButton = new javax.swing.JButton();
        rightPanel = new javax.swing.JPanel();
        contentPanel = new javax.swing.JPanel();
        employeesPanel = new javax.swing.JPanel();
        searchEmployeesTextField = new javax.swing.JTextField();
        jScrollPane = new javax.swing.JScrollPane();
        employeesTable = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        employeeDetailPanel = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
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
        idDetailPage = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        backToEmployeesButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        editEmployeeButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        departmentsListSelect = new javax.swing.JComboBox<>();
        addEmployeeButton = new javax.swing.JButton();
        departmentsPanel = new javax.swing.JPanel();
        searchDepartmentsField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        departmentsTable = new javax.swing.JTable();
        addDepartmentButton = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        departmentDetailPanel = new javax.swing.JPanel();
        backToDepartmentsButton = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        editDepartmentButton = new javax.swing.JButton();
        deleteButton1 = new javax.swing.JButton();
        departmentNameDetailPage = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        idDepartmentDetailPage = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        locationDetailPage = new javax.swing.JTextField();
        deptHeadDetail = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        employeeListArea = new javax.swing.JTextPane();
        payrollPanel = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        generateReportButton = new javax.swing.JButton();
        reportPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        reportTextPane = new javax.swing.JTextPane();
        headerPanel = new javax.swing.JPanel();
        lblUserName = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        LoginPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtUserName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        loginButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        deleteAllData = new javax.swing.JButton();
        txtPassword = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(250, 244, 222));
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));

        MainFrame.setBackground(new java.awt.Color(250, 244, 222));
        MainFrame.setPreferredSize(new java.awt.Dimension(1380, 1000));
        MainFrame.setLayout(new java.awt.CardLayout());

        DashboardPanel.setBackground(new java.awt.Color(255, 255, 255));

        sidemenuPanel.setBackground(new java.awt.Color(0, 51, 102));

        departmentsButton.setBackground(new java.awt.Color(0, 51, 102));
        departmentsButton.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        departmentsButton.setForeground(new java.awt.Color(255, 255, 255));
        departmentsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/departments.png"))); // NOI18N
        departmentsButton.setText("Departments");
        departmentsButton.setBorderPainted(false);
        departmentsButton.setFocusPainted(false);
        departmentsButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        departmentsButton.setMaximumSize(new java.awt.Dimension(159, 60));
        departmentsButton.setMinimumSize(new java.awt.Dimension(159, 60));
        departmentsButton.setOpaque(true);
        departmentsButton.setPreferredSize(new java.awt.Dimension(177, 63));
        departmentsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                departmentsButtonActionPerformed(evt);
            }
        });

        payrollReportButton.setBackground(new java.awt.Color(0, 51, 102));
        payrollReportButton.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        payrollReportButton.setForeground(new java.awt.Color(255, 255, 255));
        payrollReportButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/payroll.png"))); // NOI18N
        payrollReportButton.setText("<html><div style='text-align:center;'>Payroll<br>Report</div></html>");
        payrollReportButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 23, 0, 0));
        payrollReportButton.setBorderPainted(false);
        payrollReportButton.setFocusPainted(false);
        payrollReportButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        payrollReportButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        payrollReportButton.setIconTextGap(15);
        payrollReportButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        payrollReportButton.setMaximumSize(new java.awt.Dimension(159, 60));
        payrollReportButton.setMinimumSize(new java.awt.Dimension(120, 60));
        payrollReportButton.setOpaque(true);
        payrollReportButton.setPreferredSize(new java.awt.Dimension(159, 60));
        payrollReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payrollReportButtonActionPerformed(evt);
            }
        });

        exitButton.setBackground(new java.awt.Color(102, 102, 102));
        exitButton.setForeground(new java.awt.Color(255, 255, 255));
        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        employeesButton.setBackground(new java.awt.Color(0, 51, 102));
        employeesButton.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        employeesButton.setForeground(new java.awt.Color(255, 255, 255));
        employeesButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/employees.png"))); // NOI18N
        employeesButton.setText("Employees");
        employeesButton.setToolTipText("");
        employeesButton.setBorder(null);
        employeesButton.setBorderPainted(false);
        employeesButton.setFocusPainted(false);
        employeesButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        employeesButton.setOpaque(true);
        employeesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeesButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout sidemenuPanelLayout = new javax.swing.GroupLayout(sidemenuPanel);
        sidemenuPanel.setLayout(sidemenuPanelLayout);
        sidemenuPanelLayout.setHorizontalGroup(
            sidemenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sidemenuPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(sidemenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(employeesButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(departmentsButton, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)))
            .addGroup(sidemenuPanelLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(exitButton)
                .addContainerGap(100, Short.MAX_VALUE))
            .addComponent(payrollReportButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        sidemenuPanelLayout.setVerticalGroup(
            sidemenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidemenuPanelLayout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(employeesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(departmentsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(payrollReportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(381, 381, 381)
                .addComponent(exitButton)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        contentPanel.setForeground(new java.awt.Color(250, 244, 222));
        contentPanel.setLayout(new java.awt.CardLayout());

        employeesPanel.setBackground(new java.awt.Color(140, 174, 201));
        employeesPanel.setForeground(new java.awt.Color(250, 244, 222));

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

        employeesTable.setBackground(new java.awt.Color(0, 51, 102));
        employeesTable.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        employeesTable.setForeground(new java.awt.Color(255, 255, 255));
        employeesTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        employeesTable.setGridColor(new java.awt.Color(50, 80, 130));
        employeesTable.setRowHeight(28);
        employeesTable.setSelectionBackground(new java.awt.Color(80, 130, 180));
        employeesTable.setSelectionForeground(new java.awt.Color(255, 255, 255));
        employeesTable.setShowGrid(true);
        employeesTable.setShowVerticalLines(false);
        employeesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                employeesTableMouseClicked(evt);
            }
        });
        jScrollPane.setViewportView(employeesTable);

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 51, 102));
        jLabel12.setText("Employees");

        jLabel11.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N

        employeeDetailPanel.setBackground(new java.awt.Color(140, 174, 201));

        jLabel17.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel17.setText("Employee Detail");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(51, 51, 51));
        jPanel1.setPreferredSize(new java.awt.Dimension(520, 400));

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
        payLevelDetailPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payLevelDetailPageActionPerformed(evt);
            }
        });

        jLabel32.setText("Address");

        addressDetailPage.setEditable(false);
        addressDetailPage.setColumns(20);
        addressDetailPage.setRows(5);
        jScrollPane2.setViewportView(addressDetailPage);

        idDetailPage.setEditable(false);
        idDetailPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idDetailPageActionPerformed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel33.setText("ID");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel32)
                    .addComponent(jLabel29)
                    .addComponent(jLabel33)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(payLevelDetailPage, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(genderDetailPage, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(firstNameDetailPage, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                                            .addComponent(idDetailPage, javax.swing.GroupLayout.Alignment.LEADING))
                                        .addComponent(jLabel27)))
                                .addComponent(jLabel28)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel31)
                            .addComponent(jLabel30)
                            .addComponent(surnameDetailPage, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                            .addComponent(departmentDetailPage)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(idDetailPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addGap(18, 18, 18)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(payLevelDetailPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel32)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(141, Short.MAX_VALUE))
        );

        backToEmployeesButton.setBackground(new java.awt.Color(102, 102, 102));
        backToEmployeesButton.setForeground(new java.awt.Color(255, 255, 255));
        backToEmployeesButton.setText("< Back to list");
        backToEmployeesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backToEmployeesButtonActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 51, 102));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Helvetica Neue", 2, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Employee Information");

        editEmployeeButton.setBackground(new java.awt.Color(0, 51, 102));
        editEmployeeButton.setForeground(new java.awt.Color(255, 255, 255));
        editEmployeeButton.setText("Edit");
        editEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editEmployeeButtonActionPerformed(evt);
            }
        });

        deleteButton.setBackground(new java.awt.Color(102, 0, 0));
        deleteButton.setForeground(new java.awt.Color(255, 255, 255));
        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/employeeDetail.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
                .addComponent(editEmployeeButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deleteButton)
                .addGap(32, 32, 32))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(editEmployeeButton)
                            .addComponent(deleteButton))))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout employeeDetailPanelLayout = new javax.swing.GroupLayout(employeeDetailPanel);
        employeeDetailPanel.setLayout(employeeDetailPanelLayout);
        employeeDetailPanelLayout.setHorizontalGroup(
            employeeDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeDetailPanelLayout.createSequentialGroup()
                .addGroup(employeeDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(employeeDetailPanelLayout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addGroup(employeeDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(employeeDetailPanelLayout.createSequentialGroup()
                        .addGap(106, 106, 106)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(backToEmployeesButton)))
                .addContainerGap(158, Short.MAX_VALUE))
        );
        employeeDetailPanelLayout.setVerticalGroup(
            employeeDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeDetailPanelLayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addGroup(employeeDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(backToEmployeesButton))
                .addGap(30, 30, 30)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(51, Short.MAX_VALUE))
        );

        departmentsListSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Departments", "Item 2", "Item 3", "Item 4" }));
        departmentsListSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                departmentsListSelectActionPerformed(evt);
            }
        });

        addEmployeeButton.setBackground(new java.awt.Color(0, 51, 102));
        addEmployeeButton.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        addEmployeeButton.setForeground(new java.awt.Color(255, 255, 255));
        addEmployeeButton.setText("+ Add Employee");
        addEmployeeButton.setBorder(null);
        addEmployeeButton.setBorderPainted(false);
        addEmployeeButton.setFocusPainted(false);
        addEmployeeButton.setOpaque(true);
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
                .addGap(31, 31, 31)
                .addGroup(employeesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addGroup(employeesPanelLayout.createSequentialGroup()
                        .addGroup(employeesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(employeesPanelLayout.createSequentialGroup()
                                .addComponent(searchEmployeesTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(departmentsListSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 792, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addEmployeeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(employeeDetailPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(1388, Short.MAX_VALUE))
            .addGroup(employeesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(employeesPanelLayout.createSequentialGroup()
                    .addGap(477, 477, 477)
                    .addComponent(jLabel11)
                    .addContainerGap(2513, Short.MAX_VALUE)))
        );
        employeesPanelLayout.setVerticalGroup(
            employeesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeesPanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(employeesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(employeesPanelLayout.createSequentialGroup()
                        .addGroup(employeesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(employeesPanelLayout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(searchEmployeesTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(employeesPanelLayout.createSequentialGroup()
                                .addComponent(addEmployeeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(departmentsListSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(employeeDetailPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(764, 764, 764))
            .addGroup(employeesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(employeesPanelLayout.createSequentialGroup()
                    .addGap(254, 254, 254)
                    .addComponent(jLabel11)
                    .addContainerGap(1357, Short.MAX_VALUE)))
        );

        contentPanel.add(employeesPanel, "card4");

        departmentsPanel.setBackground(new java.awt.Color(140, 174, 201));

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

        departmentsTable.setBackground(new java.awt.Color(0, 51, 102));
        departmentsTable.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        departmentsTable.setForeground(new java.awt.Color(255, 255, 255));
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
        departmentsTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        departmentsTable.setGridColor(new java.awt.Color(50, 80, 130));
        departmentsTable.setRowHeight(28);
        departmentsTable.setSelectionBackground(new java.awt.Color(80, 130, 180));
        departmentsTable.setSelectionForeground(new java.awt.Color(255, 255, 255));
        departmentsTable.setShowGrid(true);
        departmentsTable.setShowVerticalLines(false);
        departmentsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                departmentsTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(departmentsTable);
        if (departmentsTable.getColumnModel().getColumnCount() > 0) {
            departmentsTable.getColumnModel().getColumn(3).setResizable(false);
        }

        addDepartmentButton.setBackground(new java.awt.Color(0, 51, 102));
        addDepartmentButton.setForeground(new java.awt.Color(255, 255, 255));
        addDepartmentButton.setText("+ Add Department");
        addDepartmentButton.setBorder(null);
        addDepartmentButton.setBorderPainted(false);
        addDepartmentButton.setFocusPainted(false);
        addDepartmentButton.setMaximumSize(new java.awt.Dimension(104, 18));
        addDepartmentButton.setMinimumSize(new java.awt.Dimension(104, 18));
        addDepartmentButton.setOpaque(true);
        addDepartmentButton.setPreferredSize(new java.awt.Dimension(104, 18));
        addDepartmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDepartmentButtonActionPerformed(evt);
            }
        });

        jLabel13.setBackground(new java.awt.Color(0, 51, 102));
        jLabel13.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 51, 102));
        jLabel13.setText("Departments");

        jLabel14.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N

        departmentDetailPanel.setBackground(new java.awt.Color(140, 174, 201));
        departmentDetailPanel.setForeground(new java.awt.Color(140, 174, 201));

        backToDepartmentsButton.setBackground(new java.awt.Color(102, 102, 102));
        backToDepartmentsButton.setForeground(new java.awt.Color(255, 255, 255));
        backToDepartmentsButton.setText("< Back to list");
        backToDepartmentsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backToDepartmentsButtonActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel19.setText("Department Detail");

        jPanel3.setBackground(new java.awt.Color(0, 51, 102));
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));

        editDepartmentButton.setBackground(new java.awt.Color(0, 51, 102));
        editDepartmentButton.setForeground(new java.awt.Color(255, 255, 255));
        editDepartmentButton.setText("Edit");
        editDepartmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editDepartmentButtonActionPerformed(evt);
            }
        });

        deleteButton1.setBackground(new java.awt.Color(102, 0, 0));
        deleteButton1.setForeground(new java.awt.Color(255, 255, 255));
        deleteButton1.setText("Delete");
        deleteButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButton1ActionPerformed(evt);
            }
        });

        departmentNameDetailPage.setBackground(new java.awt.Color(255, 255, 255));
        departmentNameDetailPage.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        departmentNameDetailPage.setForeground(new java.awt.Color(255, 255, 255));
        departmentNameDetailPage.setText("Department Name ");

        jLabel34.setBackground(new java.awt.Color(255, 255, 255));
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("ID:");

        idDepartmentDetailPage.setEditable(false);
        idDepartmentDetailPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idDepartmentDetailPageActionPerformed(evt);
            }
        });

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/departmentDetail.png"))); // NOI18N

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setForeground(new java.awt.Color(51, 51, 51));

        jLabel35.setText("Location");

        jLabel38.setText("Department Head");

        locationDetailPage.setEditable(false);
        locationDetailPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locationDetailPageActionPerformed(evt);
            }
        });

        deptHeadDetail.setEditable(false);

        jLabel18.setText("Employees in Department");

        employeeListArea.setEditable(false);
        jScrollPane3.setViewportView(employeeListArea);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3)
                    .addComponent(jLabel18)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel35)
                            .addComponent(locationDetailPage, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(61, 61, 61)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel38)
                            .addComponent(deptHeadDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(jLabel38))
                .addGap(8, 8, 8)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(locationDetailPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deptHeadDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(56, 56, 56)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(idDepartmentDetailPage, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(departmentNameDetailPage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(editDepartmentButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteButton1)
                        .addGap(15, 15, 15))))
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(editDepartmentButton)
                            .addComponent(deleteButton1)
                            .addComponent(departmentNameDetailPage))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(idDepartmentDetailPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout departmentDetailPanelLayout = new javax.swing.GroupLayout(departmentDetailPanel);
        departmentDetailPanel.setLayout(departmentDetailPanelLayout);
        departmentDetailPanelLayout.setHorizontalGroup(
            departmentDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(departmentDetailPanelLayout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addGroup(departmentDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(departmentDetailPanelLayout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addGap(187, 187, 187)
                        .addComponent(backToDepartmentsButton)
                        .addGap(19, 19, 19)))
                .addContainerGap(67, Short.MAX_VALUE))
        );
        departmentDetailPanelLayout.setVerticalGroup(
            departmentDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(departmentDetailPanelLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(departmentDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(backToDepartmentsButton))
                .addGap(46, 46, 46)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(79, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout departmentsPanelLayout = new javax.swing.GroupLayout(departmentsPanel);
        departmentsPanel.setLayout(departmentsPanelLayout);
        departmentsPanelLayout.setHorizontalGroup(
            departmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(departmentsPanelLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(departmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addDepartmentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(departmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel13)
                        .addComponent(searchDepartmentsField, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, departmentsPanelLayout.createSequentialGroup()
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 792, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(departmentDetailPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(1587, Short.MAX_VALUE))
            .addGroup(departmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(departmentsPanelLayout.createSequentialGroup()
                    .addGap(477, 477, 477)
                    .addComponent(jLabel14)
                    .addContainerGap(2541, Short.MAX_VALUE)))
        );
        departmentsPanelLayout.setVerticalGroup(
            departmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(departmentsPanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel13)
                .addGap(10, 10, 10)
                .addComponent(addDepartmentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(searchDepartmentsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(departmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(departmentsPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(departmentsPanelLayout.createSequentialGroup()
                        .addGap(121, 121, 121)
                        .addComponent(departmentDetailPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(342, 342, 342))
            .addGroup(departmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(departmentsPanelLayout.createSequentialGroup()
                    .addGap(254, 254, 254)
                    .addComponent(jLabel14)
                    .addContainerGap(1357, Short.MAX_VALUE)))
        );

        contentPanel.add(departmentsPanel, "card4");

        payrollPanel.setBackground(new java.awt.Color(140, 174, 201));
        payrollPanel.setForeground(new java.awt.Color(140, 174, 201));

        jLabel36.setBackground(new java.awt.Color(0, 51, 102));
        jLabel36.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(0, 51, 102));
        jLabel36.setText("Payroll Report");

        generateReportButton.setBackground(new java.awt.Color(0, 51, 102));
        generateReportButton.setForeground(new java.awt.Color(255, 255, 255));
        generateReportButton.setText("Generate Report");
        generateReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateReportButtonActionPerformed(evt);
            }
        });

        reportPanel.setBackground(new java.awt.Color(255, 255, 255));

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(generateReportButton)))
                .addContainerGap(2305, Short.MAX_VALUE))
        );
        payrollPanelLayout.setVerticalGroup(
            payrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(payrollPanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(payrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(generateReportButton))
                .addGap(26, 26, 26)
                .addComponent(reportPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1069, Short.MAX_VALUE))
        );

        contentPanel.add(payrollPanel, "card4");

        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(contentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(contentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(511, Short.MAX_VALUE))
        );

        headerPanel.setBackground(new java.awt.Color(0, 51, 102));
        headerPanel.setPreferredSize(new java.awt.Dimension(850, 400));

        lblUserName.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        lblUserName.setForeground(new java.awt.Color(255, 255, 255));
        lblUserName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/user.png"))); // NOI18N

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/headerImage.png"))); // NOI18N

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 522, Short.MAX_VALUE)
                .addComponent(lblUserName)
                .addGap(146, 146, 146))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblUserName)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(341, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout DashboardPanelLayout = new javax.swing.GroupLayout(DashboardPanel);
        DashboardPanel.setLayout(DashboardPanelLayout);
        DashboardPanelLayout.setHorizontalGroup(
            DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DashboardPanelLayout.createSequentialGroup()
                .addGap(136, 136, 136)
                .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rightPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(sidemenuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        DashboardPanelLayout.setVerticalGroup(
            DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DashboardPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sidemenuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(DashboardPanelLayout.createSequentialGroup()
                        .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rightPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        MainFrame.add(DashboardPanel, "dashboard");

        LoginPanel.setBackground(new java.awt.Color(140, 174, 201));
        LoginPanel.setForeground(new java.awt.Color(250, 235, 187));
        LoginPanel.setToolTipText("");

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel2.setText("Employee Login");

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel3.setText("Username");

        txtUserName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserNameActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel4.setText("Password");

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 0, 12)); // NOI18N
        jLabel5.setText("Forgot password?");

        jCheckBox1.setFont(new java.awt.Font("Helvetica Neue", 0, 12)); // NOI18N
        jCheckBox1.setText("Remember me");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        loginButton.setBackground(new java.awt.Color(0, 51, 102));
        loginButton.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        loginButton.setForeground(new java.awt.Color(255, 255, 255));
        loginButton.setText("Log in");
        loginButton.setBorder(null);
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        deleteAllData.setBackground(new java.awt.Color(136, 156, 176));
        deleteAllData.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        deleteAllData.setForeground(new java.awt.Color(255, 255, 255));
        deleteAllData.setText("Clean Start");
        deleteAllData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteAllDataActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logoLogin.png"))); // NOI18N

        javax.swing.GroupLayout LoginPanelLayout = new javax.swing.GroupLayout(LoginPanel);
        LoginPanel.setLayout(LoginPanelLayout);
        LoginPanelLayout.setHorizontalGroup(
            LoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginPanelLayout.createSequentialGroup()
                .addGap(380, 380, 380)
                .addComponent(jLabel1))
            .addGroup(LoginPanelLayout.createSequentialGroup()
                .addGap(490, 490, 490)
                .addComponent(jLabel2))
            .addGroup(LoginPanelLayout.createSequentialGroup()
                .addGap(400, 400, 400)
                .addComponent(jLabel3))
            .addGroup(LoginPanelLayout.createSequentialGroup()
                .addGap(400, 400, 400)
                .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(LoginPanelLayout.createSequentialGroup()
                .addGap(400, 400, 400)
                .addComponent(jLabel4))
            .addGroup(LoginPanelLayout.createSequentialGroup()
                .addGap(400, 400, 400)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(LoginPanelLayout.createSequentialGroup()
                .addGap(400, 400, 400)
                .addComponent(jCheckBox1)
                .addGap(111, 111, 111)
                .addComponent(jLabel5))
            .addGroup(LoginPanelLayout.createSequentialGroup()
                .addGap(510, 510, 510)
                .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(LoginPanelLayout.createSequentialGroup()
                .addGap(400, 400, 400)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(LoginPanelLayout.createSequentialGroup()
                .addGap(480, 480, 480)
                .addComponent(deleteAllData, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        LoginPanelLayout.setVerticalGroup(
            LoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginPanelLayout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(jLabel1)
                .addGap(0, 0, 0)
                .addComponent(jLabel2)
                .addGap(37, 37, 37)
                .addComponent(jLabel3)
                .addGap(2, 2, 2)
                .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabel4)
                .addGap(2, 2, 2)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addGroup(LoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox1)
                    .addComponent(jLabel5))
                .addGap(41, 41, 41)
                .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(deleteAllData, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        MainFrame.add(LoginPanel, "login");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(MainFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 1168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(MainFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Name: txtUserNameActionPerformed Purpose: This method handles the event
     * when the user presses Enter while focused on the username text field. It
     * shifts the focus to the password field.
     *
     * @param evt The ActionEvent triggered by pressing Enter in the username
     * field.
     */
    private void txtUserNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserNameActionPerformed

    /**
     * Name: jCheckBox1ActionPerformed Purpose: Handles the event of toggling
     * the password visibility checkbox.
     *
     * @param evt The ActionEvent triggered when the checkbox is selected or
     * deselected.
     */
    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed
    /**
     * Name: loginButtonActionPerformed
     *
     * @author Zahraa Purpose: Handles the login action, verifying the username
     * and password inputs. If valid, it checks for the serialized data file
     * ("HRSystem.dat"). If the file exists, it deserializes the data into the
     * employee and department lists. If default data has not been loaded, it
     * calls the `deserializeData()` method to initialize data. Updates the
     * tables and switches to the dashboard view.
     *
     * @param evt The ActionEvent triggered by the login button.
     */
    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        // TODO add your handling code here:

        //Verifing that input is not null
        if (txtUserName.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            JOptionPane.showMessageDialog(MainWindow.this, "Username and Password should not be empty!",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                File file = new File("HRSystem.dat");
                if (file.exists()) {
                    loadSerializedData();
                } else {
                    loadFromStartupFile();
                }

                initialiseEmployeesTable();
                initialiseDepartmentsTable();
                populateDepartmentsComboBox();

                CardLayout cl = (CardLayout) MainFrame.getLayout();
                cl.show(MainFrame, "dashboard");
                lblUserName.setText(txtUserName.getText());

            } catch (Exception e) { // loading methods handle specific exceptions
                JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(),
                        "Load Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

    }//GEN-LAST:event_loginButtonActionPerformed
    /**
     * Name: employeesButtonActionPerformed Purpose: This method switches the
     * main content panel to the "employees" view when the Employees button is
     * clicked.
     *
     * @param evt The ActionEvent triggered by clicking the Employees button.
     */
    private void employeesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeesButtonActionPerformed
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) (contentPanel.getLayout());
        cl.show(contentPanel, "employees");

    }//GEN-LAST:event_employeesButtonActionPerformed
    /**
     * Name: addEmployeeButtonActionPerformed Purpose: Opens the AddEmployeeForm
     *
     * @author Zainab when the "Add Employee" button is clicked and hides the
     * main window while the new form is open.
     *
     * @param evt The ActionEvent triggered by clicking the Add Employee button.
     */
    private void addEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEmployeeButtonActionPerformed
        // TODO add your handling code here:
        // Create and show the AddEmployeeForm
        // Hide the main window
        setVisible(false);
        AddEmployeeForm addEmployeeForm = new AddEmployeeForm(this);
        addEmployeeForm.setVisible(true);


    }//GEN-LAST:event_addEmployeeButtonActionPerformed

    /**
     * Name: searchEmployeesTextFieldActionPerformed Purpose: Handles actions
     * when the user presses enter on the employee search field.
     *
     * @param evt The ActionEvent triggered by pressing enter in the search
     * field.
     */
    private void searchEmployeesTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchEmployeesTextFieldActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_searchEmployeesTextFieldActionPerformed
    /**
     * Name: searchEmployeesTextFieldFocusGained Purpose: Clears default text
     * when the search field gains focus.
     *
     * @author Zainab
     * @param evt The FocusEvent triggered when the search field is focused.
     */
    private void searchEmployeesTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchEmployeesTextFieldFocusGained
        // TODO add your handling code here:
        searchEmployeesTextField.setText("");
    }//GEN-LAST:event_searchEmployeesTextFieldFocusGained
    /**
     * Name: departmentsButtonActionPerformed Purpose: Displays the departments
     * panel.
     *
     * @author Zainab
     * @param evt The ActionEvent triggered by clicking the Departments button.
     */
    private void departmentsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_departmentsButtonActionPerformed
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) (contentPanel.getLayout());
        cl.show(contentPanel, "departments");
    }//GEN-LAST:event_departmentsButtonActionPerformed
    /**
     * Name: addDepartmentButtonActionPerformed Purpose: Opens AddDepartmentForm
     * and hides main window.
     *
     * @author Zainab
     * @param evt The ActionEvent triggered by clicking the Add Department
     * button.
     */
    private void addDepartmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDepartmentButtonActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        AddDepartmentForm addDeptartmentForm = new AddDepartmentForm(this);
        addDeptartmentForm.setVisible(true);
    }//GEN-LAST:event_addDepartmentButtonActionPerformed
    /**
     * Name: searchDepartmentsFieldActionPerformed Purpose: Placeholder for
     * actions on department search field.
     *
     * @param evt The ActionEvent triggered when user presses enter in
     * department search field.
     */
    private void searchDepartmentsFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchDepartmentsFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchDepartmentsFieldActionPerformed
    /**
     * Name: searchDepartmentsFieldFocusGained Purpose: Clears default text when
     * the department search field gains focus.
     *
     * @author Zainab
     * @param evt The FocusEvent triggered when field gains focus.
     */
    private void searchDepartmentsFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchDepartmentsFieldFocusGained
        // TODO add your handling code here:
        searchDepartmentsField.setText("");
    }//GEN-LAST:event_searchDepartmentsFieldFocusGained
    /**
     * Name: exitButtonActionPerformed
     *
     * @author Zahraa Purpose: Handles the action of clicking the exit button.
     * Prompts the user to confirm whether they want to save changes before
     * exiting. If the user chooses to save, the current state of employees,
     * departments, and static ID counters are serialized to "HRSystem.dat".
     * Exits the application after performing the selected action.
     *
     * @param evt The ActionEvent triggered by the exit button.
     */

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed

        // Prompt the user to ask if they want to save changes before exiting
        int option = JOptionPane.showConfirmDialog(this, "Do you want to save changes before exiting?",
                "Save Changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            // Serializing the employees & departments array lists and the static ID count for employees & departments
            try (FileOutputStream fileOut = new FileOutputStream("HRSystem.dat"); ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

                out.writeObject(allEmployees);
                out.writeObject(departments);
                out.writeObject(staticEmployeeID);
                out.writeObject(staticDeptID);

                JOptionPane.showMessageDialog(this, "Information saved!", "Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.exit(0);  // exit after saving
        } else if (option == JOptionPane.NO_OPTION) {
            System.exit(0);  // exit without saving
        }
        // if CANCEL_OPTION or CLOSED_OPTION: do nothing, return to app
    }//GEN-LAST:event_exitButtonActionPerformed

    /**
     * Name: employeesTableMouseClicked
     *
     * @author Zainab Purpose: Handles the action when a row in the employees
     * table is clicked. Retrieves the employee ID from the selected row,
     * searches for the corresponding Employee object in the employee list, and
     * displays the employee details. Displays error messages if the employee ID
     * is invalid or the employee is not found.
     *
     * @param evt The MouseEvent triggered by clicking on a table row.
     */

    private void employeesTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_employeesTableMouseClicked
        // TODO add your handling code here:

        int selectedRow = employeesTable.getSelectedRow();

        if (selectedRow != -1) {
            Object employeeIdObj = employeesTable.getValueAt(selectedRow, 0);

            if (employeeIdObj instanceof Integer) {
                int employeeId = (Integer) employeeIdObj;
                Employee selectedEmployee = null;

                if (allEmployees != null) {
                    for (Employee emp : allEmployees) {
                        if (emp.getEmployeeId() == employeeId) {
                            selectedEmployee = emp;
                            break;
                        }
                    }
                }

                if (selectedEmployee != null) {
                    showEmployeeDetails(selectedEmployee);
                } else {
                    JOptionPane.showMessageDialog(this, "Employee not found.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Employee ID.");
            }
        }
    }//GEN-LAST:event_employeesTableMouseClicked
    /**
     * Name: editEmployeeButtonActionPerformed Purpose: Opens EditEmployeeForm
     * for selected employee.
     *
     * @author Zainab
     * @param evt The ActionEvent triggered by Edit button.
     */
    private void editEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editEmployeeButtonActionPerformed
        // TODO add your handling code here:

        EditEmployeeForm editForm = new EditEmployeeForm(this, selectedEmployee);
        editForm.setVisible(true);


    }//GEN-LAST:event_editEmployeeButtonActionPerformed
    /**
     * Name: idDetailPageActionPerformed Purpose: Placeholder for ID detail page
     * button action.
     *
     * @param evt The ActionEvent triggered by ID detail page button.
     */
    private void idDetailPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idDetailPageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idDetailPageActionPerformed
    /**
     * Name: departmentsTableMouseClicked
     *
     * @author Zainab Purpose: Handles the action when a row in the departments
     * table is clicked. Retrieves the department ID from the selected row,
     * searches for the corresponding Department object in the department list,
     * and displays the department details. Displays error messages if the
     * department ID is invalid or the department is not found.
     *
     * @param evt The MouseEvent triggered by clicking on a table row.
     */

    private void departmentsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_departmentsTableMouseClicked
        // TODO add your handling code here:
        // fake data to test detail page
        int selectedRow = departmentsTable.getSelectedRow();

        if (selectedRow != -1) {
            Object deptIdObj = departmentsTable.getValueAt(selectedRow, 0);

            if (deptIdObj instanceof Integer) {
                int deptId = (Integer) deptIdObj;
                Department selectedDepartment = null;

                if (departments != null) {
                    for (Department dept : departments) {
                        if (dept.getDeptID() == deptId) {
                            selectedDepartment = dept;
                            break;
                        }
                    }
                }

                if (selectedDepartment != null) {
                    showDepartmentDetails(selectedDepartment);
                } else {
                    JOptionPane.showMessageDialog(this, "Department not found.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid department ID.");
            }
        }
    }//GEN-LAST:event_departmentsTableMouseClicked
    /**
     * Name: genderDetailPageActionPerformed Purpose/description: Placeholder
     * method for future implementation of gender detail page behavior.
     *
     * @param evt - the action event triggered by the user interaction.
     * @return void - this method does not return any value.
     *
     */
    private void genderDetailPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_genderDetailPageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_genderDetailPageActionPerformed
    /**
     * Name: backToEmployeesButtonActionPerformed Purpose/description: Returns
     * to the employees panel from another view.
     *
     * @author Zainab
     * @param evt - the action event triggered by the button click.
     * @return void - this method does not return any value.
     */
    private void backToEmployeesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backToEmployeesButtonActionPerformed
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "employees");

    }//GEN-LAST:event_backToEmployeesButtonActionPerformed
    /**
     * Name: backToDepartmentsButtonActionPerformed Purpose/description: Returns
     * to the departments panel from another view.
     *
     * @author Zainab
     * @param evt - the action event triggered by the button click.
     * @return void - this method does not return any value.
     */
    private void backToDepartmentsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backToDepartmentsButtonActionPerformed
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "departments");
    }//GEN-LAST:event_backToDepartmentsButtonActionPerformed
    /**
     * Name: editDepartmentButtonActionPerformed Purpose/description: Opens the
     * edit form for the selected department.
     *
     * @author Zainab
     * @param evt - the action event triggered by the button click.
     * @return void - this method does not return any value.
     */
    private void editDepartmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editDepartmentButtonActionPerformed
        // TODO add your handling code here:
        EditDepartmentForm editForm = new EditDepartmentForm(this, selectedDepartment);
        editForm.setVisible(true);

    }//GEN-LAST:event_editDepartmentButtonActionPerformed
    /**
     * Name: idDepartmentDetailPageActionPerformed Purpose/description:
     * Placeholder method for ID field interaction in department detail page.
     *
     * @author Zainab
     * @param evt - the action event triggered by the user interaction.
     * * @return void - this method does not return any value.
     */
    private void idDepartmentDetailPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idDepartmentDetailPageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idDepartmentDetailPageActionPerformed
    /**
     * Name: locationDetailPageActionPerformed Purpose/description: Placeholder
     * method for location field interaction in department detail page.
     *
     * @param evt - the action event triggered by the user interaction.
     * @return void - this method does not return any value.
     */
    private void locationDetailPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locationDetailPageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_locationDetailPageActionPerformed
    /**
     * Name: generateReportButtonActionPerformed
     *
     * @author: Hajar Purpose: Generates a payroll report for all employees,
     * grouped by department. - Checks if employees exist to generate the
     * report. - Creates a directory and payroll report file in the user's home
     * folder. - Writes employee payroll details and department totals to the
     * file. - Calculates biweekly pay based on employee pay level. - Displays
     * the generated report content in a text pane. - Copies the report file to
     * the project directory. - Handles errors with informative dialogs and
     * logs.
     *
     * @param evt The ActionEvent triggered by clicking the Generate Report
     * button.
     */

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
                    "Error generating payroll report:\n"
                    + e.getMessage()
                    + "\n\nPlease check file permissions and try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            // Print full stack trace for unexpected errors
            e.printStackTrace();

            // Log unexpected error details
            System.err.println("Unexpected Error: " + e.getMessage());

            // Show user-friendly error message
            JOptionPane.showMessageDialog(this,
                    "Unexpected error:\n" + e.getMessage()
                    + "\n\nPlease contact support.",
                    "Critical Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_generateReportButtonActionPerformed

    /**
     * Name: payrollReportButtonActionPerformed Purpose/description: Switches
     * the view to the payroll report panel.
     *
     * @author Hajar
     * @param evt - the action event triggered by the button click.
     * * @return void - this method does not return any value.
     */
    private void payrollReportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payrollReportButtonActionPerformed
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) (contentPanel.getLayout());
        cl.show(contentPanel, "payroll");
    }//GEN-LAST:event_payrollReportButtonActionPerformed
    /**
     * Name: deleteButtonActionPerformed
     *
     * @author Raghad Purpose/description: Deletes the selected employee after
     * user confirmation.
     * @param evt - the action event triggered by the button click.
     * @return void - this method does not return any value.
     */
    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure that you want to delete this employee?",
                "DELETE EMPLOYEE", 2, JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {

            //remove the employee from the data source
            deleteEmployee(selectedEmployee);
            JOptionPane.showMessageDialog(this, "Employee deleted succefully.");

            employeeDetailPanel.setVisible(false);

            // Switch back to Employee Panel 
            CardLayout cl = (CardLayout) contentPanel.getLayout();
            cl.show(contentPanel, "employeePanel");

        }

    }//GEN-LAST:event_deleteButtonActionPerformed

    /**
     * Name: deleteButton1ActionPerformed
     *
     * @author Raghad Purpose/description: Deletes the selected department if no
     * employees are assigned to it.
     * @param evt - the action event triggered by the button click.
     * @return void - this method does not return any value.
     */
    private void deleteButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButton1ActionPerformed
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure that you want to delete this department?",
                "DELETE DEPARTMENT", 2);

        if (confirm == JOptionPane.YES_OPTION) {
            if (confirm == JOptionPane.YES_OPTION) {
                boolean hasEmployees = false;
                int i = 0;

                // Use while loop to check if any employee is in the selected department
                if (allEmployees != null) {
                    while (i < allEmployees.size()) {
                        Employee emp = allEmployees.get(i);
                        if (emp.getDeptID() != null && emp.getDeptID() == selectedDepartment.getDeptID()) {
                            hasEmployees = true;
                            break;
                        }
                        i++;
                    }
                }

                if (hasEmployees) {
                    JOptionPane.showMessageDialog(this,
                            "Cannot delete department. It still has employees assigned to it.",
                            "Delete Failed",
                            0);
                } else {
                    deleteDepartment(selectedDepartment);
                    JOptionPane.showMessageDialog(this, "Department deleted successfully.");
                    departmentDetailPanel.setVisible(false);

                    // Switch back to Department Panel 
                    CardLayout cl = (CardLayout) contentPanel.getLayout();
                    cl.show(contentPanel, "departments");
                }
            }

            // Refresh tables
            refreshEmployeeTable();
            refreshDepartmentTable();
        }


    }//GEN-LAST:event_deleteButton1ActionPerformed

    /**
     * Name: departmentsListSelectActionPerformed Purpose/description: Updates
     * the employees table based on the selected department from the combo box.
     *
     * @author Zainab Input: evt - the action event triggered by selecting a
     * department. Output: none Effect: Updates the employees table to show only
     * employees belonging to the selected department or all employees if "All
     * Departments" is selected.
     *
     * @param evt - the action event triggered by the department selection.
     * @return void- this method does not return any value.
     */
    private void departmentsListSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_departmentsListSelectActionPerformed
// TODO add your handling code here:
        try {
            String selectedDepartment = (String) departmentsListSelect.getSelectedItem();

            if (selectedDepartment == null || selectedDepartment.equals("All Departments")) {
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

                    String annualPay = getAnnualPayByLevel(payLevel);
                    Object[] row = {id, fullName, department, gender, annualPay};
                    model.addRow(row);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An error occurred while updating the employee table: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Optional for debugging
        }

    }//GEN-LAST:event_departmentsListSelectActionPerformed
    /**
     * Name: payLevelDetailPageActionPerformed
     *
     * @author Zainab Purpose/description: Placeholder method for future
     * implementation of pay level detail page actions. Input: evt - the action
     * event triggered by user interaction. Output: none Effect: Currently does
     * nothing.
     *
     * @param evt - the action event triggered by pay level detail page
     * interaction.
     * @return void - this method does not return any value.
     */
    private void payLevelDetailPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payLevelDetailPageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_payLevelDetailPageActionPerformed

    /**
     * Name: deleteAllDataActionPerformed
     *
     * @author Zainab Purpose: Handles the action when the delete button is
     * clicked to remove all saved data. It attempts to delete the serialized
     * data file "HRSystem.dat". If deletion is successful, it also clears the
     * current employee and department lists. The user is notified of the result
     * through message dialogs.
     *
     * @param evt The ActionEvent triggered by the button click.
     * @return void This method does not return any value.
     */

    private void deleteAllDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteAllDataActionPerformed
        // TODO add your handling code here:
        // Clear and delete the serialised file 
        File file = new File("HRSystem.dat");
        if (file.exists()) {
            if (file.delete()) {
                JOptionPane.showMessageDialog(null, "Data file deleted successfully.");
                allEmployees.clear();
                departments.clear();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete data file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Data file does not exist.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_deleteAllDataActionPerformed

    /**
     * Name: main
     *
     * Purpose/description: The main entry point to launch the GUI application.
     * Input: args - command line arguments. Output: none Effect: Initializes
     * and shows the main GUI form.
     *
     * @param args - command line arguments.
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
    private javax.swing.JPanel DashboardPanel;
    private javax.swing.JPanel LoginPanel;
    private javax.swing.JPanel MainFrame;
    private javax.swing.JButton addDepartmentButton;
    private javax.swing.JButton addEmployeeButton;
    private javax.swing.JTextArea addressDetailPage;
    private javax.swing.JButton backToDepartmentsButton;
    private javax.swing.JButton backToEmployeesButton;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JButton deleteAllData;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton deleteButton1;
    private javax.swing.JTextField departmentDetailPage;
    private javax.swing.JPanel departmentDetailPanel;
    private javax.swing.JLabel departmentNameDetailPage;
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
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JTextField locationDetailPage;
    private javax.swing.JButton loginButton;
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
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
}
