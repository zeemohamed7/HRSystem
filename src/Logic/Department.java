/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logic;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * Name: Department
 * @author Zainab
 * @version 1.0 Purpose: Represents a department within the organization,
 * including its ID, name, location, and optionally its head employee.
 */
public class Department implements Serializable {

    private int deptID; // Unique ID of the department
    private String name; // Name of the department
    private String location; // Location of the department
    private Employee departmentHead; // Head of the department

    /**
     * Name: Department Purpose: Default constructor Effect: Creates an empty
     * department object
     */
    public Department() {
    }

    /**
     * Name: Department Purpose: Constructor to create a department without
     * assigning a head
     *
     * @param name - the name of the department
     * @param location - the location of the department Effect: Initializes
     * department with name and location, assigns auto-generated ID
     */
    public Department(int staticDeptID, String name, String location) {
        this.deptID = staticDeptID;
        this.name = name;
        this.location = location;
    }

    /**
     * @return the unique department ID
     */
    public int getDeptID() {
        return deptID;
    }

    /**
     * @return the name of the department
     */
    public String getName() {
        return name;
    }

    /**
     * @param name - new name to set for the department Effect: Updates the
     * department name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the location of the department
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location - new location to set for the department Effect: Updates
     * the department location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the current head of the department
     */
    public Employee getDepartmentHead() {
        return departmentHead;
    }

    /**
     * @param departmentHead - employee to assign as department head Effect:
     * Updates the department head
     */

    public void setDepartmentHead(Employee departmentHead) {
        this.departmentHead = departmentHead;
    }

    /**
     * Name: toString Purpose: Returns the name of the department for display
     *
     * @return department name if deptID is not 0; otherwise, "Select
     * Department"
     */
    @Override
    public String toString() {
        return (deptID == 0) ? "Select Department" : name; //  makes jcombo display the department name
    }

    /**
     * Name: getDepartmentNameById Purpose: Looks up department name by its ID
     *
     * @param departments - list of all departments
     * @param deptId - the department ID to search for
     * @return the name of the department if found, else "No Department"
     */
    public static String getDepartmentNameById(ArrayList<Department> departments, int deptId) {
        for (Department dept : departments) {
            if (dept.getDeptID() == deptId) {
                return dept.getName();
            }
        }
        return "No Department";
    }

    /**
     * Name: findDepartmentIndexById Purpose: Finds index of a department by ID
     * in the list
     *
     * @param departments - list of all departments
     * @param deptId - the department ID to find
     * @return index of the department if found, else 0
     */
    public static int findDepartmentIndexById(ArrayList<Department> departments, int deptId) {
        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).getDeptID() == deptId) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Name: getIdByDepartmentName Purpose: Retrieves the ID of a department
     * based on its name
     *
     * @param departments - list of all departments
     * @param name - the name of the department to find
     * @return department ID if found, else 0
     */
    public static int getIdByDepartmentName(ArrayList<Department> departments, String name) {
        for (Department dept : departments) {
            if (dept.getName().equals(name)) {
                return dept.getDeptID();
            }
        }
        return 0;
    }

    /**
     * Name: calculateBiweeklyPay author: Hajar Purpose: Calculates biweekly
     * salary based on pay level
     *
     * @param payLevel - pay level from 1 to 8
     * @return biweekly pay corresponding to pay level, or 0.0 if invalid
     */
    public static double calculateBiweeklyPay(int payLevel) {
        // Pay levels and their corresponding annual salaries
        double[] payLevelSalaries = {
            0.0, // Level 0 (invalid/placeholder)
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
}
