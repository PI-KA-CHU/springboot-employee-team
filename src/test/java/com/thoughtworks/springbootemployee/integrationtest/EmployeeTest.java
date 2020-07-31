package com.thoughtworks.springbootemployee.integrationtest;

import com.thoughtworks.springbootemployee.entity.Company;
import com.thoughtworks.springbootemployee.entity.Employee;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @AfterEach
    void tearDown() {
        employeeRepository.deleteAll();
    }

    @Test
    void should_return_a_employee_when_add_employee_given_a_employee() throws Exception {
        String employeeJsonStr = "{\n" +
                "      \"name\": \"Xiaoming\",\n" +
                "      \"age\": 20,\n" +
                "      \"gender\": \"Male\",\n" +
                "      \"company_id\": 1\n" +
                "}";
        saveCompanyByName("oocl");

        mockMvc.perform(post("/employees/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJsonStr))
                .andExpect(status().isCreated());

        List<Employee> employeeQueried = employeeRepository.findAll();
        Assertions.assertEquals(1, employeeQueried.size());
    }

    @Test
    void should_return_ok_when_get_employees_given_employee_id_db() throws Exception {
        Company company = new Company();
        company.setName("oocl");
        companyRepository.save(company);

        saveEmployee(company, "Benjamin");

        int employeeId = employeeRepository.findAll().get(0).getId();

        mockMvc.perform(get("/employees/" + employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Benjamin"));
    }

    @Test
    void should_return_2_employees_when_get_employees_given_2_employee_id_db() throws Exception {
        Company company = new Company();
        company.setName("oocl");
        companyRepository.save(company);

        saveEmployee(company, "Benjamin");
        saveEmployee(company, "LeBron");


        mockMvc.perform(get("/employees/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    private void saveEmployee(Company company, String name) {
        Employee employee = new Employee();
        employee.setName(name);
        employee.setCompany(company);
        employeeRepository.save(employee);
    }


    private void saveCompanyByName(String companyName) {
        Company company = new Company();
        company.setName(companyName);
        companyRepository.save(company);
    }

}
