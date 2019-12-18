package com.be.jpa.controller;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.Valid;

import com.be.jpa.util.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.be.jpa.model.Employee;
import com.be.jpa.repository.EmployeeDAOService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class EmployeeController {

	@Autowired
	private EmployeeDAOService employeeDaoService;

	@GetMapping("/employees")
	public List<Employee> getAllEmployees() {
		return employeeDaoService.findAll();
	}

	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId) throws Exception {
		Employee employee = employeeDaoService.findById(employeeId);
		return ResponseEntity.ok().body(employee);
	}

	@PostMapping("/employees")
	public Employee createEmployee(@Valid @RequestBody Employee employee) {
		return employeeDaoService.save(employee);
	}

	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId,
			@Valid @RequestBody Employee employeeDetails) throws Exception {
		Employee updatedEmployee = employeeDaoService.update(employeeId, employeeDetails);
		return ResponseEntity.ok(updatedEmployee);
	}

	@DeleteMapping("/employees/{id}")
	public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId) throws Exception {
		employeeDaoService.delete(employeeId);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	@GetMapping("/employees/search")
	public List<Employee> searchEmployees(@RequestParam(value = "param", required = false) String search) {
		List<SearchCriteria> criterias = buildCriteria(search);
		return employeeDaoService.search(criterias);
	}

	@GetMapping("/employees/searchSpec")
	public List<Employee> searchEmployeesSpec(@RequestParam(value = "param", required = false) String search) {
		return employeeDaoService.searchSpec(buildCriteria(search));
	}

	@GetMapping("/employees/searchSpecPage")
	public Page<Employee> searchEmployeesSpecPage(@RequestParam(value = "param", required = false) String search,
												  Pageable pageable) {
		return employeeDaoService.searchSpecPage(buildCriteria(search), pageable);
	}

	private List<SearchCriteria> buildCriteria(String search) {
		List<SearchCriteria> criteria = new ArrayList<>();
		if (!StringUtils.isEmpty(search)) {
			Pattern pattern = Pattern.compile("(\\w+?)([:<>])(\\w+?),");
			Matcher matcher = pattern.matcher(search + ",");
			while (matcher.find()) {
				criteria.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
			}
		}
		return criteria;
	}
}
