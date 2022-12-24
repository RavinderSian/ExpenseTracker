package com.personal.budget.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

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
		expense.setCategory("Dates");
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
		expense.setCategory("Dates");
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
		expense.setCategory("Dates");
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
	void test_FindByUserId_ReturnsEmptyOptional_WhenExpenseDoesNotWithGivenId() {
		assertThat(repository.findById(1L).isEmpty(), equalTo(true));
	}
	
	@Test
	void test_FindByUserId_FindsCorrectExpense_WhenExpenseExistsWithGivenId() {
		
		Expense expense = new Expense();
		expense.setUserId(1L);
		expense.setAmount(BigDecimal.valueOf(10));
		expense.setCategory("Dates");
		expense.setDescription("fds");
		expense.setPurchaseDate(Timestamp.valueOf(LocalDateTime.now()));
		
		Expense expense2 = new Expense();
		expense2.setUserId(1L);
		expense2.setAmount(BigDecimal.valueOf(10));
		expense2.setCategory("Dates");
		expense2.setDescription("fdfefsds");
		expense2.setPurchaseDate(Timestamp.valueOf(LocalDateTime.now()));
		
		Expense saved = repository.save(expense);
		Expense saved2 = repository.save(expense2);
		
		List<Expense> result = repository.findByUserId(saved.getUserId());
		
		assertThat(result.size(), equalTo(2));
		
		assertThat(result.get(0).getId(), equalTo(saved.getId()));
		assertThat(result.get(0).getUserId(), equalTo(saved.getUserId()));
		assertThat(result.get(0).getAmount(), equalTo(saved.getAmount()));
		assertThat(result.get(0).getCategory(), equalTo(saved.getCategory()));
		assertThat(result.get(0).getDescription(), equalTo(saved.getDescription()));

		assertThat(result.get(1).getId(), equalTo(saved2.getId()));
		assertThat(result.get(1).getUserId(), equalTo(saved2.getUserId()));
		assertThat(result.get(1).getAmount(), equalTo(saved2.getAmount()));
		assertThat(result.get(1).getCategory(), equalTo(saved2.getCategory()));
		assertThat(result.get(1).getDescription(), equalTo(saved2.getDescription()));
		
	}
	
	@Test
	void test_FindById_ReturnsEmptyOptional_WhenExpenseDoesNotWithGivenId() {
		assertThat(repository.findByUserId(1L).size(), equalTo(0));
	}
	
	@Test
	void test_DeleteById_FindsCorrectEntity_WhenEntityExistsWithGivenId() {
		
		Expense expense = new Expense();
		expense.setUserId(1L);
		expense.setAmount(BigDecimal.valueOf(10));
		expense.setCategory("Dates");
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
		expense.setCategory("Dates");
		expense.setDescription("fds");
		expense.setPurchaseDate(Timestamp.valueOf(LocalDateTime.now()));
		
		repository.save(expense);
		
		Expense expense2 = new Expense();
		expense2.setUserId(1L);
		expense2.setAmount(BigDecimal.valueOf(10));
		expense2.setCategory("Dates");
		expense2.setDescription("test2");
		expense2.setPurchaseDate(Timestamp.valueOf(LocalDateTime.now()));
		
		repository.save(expense2);
		
		assertThat(repository.findAll().size(), equalTo(2));
		
	}
	
	@Test
	void test_FindExpensesByYear_ReturnsListOfLength2ExpectedEntries_WhenTableHas2EntriesForGivenYear() {
		
		Expense expense = new Expense();
		expense.setUserId(1L);
		expense.setAmount(BigDecimal.valueOf(10));
		expense.setCategory("Dates");
		expense.setDescription("fds");
		expense.setPurchaseDate(Timestamp.valueOf(LocalDateTime.now()));
		
		repository.save(expense);
		
		Expense expense2 = new Expense();
		expense2.setUserId(1L);
		expense2.setAmount(BigDecimal.valueOf(10));
		expense2.setCategory("Dates");
		expense2.setDescription("test2");
		expense2.setPurchaseDate(Timestamp.valueOf(LocalDateTime.now()));
		
		repository.save(expense2);
		
		List<Expense> expenses = repository.findExpensesByYear(LocalDate.now().getYear());
		
		assertThat(expenses.size(), equalTo(2));
		
	}
	
	@Test
	void test_FindExpensesByYear_ReturnsListOfLength1_WhenTableHasEntryThisYear1JanMidnight() {
		
		Expense expense = new Expense();
		expense.setUserId(1L);
		expense.setAmount(BigDecimal.valueOf(10));
		expense.setCategory("Dates");
		expense.setDescription("fds");
		expense.setPurchaseDate(Timestamp.valueOf(LocalDateTime.of(LocalDate.now().getYear(), 1, 1, 0, 0)));
		
		repository.save(expense);
		
		List<Expense> expenses = repository.findExpensesByYear(LocalDate.now().getYear());
		
		assertThat(expenses.size(), equalTo(1));
		
	}
	
	@Test
	void test_FindExpensesByYear_ReturnsListOfLength0_WhenTableHasEntryNextYear1JanMidnight() {
		
		Expense expense = new Expense();
		expense.setUserId(1L);
		expense.setAmount(BigDecimal.valueOf(10));
		expense.setCategory("Dates");
		expense.setDescription("fds");
		expense.setPurchaseDate(Timestamp.valueOf(LocalDateTime.of(LocalDate.now().getYear()+1, 1, 1, 0, 0)));
		
		repository.save(expense);
		
		List<Expense> expenses = repository.findExpensesByYear(LocalDate.now().getYear());
		
		assertThat(expenses.size(), equalTo(0));
		
	}
	
	@Test
	void test_FindExpensesByYear_ReturnsListOfLength1_WhenTableHasEntryThisYear31DecMidnight() {
		
		Expense expense = new Expense();
		expense.setUserId(1L);
		expense.setAmount(BigDecimal.valueOf(10));
		expense.setCategory("Dates");
		expense.setDescription("fds");
		expense.setPurchaseDate(Timestamp.valueOf(LocalDateTime.of(LocalDate.now().getYear(), 12, 31, 23, 59)));
		
		repository.save(expense);
		
		List<Expense> expenses = repository.findExpensesByYear(LocalDate.now().getYear());
		
		assertThat(expenses.size(), equalTo(1));
		
	}
	
	@Test
	void test_FindExpensesByYear_ReturnsListOfLength0_WhenTableHasEntriesForNextYear() {
		
		Expense expense = new Expense();
		expense.setUserId(1L);
		expense.setAmount(BigDecimal.valueOf(10));
		expense.setCategory("Dates");
		expense.setDescription("fds");
		expense.setPurchaseDate(Timestamp.valueOf(LocalDateTime.of(LocalDate.now().getYear()+1, 1, 1, 0, 0)));
		
		repository.save(expense);
		
		Expense expense2 = new Expense();
		expense2.setUserId(1L);
		expense2.setAmount(BigDecimal.valueOf(10));
		expense2.setCategory("Dates");
		expense2.setDescription("test2");
		expense2.setPurchaseDate(Timestamp.valueOf(LocalDateTime.of(LocalDate.now().getYear()+1, 1, 1, 0, 0)));
		
		repository.save(expense2);
		
		List<Expense> expenses = repository.findExpensesByYear(LocalDate.now().getYear());
		
		assertThat(expenses.size(), equalTo(0));
		
	}
	
}
