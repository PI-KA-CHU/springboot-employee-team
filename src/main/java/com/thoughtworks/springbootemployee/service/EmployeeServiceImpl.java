package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.dto.EmployeeRequestDto;
import com.thoughtworks.springbootemployee.entity.Company;
import com.thoughtworks.springbootemployee.entity.Employee;
import com.thoughtworks.springbootemployee.exception.UnknownCompanyException;
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
    private final CompanyService companyService;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, CompanyService companyService) {
        this.employeeRepository = employeeRepository;
        this.companyService = companyService;
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
        Company company = companyService.findCompanyById(employeeRequestDto.getCompany_id());
        employee.setCompany(company);
        employee.setName(employeeRequestDto.getName());
        employee.setGender(employeeRequestDto.getGender());
        employee.setAge(employeeRequestDto.getAge());
    }

    @Override
    public Employee updateEmployee(int employeeId, EmployeeRequestDto employeeRequestDto) {
        Employee employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(() -> new UnknownEmployeeException(employeeId,"EmployeeId id error! Not found!"));
        toEmployeeEntity(employeeRequestDto, employee);
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployeeById(int employeeId) {
        throwExceptionIfEmployeeNotExit(employeeId);
        employeeRepository.deleteById(employeeId);
    }

    @Override
    public Page<Employee> getPagedEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Override
    public List<Employee> findEmployeesByGender(String gender) {
        return employeeRepository.findByGender(gender);
    }

    private void throwExceptionIfEmployeeNotExit(int employeeId) {
        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new UnknownCompanyException(employeeId,"EmployeeId id error! Not found!"));
    }
}
