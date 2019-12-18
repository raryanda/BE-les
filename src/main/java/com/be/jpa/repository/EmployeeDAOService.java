package com.be.jpa.repository;

import java.util.List;

import com.be.jpa.model.Employee;

public interface EmployeeDAOService {

	Employee save(Employee employee);

	void delete(Long employeeId) throws Exception;

	Employee update(Long employeeId, Employee employeeDetails) throws Exception;

	Employee findById(Long employeeId) throws Exception;

	List<Employee> findAll();

}
