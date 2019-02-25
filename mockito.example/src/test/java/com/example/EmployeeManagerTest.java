package com.example;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

public class EmployeeManagerTest {

	private EmployeeManager employeeManager;

	private EmployeeRepository employeeRepository;

	private BankService bankService;

	@Before
	public void setup() {
		employeeRepository = mock(EmployeeRepository.class);
		bankService = mock(BankService.class);
		employeeManager = new EmployeeManager(employeeRepository, bankService);
	}

	@Test
	public void testPayEmployeesWhenNoEmployeesArePresent() {
		when(employeeRepository.findAll())
			.thenReturn(emptyList());
		assertThat(employeeManager.payEmployees())
			.isEqualTo(0);
	}

	@Test
	public void testPayEmployeesWhenOneEmployeeIsPresent() {
		when(employeeRepository.findAll())
			.thenReturn(asList(new Employee("1", 1000)));
		assertThat(employeeManager.payEmployees()).isEqualTo(1);
		verify(bankService).pay("1", 1000);
	}

	@Test
	public void testPayEmployeesWhenSeveralEmployeesArePresent() {
		when(employeeRepository.findAll())
			.thenReturn(asList(
					new Employee("1", 1000),
					new Employee("2", 2000)));
		assertThat(employeeManager.payEmployees()).isEqualTo(2);
		verify(bankService).pay("2", 2000);
		verify(bankService).pay("1", 1000);
		verifyNoMoreInteractions(bankService);
	}

	@Test
	public void testPayEmployeesInOrderWhenSeveralEmployeeArePresent() {
		// an example of invocation order verification
		when(employeeRepository.findAll())
				.thenReturn(asList(
						new Employee("1", 1000),
						new Employee("2", 2000)));
		assertThat(employeeManager.payEmployees()).isEqualTo(2);
		InOrder inOrder = inOrder(bankService);
		inOrder.verify(bankService).pay("1", 1000);
		inOrder.verify(bankService).pay("2", 2000);
		verifyNoMoreInteractions(bankService);
	}
}