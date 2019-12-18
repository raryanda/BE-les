package com.be.jpa.repository;

import java.util.List;

import com.be.jpa.model.Employee;
import com.be.jpa.util.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeDAOService {

	Employee save(Employee employee);

	void delete(Long employeeId) throws Exception;

	Employee update(Long employeeId, Employee employeeDetails) throws Exception;

	Employee findById(Long employeeId) throws Exception;

	List<Employee> findAll();

	List<Employee> search(List<SearchCriteria> criterias);

	List<Employee> searchSpec(List<SearchCriteria> criterias);

	Page<Employee> searchSpecPage(List<SearchCriteria> criterias, Pageable pageable);
}
