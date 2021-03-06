package com.kaan.vaadin.vaadinExample;

import org.springframework.util.StringUtils;

import com.kaan.vaadin.vaadinExample.model.Customer;
import com.kaan.vaadin.vaadinExample.repository.CustomerRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout {

	private final CustomerRepository repo;
	
	final Grid<Customer> grid;
	
	private final CustomerEditor editor;

	final TextField filter;

	private final Button addNewBtn;
	
	public MainView(CustomerRepository repo, CustomerEditor editor) {
		/*
		 * //add(new Button("Click me", e -> Notification.show("Hello user!")));
		 * this.repo = repo; this.grid = new Grid<>(Customer.class); //add(grid);
		 * //listCustomers(); TextField filter = new TextField();
		 * filter.setPlaceholder("Filter by last name");
		 * filter.setValueChangeMode(ValueChangeMode.EAGER);
		 * filter.addValueChangeListener(e -> listCustomers(e.getValue())); add(filter,
		 * grid);
		 */
		
		
		this.repo = repo;
		this.editor = editor;
		this.grid = new Grid<>(Customer.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("New customer", VaadinIcon.PLUS.create());

		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		add(actions, grid, editor);

		grid.setHeight("300px");
		grid.setColumns("id", "firstName", "lastName");
		grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

		filter.setPlaceholder("Filter by last name");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.EAGER);
		filter.addValueChangeListener(e -> listCustomers(e.getValue()));

		// Connect selected Customer to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			editor.editCustomer(e.getValue());
		});

		// Instantiate and edit new Customer the new button is clicked
		addNewBtn.addClickListener(e -> editor.editCustomer(new Customer("", "")));

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listCustomers(filter.getValue());
		});

		// Initialize listing
		listCustomers(null);
		
	}

	public void listCustomers() {
		grid.setItems(repo.findAll());
	}

	public void listCustomers(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems(repo.findAll());
		} else {
			grid.setItems(repo.findByLastNameStartsWithIgnoreCase(filterText));
		}
	}
}
