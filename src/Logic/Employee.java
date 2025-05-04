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
    private int id;
    private String name;
    private String department;
    private String gender;
    private double salary;

    public Employee(int id, String name, String department, String gender, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.gender = gender;
        this.salary = salary;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public String getGender() { return gender; }
    public double getSalary() { return salary; }
}