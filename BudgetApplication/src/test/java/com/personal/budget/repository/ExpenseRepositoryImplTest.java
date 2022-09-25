package com.personal.budget.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.personal.budget.model.Category;
import com.personal.budget.model.Expense;

@SpringBootTest
@AutoConfigureTestDatabase
class ExpenseRepositoryImplTest {
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
    @Autowired
	ExpenseRepository repository;
    
    @BeforeEach
    void createTable() {
    	jdbcTemplate.execute("CREATE TABLE EXPENSES ( ID bigint NOT NULL PRIMARY KEY AUTO_INCREMENT, "
    			+ "CATEGORY varchar(50) NOT NULL,"
    			+ "USER_ID bigint NOT NULL,"
    			+ "AMOUNT decimal NOT NULL,"
    			+ "DESCRIPTION varchar(50) NOT NULL,"
    			+ "PURCHASE_DATE timestamp NOT NULL);");
    }
	
    @AfterEach
    void deleteTable() {
    	jdbcTemplate.execute("DROP TABLE IF EXISTS EXPENSES");
    }

	@Test
	void test_Save_SavesExpenseCorrectly_WhenGivenValidExpense() {
		
		Expense expense = new Expense();
		expense.setId(1L);
		expense.setUserId(1L);
		expense.setAmount(BigDecimal.valueOf(10));
		expense.setCategory(Category.DATES);
		expense.setDescription("fds");
		expense.setPurchaseDate(Timestamp.valueOf(LocalDateTime.now()));
		
		repository.save(expense);
	}

}
