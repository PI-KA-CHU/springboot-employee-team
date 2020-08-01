package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.dto.EmployeeRequestDto;
import com.thoughtworks.springbootemployee.entity.Employee;
import com.thoughtworks.springbootemployee.exception.UnknownEmployeeException;
import com.thoughtworks.springbootemployee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(value = "/employees")
    public List<Employee> findAllEmployees() {
        return employeeService.findAllEmployees();
    }

    @GetMapping("/employees/{employeeId}")
    public Employee findEmployeeById(@PathVariable int employeeId) {
        return employeeService.findEmployeeById(employeeId);
    }

    @GetMapping(value = "/employees", params = {"page", "size"})
    public Page<Employee> findEmployeesByPage(@PageableDefault Pageable pageable) {
        return employeeService.getPagedEmployees(pageable);
    }

    @GetMapping(value = "/employees", params = {"gender"})
    public List<Employee> findEmployeesByGender(@RequestParam String gender) {
        return employeeService.findEmployeesByGender(gender);
    }

    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    public void addEmployee(@RequestBody @Valid EmployeeRequestDto employeeRequestDto) {
        employeeService.addEmployee(employeeRequestDto);
    }

    @PutMapping("/employees/{employeeId}")
    public Employee updateEmployee(@PathVariable int employeeId, @RequestBody EmployeeRequestDto employeeRequestDto) {
        return employeeService.updateEmployee(employeeId, employeeRequestDto);
    }

    @DeleteMapping("/employees/{employeeId}")
    public void deleteEmployeeById(@PathVariable int employeeId) {
        employeeService.deleteEmployeeById(employeeId);
    }
}
