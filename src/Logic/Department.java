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
=======

public class Department {
    private int staticDeptId = 0;
    private int deptID;
    private String name;
    private String location;
    private Employee departmentHead; 
    private ArrayList<Employee> employees;

    public Department() {
    }

    //Constructor without department head
    public Department(String name, String location) {
        deptID = staticDeptId++;
        this.name = name;
        this.location = location;
        employees = new ArrayList();
    }

    //Constructor with department head
    public Department(String name, String location, Employee departmentHead) {
        deptID = staticDeptId++;
        this.name = name;
        this.location = location;
        this.departmentHead = departmentHead;
        employees = new ArrayList();
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

    public ArrayList<Employee> getEmployees() {
        return employees;
    }
 
}
