package com.thoughtworks.springbootemployee.integrationtest;

import com.thoughtworks.springbootemployee.entity.Company;
import com.thoughtworks.springbootemployee.entity.Employee;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @BeforeEach
    void addCompanyInDb(){
        saveCompanyByName("oocl");
    }
    
    @AfterEach
    void tearDown() {
        employeeRepository.deleteAll();
    }

    @Test
    void should_return_a_employee_when_add_employee_given_a_employee() throws Exception {
        
        int companyId = companyRepository.findAll().get(0).getCompanyId();
        String employeeJsonStr = getEmployeeJsonString(companyId, "male");
        mockMvc.perform(post("/employees/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJsonStr))
                .andExpect(status().isCreated());

        List<Employee> employeeQueried = employeeRepository.findAll();
        Assertions.assertEquals(1, employeeQueried.size());
    }


    @Test
    void should_return_ok_when_get_employees_given_employee_id_db() throws Exception {
        Company company = companyRepository.findAll().get(0);

        saveEmployee(company, "Benjamin");

        int employeeId = employeeRepository.findAll().get(0).getId();

        mockMvc.perform(get("/employees/" + employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Benjamin"));
    }

    @Test
    void should_return_2_employees_when_get_employees_given_2_employee_id_db() throws Exception {
        Company company = companyRepository.findAll().get(0);

        saveEmployee(company, "Benjamin");
        saveEmployee(company, "LeBron");


        mockMvc.perform(get("/employees/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void should_return_2_employee_when_get_employee_page_given_page_1_size_2() throws Exception {
        Integer page = 1;
        Integer size = 2;
        Company company = companyRepository.findAll().get(0);
        saveEmployee(company, "Benjamin");
        saveEmployee(company, "LeBron");
        saveEmployee(company, "Jim");

        mockMvc.perform(get("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void should_return_male_employees_when_get_employees_by_gender_given_1_male_employee_and_one_female_employee_and_gender_male() throws Exception {
        String gender = "male";
        Company company = companyRepository.findAll().get(0);
        saveEmployee(company, "LeBron", "male");
        saveEmployee(company, "Ellie", "female");

        mockMvc.perform(get("/employees")
                .param("gender", gender))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].gender").value(gender));
    }

    @Test
    void should_return_new_name_employee_when_update_employee_given_employee_in_db_and_new_employee() throws Exception {
        Company company = companyRepository.findAll().get(0);

        saveEmployee(company, "LeBron");

        int employeeId = employeeRepository.findAll().get(0).getId();

        String employeeJsonStr = getEmployeeJsonString(employeeId, "male");

        mockMvc.perform(put("/employees/" + employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJsonStr))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_none_when_delete_employee_given_1_employee_in_db_and_employee_id() throws Exception {
        Company company = companyRepository.findAll().get(0);
        saveEmployee(company, "Benjamin");

        int employeeId = employeeRepository.findAll().get(0).getId();
        mockMvc.perform(delete("/employees/" + employeeId))
                .andExpect(status().isOk());
        List<Employee> employees = employeeRepository.findAll();
        Assertions.assertEquals(0, employees.size());
    }

    @Test
    void should_return_not_found_when_get_employee_given_employee_id_not_exist() throws Exception {
        int employeeId = 1;

        mockMvc.perform(get("/employees/" + employeeId))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_not_found_when_delete_employee_given_employee_id_not_exist() throws Exception {
        int employeeId = 1;

        mockMvc.perform(delete("/employees/" + employeeId))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_not_found_when_update_employee_given_employee_id_not_exist_and_new_employee() throws Exception {
        int employeeId = 1;

        String gender = "Female";

        String employeeJsonStr = getEmployeeJsonString(employeeId, gender);

        mockMvc.perform(put("/employees/" + employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJsonStr))
                .andExpect(status().isNotFound());
    }

    private String getEmployeeJsonString(int employeeId, String gender) {
        return "{\n" +
                "      \"name\": \"Xiaohong\",\n" +
                "      \"age\": 19,\n" +
                "      \"gender\": \"" + gender + "\",\n" +
                "      \"company_id\": \"" + employeeId + "\"  \n" +
                "    }";
    }

    private void saveEmployee(Company company, String name) {
        Employee employee = new Employee();
        employee.setName(name);
        employee.setCompany(company);
        employeeRepository.save(employee);
    }

    private void saveEmployee(Company company, String name, String gender) {
        Employee employee = new Employee();
        employee.setName(name);
        employee.setGender(gender);
        employee.setCompany(company);
        employeeRepository.save(employee);
    }

    private void saveCompanyByName(String companyName) {
        Company company = new Company();
        company.setName(companyName);
        companyRepository.save(company);

    }

}
