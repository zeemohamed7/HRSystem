/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logic;

/**
 *
 * @author zainab
 */
public class Employee {
    private int staticEmployeeId = 0;
    private int employeeId;
    private String firstName;
    private String surname;
    private char gender;
    private String address;
    private int payLevel;  
    private boolean isHead;

    public Employee() {
    }

    public Employee(String firstName, String surname, char gender, String address, int payLevel) {
        employeeId = staticEmployeeId++;
        this.firstName = firstName;
        this.surname = surname;
        this.gender = gender;
        this.address = address;
        this.payLevel = payLevel;
    }


    public int getEmployeeId() {
        return employeeId;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPayLevel() {
        return payLevel;
    }

    public void setPayLevel(int payLevel) {
        this.payLevel = payLevel;
    }

    public boolean isIsHead() {
        return isHead;
    }

    public void setIsHead(boolean isHead) {
        this.isHead = isHead;
    }
    
    
    
}