package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.dto.EmployeeRequestDto;
import com.thoughtworks.springbootemployee.entity.Company;
import com.thoughtworks.springbootemployee.entity.Employee;
import com.thoughtworks.springbootemployee.exception.UnknownEmployeeException;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, CompanyRepository companyRepository) {
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findEmployeeById(int employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new UnknownEmployeeException(employeeId, "Find employee by id failed! Not found!"));
    }

    @Override
    public void addEmployee(EmployeeRequestDto employeeRequestDto) {
        Employee employee = new Employee();
        toEmployeeEntity(employeeRequestDto, employee);
        employeeRepository.save(employee);
    }

    private void toEmployeeEntity(EmployeeRequestDto employeeRequestDto, Employee employee) {
        Company company = companyRepository.findById(employeeRequestDto.getCompany_id()).get();
        employee.setCompany(company);
        employee.setName(employeeRequestDto.getName());
        employee.setGender(employeeRequestDto.getGender());
        employee.setAge(employeeRequestDto.getAge());
    }

    @Override
    public Employee updateEmployee(int employeeId, EmployeeRequestDto employeeRequestDto) {
        Employee employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(() -> new UnknownEmployeeException(employeeId,"Update employee by id failed! Not found!"));
        toEmployeeEntity(employeeRequestDto, employee);
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployeeById(int employeeId) {
        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new UnknownEmployeeException(employeeId, "Delete employee by id failed! Not found!"));
        employeeRepository.deleteById(employeeId);
    }

    @Override
    public Page<Employee> getPagedEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    //TODO
    @Override
    public List<Employee> findEmployeesByGender(String gender) {
        return employeeRepository.findAll()
                .stream()
                .filter(employee -> employee.getGender().equals(gender))
                .collect(Collectors.toList());
    }
}
