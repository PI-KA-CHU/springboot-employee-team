package com.thoughtworks.springbootemployee.dto;

//TODO
import com.thoughtworks.springbootemployee.entity.Company;

public class EmployeeRequestDto {

    public EmployeeRequestDto() {
    }

    public EmployeeRequestDto(Integer age, String name, String gender, Integer company_id) {
        this.age = age;
        this.name = name;
        this.gender = gender;
        this.company_id = company_id;
    }

    private Integer age;

    private String name;

    private String gender;

    private Integer company_id;


    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Integer company_id) {
        this.company_id = company_id;
    }
}

