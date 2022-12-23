package com.personal.budget.controller;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.personal.budget.repository.ExpenseRepository;
import com.personal.budget.service.ExpenseService;
import com.personal.budget.service.ExpenseServiceImpl;

@WebMvcTest(ExpenseJsonController.class)
class ExpenseJsonControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	private ExpenseJsonController controller;
	
	@MockBean
	private ExpenseService service;
	
	@MockBean
	private ExpenseRepository expenseRepository;
	
	@BeforeEach
	void setUp() throws Exception {
		controller = new ExpenseJsonController(new ExpenseServiceImpl(expenseRepository));
	}
	
	@WithMockUser(username = "test", password = "testing")
	@Test
	void test_DeleteRequest_DeletesEntity_WhenGivenValidEntity() throws Exception {
		
		mockMvc.perform(get("/delete/1"))
		.andExpect(status().isOk());
		
	}

}
