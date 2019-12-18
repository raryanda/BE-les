package com.be.jpa.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.be.jpa.util.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.be.jpa.model.Employee;

public class EmployeeDAOServiceImpl implements EmployeeDAOService {

	@Autowired
	private EmployeeRepository repository;

	@Autowired
	EntityManager entityManager;

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

	@Override
	public List<Employee> search(List<SearchCriteria> criterias) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Employee> query = builder.createQuery(Employee.class);
		Root root = query.from(Employee.class);

		Predicate predicate = buildPredicate(root, builder, criterias);
		query.where(predicate);

		return entityManager.createQuery(query).getResultList();
	}

	@Override
	public List<Employee> searchSpec(List<SearchCriteria> criterias) {
		return repository.findAll(buildSpec(criterias));
	}

	@Override
	public Page<Employee> searchSpecPage(List<SearchCriteria> criterias, Pageable pageable) {
		return repository.findAll(buildSpec(criterias), pageable);
	}

	private Predicate buildPredicate(Root root, CriteriaBuilder builder, List<SearchCriteria> criterias) {
		Predicate predicate = builder.conjunction();
		for (SearchCriteria criteria : criterias) {
			if (criteria.getOperation().equalsIgnoreCase(":")) {
				predicate = builder.and(predicate, builder.equal(root.get(criteria.getKey()), criteria.getValue()));
			} else if (criteria.getOperation().equalsIgnoreCase("<")) {
				predicate = builder.and(predicate, builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue()));
			} else if (criteria.getOperation().equalsIgnoreCase(">")) {
				predicate = builder.and(predicate, builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue()));
			}
		}
		return predicate;
	}

	private Specification<Employee> buildSpec(List<SearchCriteria> criterias) {
		return (root, criteriaQuery, criteriaBuilder) ->
			buildPredicate(root, criteriaBuilder, criterias);
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
