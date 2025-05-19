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
 *
 * @author Zainab
 * @version 1.0 Purpose: Represents a department within the organization,
 * including its ID, name, location, and optionally its head employee.
 */
public class Department implements Serializable {

    private int deptID; //Purpose: Unique ID of the department
    private String name; //Purpose: Name of the department
    private String location; //Purpose: Location of the department
    private Employee departmentHead; //Purpose: Head of the department

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
     * @param staticDeptID - auto-generated department ID
     * @param name - the name of the department
     * @param location - the location of the department
     *
     * Effect: Initializes department with name and location, assigns
     * auto-generated ID
     */
    public Department(int staticDeptID, String name, String location) {
        this.deptID = staticDeptID;
        this.name = name;
        this.location = location;
    }

    /**
     * Retrieves the unique ID of this department.
     *
     * @return the unique department ID
     */
    public int getDeptID() {
        return deptID;
    }

    /**
     * Retrieves the name of this department.
     *
     * @return the name of the department
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a new name for the department.
     *
     * @param name the new name to set for the department (e.g., "Human
     * Resources", "Finance")
     *
     * Effect: Updates the department's name field.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the location of the department.
     *
     * @return the department's location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets a new location for the department.
     *
     * @param location the new location to assign to the department
     *
     * Effect: Updates the department's location field.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Returns the current head of the department.
     *
     * @return the department head (an Employee object)
     */
    public Employee getDepartmentHead() {
        return departmentHead;
    }

    /**
     * Assigns a new employee as the head of the department.
     *
     * @param departmentHead the employee to assign as department head
     *
     * Effect: Updates the department head field.
     */
    public void setDepartmentHead(Employee departmentHead) {
        this.departmentHead = departmentHead;
    }

    /**
     * Returns the name of the department for display purposes.
     *
     * @return the department name if deptID is not 0; otherwise, returns
     * "Select Department"
     */
    @Override
    public String toString() {
        return (deptID == 0) ? "Select Department" : name;
    }

    /**
     * Looks up the name of a department by its ID.
     *
     * @param departments the list of all departments
     * @param deptId the ID of the department to search for
     * @return the department name if found, otherwise "No Department"
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
     * Finds the index of a department by its ID in the list.
     *
     * @param departments the list of all departments
     * @param deptId the ID of the department to find
     * @return the index of the department if found, otherwise 0
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
     * Retrieves the ID of a department based on its name.
     *
     * @param departments the list of all departments
     * @param name the name of the department to search for
     * @return the department ID if found, otherwise 0
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
     * Calculates biweekly pay based on the provided pay level.
     *
     * @param payLevel the pay level ranging from 1 to 8
     * @return the calculated biweekly pay, or 0.0 if pay level is invalid
     */
    public static double calculateBiweeklyPay(int payLevel) {
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

        if (payLevel < 1 || payLevel >= payLevelSalaries.length) {
            return 0.0;
        }

        return payLevelSalaries[payLevel] / 26.0;
    }

}
