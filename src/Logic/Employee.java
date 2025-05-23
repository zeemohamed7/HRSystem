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

    private int employeeId; //Purpose: Unique identifier for the employee
    private String firstName; //Purpose: Employee's first name
    private String surname; //Purpose: Employee's surname
    private char gender; //Purpose: Employee's gender (M/F)
    private String address; //Purpose: Employee's residential address
    private int payLevel;  //Purpose: Pay level used to calculate salary
    private boolean isHead; //Purpose: Whether this employee is the head of a department
    private Integer deptID; //Purpose: ID of the department this employee belongs to. Converted to Integer as it can be null

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
     * @param employeeId - autogenerated employee ID
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
     * @param employeeId - unique identifier for the employee (auto generated)
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
     * Retrieves the unique ID of the employee.
     *
     * @return the employee's ID
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * Sets the unique ID of the employee.
     *
     * @param employeeId the ID to assign to the employee
     */
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Retrieves the first name of the employee.
     *
     * @return the employee's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the employee.
     *
     * @param firstName the first name to assign
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Retrieves the surname of the employee.
     *
     * @return the employee's surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the surname of the employee.
     *
     * @param surname the surname to assign
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Retrieves the gender of the employee.
     *
     * @return the employee's gender (e.g., 'M' or 'F')
     */
    public char getGender() {
        return gender;
    }

    /**
     * Sets the gender of the employee.
     *
     * @param gender the gender to assign (e.g., 'M' or 'F')
     */
    public void setGender(char gender) {
        this.gender = gender;
    }

    /**
     * Retrieves the address of the employee.
     *
     * @return the employee's address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the employee.
     *
     * @param address the address to assign
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Retrieves the pay level of the employee.
     *
     * @return the employee's pay level
     */
    public int getPayLevel() {
        return payLevel;
    }

    /**
     * Sets the pay level of the employee.
     *
     * @param payLevel the pay level to assign
     */
    public void setPayLevel(int payLevel) {
        this.payLevel = payLevel;
    }

    /**
     * Checks if the employee is the head of a department.
     *
     * @return true if the employee is a department head; false otherwise
     */
    public boolean isIsHead() {
        return isHead;
    }

    /**
     * Sets the department head status of the employee.
     *
     * @param isHead true if the employee is a department head; false otherwise
     */
    public void setIsHead(boolean isHead) {
        this.isHead = isHead;
    }

    /**
     * Retrieves the department ID associated with the employee.
     *
     * @return the employee's department ID
     */
    public Integer getDeptID() {
        return deptID;
    }

    /**
     * Sets the department ID for the employee.
     *
     * @param deptID the department ID to assign
     */
    public void setDeptID(Integer deptID) {
        this.deptID = deptID;
    }

}
