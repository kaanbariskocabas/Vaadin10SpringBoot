package com.kaan.vaadin.vaadinExample.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaan.vaadin.vaadinExample.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	List<Customer> findByLastNameStartsWithIgnoreCase(String lastName);
}
