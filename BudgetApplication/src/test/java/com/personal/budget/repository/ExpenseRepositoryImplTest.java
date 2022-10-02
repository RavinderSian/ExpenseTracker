package com.personal.budget.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
	void test_Save_SavesEntityCorrectly_WhenGivenValidEntity() {
		
		Expense expense = new Expense();
		expense.setUserId(1L);
		expense.setAmount(BigDecimal.valueOf(10));
		expense.setCategory(Category.DATES);
		expense.setDescription("fds");
		expense.setPurchaseDate(Timestamp.valueOf(LocalDateTime.now()));
		
		repository.save(expense);
		
		Expense saved = repository.save(expense);
		
		assertThat(saved.getId(), equalTo(expense.getId()));
		assertThat(saved.getUserId(), equalTo(expense.getUserId()));
		assertThat(saved.getAmount(), equalTo(expense.getAmount()));
		assertThat(saved.getCategory(), equalTo(expense.getCategory()));
		assertThat(saved.getDescription(), equalTo(expense.getDescription()));
		assertThat(saved.getPurchaseDate().toLocalDateTime().getHour(), equalTo(expense.getPurchaseDate().toLocalDateTime().getHour()));
	}
	
	@Test
	void test_Save_ThrowsJdbcSQLIntegrityConstraintViolationException_WhenGivenInvalidEntity() {
		
		Expense expense = new Expense();
		expense.setUserId(1L);
		expense.setCategory(Category.DATES);
		expense.setDescription("fds");
		expense.setPurchaseDate(Timestamp.valueOf(LocalDateTime.now()));
		
		Exception thrown = Assertions.assertThrows(
				Exception.class,
	           () -> {repository.save(expense);
	           });
	
		assertThat(thrown.getMessage().contains("NULL not allowed for column"), equalTo(true));
	}
	
	@Test
	void test_FindById_FindsCorrectExpense_WhenExpenseExistsWithGivenId() {
		
		Expense expense = new Expense();
		expense.setUserId(1L);
		expense.setAmount(BigDecimal.valueOf(10));
		expense.setCategory(Category.DATES);
		expense.setDescription("fds");
		expense.setPurchaseDate(Timestamp.valueOf(LocalDateTime.now()));
		
		Expense saved = repository.save(expense);
		
		Optional<Expense> result = repository.findById(saved.getId());
		
		assertThat(result.get().getId(), equalTo(saved.getId()));
		assertThat(result.get().getUserId(), equalTo(saved.getUserId()));
		assertThat(result.get().getAmount(), equalTo(saved.getAmount()));
		assertThat(result.get().getCategory(), equalTo(saved.getCategory()));
		assertThat(result.get().getDescription(), equalTo(saved.getDescription()));
		assertThat(result.get().getPurchaseDate().toLocalDateTime().getHour(), equalTo(saved.getPurchaseDate().toLocalDateTime().getHour()));

	}
	
	@Test
	void test_FindById_ReturnsEmptyOptional_WhenExpenseDoesNotWithGivenId() {
		assertThat(repository.findById(1L).isEmpty(), equalTo(true));
	}
	
	@Test
	void test_DeleteById_FindsCorrectEntity_WhenEntityExistsWithGivenId() {
		
		Expense expense = new Expense();
		expense.setUserId(1L);
		expense.setAmount(BigDecimal.valueOf(10));
		expense.setCategory(Category.DATES);
		expense.setDescription("fds");
		expense.setPurchaseDate(Timestamp.valueOf(LocalDateTime.now()));
		
		Expense saved = repository.save(expense);
		repository.deleteById(saved.getId());
		
		assertThat(repository.findById(saved.getId()).isEmpty(), equalTo(true));

	}
	
	@Test
	void test_FindAll_ReturnsEmptyList_WhenTableIsEmpty() {
		assertThat(repository.findAll().isEmpty(), equalTo(true));
	}
	
	@Test
	void test_FindAll_ReturnsListOfLength2ExpectedEntries_WhenTableHas2Entries() {
		
		Expense expense = new Expense();
		expense.setUserId(1L);
		expense.setAmount(BigDecimal.valueOf(10));
		expense.setCategory(Category.DATES);
		expense.setDescription("fds");
		expense.setPurchaseDate(Timestamp.valueOf(LocalDateTime.now()));
		
		repository.save(expense);
		
		
		Expense expense2 = new Expense();
		expense2.setUserId(1L);
		expense2.setAmount(BigDecimal.valueOf(10));
		expense2.setCategory(Category.DATES);
		expense2.setDescription("test2");
		expense2.setPurchaseDate(Timestamp.valueOf(LocalDateTime.now()));
		
		repository.save(expense2);
		
		assertThat(repository.findAll().size(), equalTo(2));
		
		
	}
}
