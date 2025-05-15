/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logic;

import java.io.Serializable;

/**
 *
 * Name: Employee
 *
 * @version 1.0 Purpose: Represents an employee with personal details,
 * employment information, and association to a department. It includes logic
 * for assigning unique employee IDs and tracking department head status.
 *
 */
public class Employee implements Serializable {

    private int employeeId; // Unique identifier for the employee
    private String firstName; // Employee's first name
    private String surname; // Employee's surname
    private char gender; // Employee's gender (M/F)
    private String address; // Employee's residential address
    private int payLevel;  // Pay level used to calculate salary
    private boolean isHead; // Whether this employee is the head of a department
    private Integer deptID; // ID of the department this employee belongs to. Converted to Integer as it can be null

    /**
     * Name: Employee Purpose: Default constructor Effect: Creates an empty
     * employee object
     */
    public Employee() {
    }

    /**
     * Name: Employee Purpose: Constructor to create an employee without
     * assigning a department
     *
     * @param firstName - employee's first name
     * @param surname - employee's last name
     * @param gender - 'M' or 'F' representing the employee's gender
     * @param address - employee's address
     * @param payLevel - salary level of the employee Effect: Initializes an
     * employee object and auto-generates a unique ID
     */
    public Employee(int employeeId, String firstName, String surname, char gender, String address, int payLevel) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.surname = surname;
        this.gender = gender;
        this.address = address;
        this.payLevel = payLevel;
        this.deptID = null;
        this.isHead = false;
    }

    /**
     * Name: Employee Purpose: Constructor to create an employee with all
     * details including department assignment.
     *
     * @param employeeId - unique identifier for the employee
     * @param firstName - employee's first name
     * @param surname - employee's last name
     * @param gender - 'M' or 'F' representing the employee's gender
     * @param address - employee's address
     * @param payLevel - salary level of the employee
     * @param deptID - ID of the department the employee belongs to Effect:
     * Initializes an employee object with the provided information and sets
     * isHead to false by default.
     */

    public Employee(int employeeId, String firstName, String surname, char gender, String address, int payLevel, int deptID) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.surname = surname;
        this.gender = gender;
        this.address = address;
        this.payLevel = payLevel;
        this.deptID = deptID;
        this.isHead = false;

    }

    /**
     * @return employee's unique ID
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId sets the employee's ID
     */
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return employee's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName sets the employee's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return employee's surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname sets the employee's surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return employee's gender
     */
    public char getGender() {
        return gender;
    }

    /**
     * @param gender sets the employee's gender (M or F)
     */
    public void setGender(char gender) {
        this.gender = gender;
    }

    /**
     * @return employee's address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address sets the employee's address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return employee's pay level
     */
    public int getPayLevel() {
        return payLevel;
    }

    /**
     * @param payLevel sets the employee's pay level
     */
    public void setPayLevel(int payLevel) {
        this.payLevel = payLevel;
    }

    /**
     * @return true if employee is department head, false otherwise
     */
    public boolean isIsHead() {
        return isHead;
    }

    /**
     * @param isHead sets the employee's department head status
     */
    public void setIsHead(boolean isHead) {
        this.isHead = isHead;
    }

    /**
     * @return the ID of the department this employee belongs to
     */
    public Integer getDeptID() {
        return deptID;
    }

    /**
     * @param deptID sets the department ID for the employee
     */
    public void setDeptID(Integer deptID) {
        this.deptID = deptID;
    }

}
