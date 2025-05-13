/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logic;

import java.util.ArrayList;

/**
 *
 * @author zainab
 */

public class Department {
    private static int staticDeptId = 1;
    private int deptID;
    private String name;
    private String location;
    private Employee departmentHead; 

    public Department() {
    }

    //Constructor without department head
    public Department(String name, String location) {
        this.deptID = staticDeptId++;
        this.name = name;
        this.location = location;
    }

    //Constructor with department head
    public Department(String name, String location, Employee departmentHead) {
        this.deptID = staticDeptId++;
        this.name = name;
        this.location = location;
        this.departmentHead = departmentHead;
    }

    public int getDeptID() {
        return deptID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Employee getDepartmentHead() {
        return departmentHead;
    }

    public void setDepartmentHead(Employee departmentHead) {
        this.departmentHead = departmentHead;
    }
        @Override
    public String toString() {
            return (deptID == 0) ? "Select Department" : name; //  makes jcombo display the department name
    }
    // Looks up department name by ID
        public static String getDepartmentNameById(ArrayList<Department> departments, int deptId) {
        for (Department dept : departments) {
            if (dept.getDeptID() == deptId) {
                return dept.getName();
            }
        }
        return "No Department";
    }

    public static int findDepartmentIndexById(ArrayList<Department> departments, int deptId) {
        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).getDeptID() == deptId) {
                return i;
            }
        }
        return 0;
    }

    // Looks up ID name by name
        public static int getIdByDepartmentName(ArrayList<Department> departments, String name) {
        for (Department dept : departments) {
            if (dept.getName().equals(name)) {
                return dept.getDeptID();
            }
        }
        return 0;
    }

    // Helper method to calculate biweekly pay based on pay level
    public static double calculateBiweeklyPay(int payLevel) {
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
}
