package com.be.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.be.jpa.repository.EmployeeDAOService;
import com.be.jpa.repository.EmployeeDAOServiceImpl;

@Configuration
public class DaoSpringConfig {

	@Bean
	public EmployeeDAOService employeeDaoService() {
		return new EmployeeDAOServiceImpl();
	}

}
