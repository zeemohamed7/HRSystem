/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logic;

/**
 *
 * @author zainab
 */
public class Department {
    private int id;
    private String name;
    private String location;
    private Employee departmentHead; 
    
    // Constructor with dept head
    public Department(int id, String name, String location, Employee departmentHead) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.departmentHead = departmentHead;
    }

    // Constructor without dept head
    public Department(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.departmentHead = null;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    
    
}
