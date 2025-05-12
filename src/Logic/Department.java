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
        public static String getDepartmentNameById(ArrayList<Department> departments, int id) {
        for (Department dept : departments) {
            if (dept.getDeptID() == id) {
                return dept.getName();
            }
        }
        return "Unknown";
    }
    // Looks up ID name by name
        public static int getIdByDepartmentName(ArrayList<Department> departments, String name) {
        for (Department dept : departments) {
            if (dept.getName() == name) {
                return dept.getDeptID();
            }
        }
        return 0;
    }

 
}
