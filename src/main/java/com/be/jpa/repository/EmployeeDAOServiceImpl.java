package com.be.jpa.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.be.jpa.model.Employee;

public class EmployeeDAOServiceImpl implements EmployeeDAOService {

	@Autowired
	private EmployeeRepository repository;

	@Override
	public List<Employee> findAll() {
		return repository.findAll();
	}

	@Override
	public Employee findById(Long employeeId) throws Exception {
		return repository.findById(employeeId)
				.orElseThrow(() -> new Exception("Employee not found for this id :: " + employeeId));
	}

	@Override
	@Transactional
	public Employee update(Long employeeId, Employee employeeDetails) throws Exception {
		Employee employee = findById(employeeId);
		employee.setEmail(employeeDetails.getEmail());
		employee.setLastname(employeeDetails.getLastname());
		employee.setFirstname(employeeDetails.getFirstname());
		return repository.save(employee);
	}

	@Override
	@Transactional
	public Employee save(Employee employee) {
		return repository.save(employee);
	}

	@Override
	public void delete(Long employeeId) throws Exception {
		Employee employee = findById(employeeId);
		repository.delete(employee);
	}

	// when configure Java API
	private void configJpa() {
		Map<String, String> properties = new HashMap<>();
		properties.put("javax.persistence.jdbc.driver", "oracle.jdbc.OracleDriver");
		properties.put("javax.persistence.jdbc.url", "jdbc:oracle:thin:@localhost:1521:local");
		properties.put("javax.persistence.jdbc.user", "root");
		properties.put("javax.persistence.jdbc.password", "password");

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Model", properties);

		EntityManager em = emf.createEntityManager();

		try {
			// begin
			em.getTransaction().begin();

			Employee employee = new Employee("Ujang", "Ujang", "ujang@btpn.com");

			em.persist(employee);

			// commit
			em.getTransaction().commit();

		} finally {

			// rollback
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			em.close();
			emf.close();
		}
	}
}
