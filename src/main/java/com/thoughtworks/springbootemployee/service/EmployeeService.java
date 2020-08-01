package com.thoughtworks.springbootemployee.service;


import com.thoughtworks.springbootemployee.dto.EmployeeRequestDto;
import com.thoughtworks.springbootemployee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> findAllEmployees();

    Employee findEmployeeById(int employeeId);

    void addEmployee(EmployeeRequestDto employeeRequestDto);

    Employee updateEmployee(int employeeId, EmployeeRequestDto employeeRequestDto);

    void deleteEmployeeById(int employeeId);

    Page<Employee> getPagedEmployees(Pageable pageable);

    List<Employee> findEmployeesByGender(String gender);
}
