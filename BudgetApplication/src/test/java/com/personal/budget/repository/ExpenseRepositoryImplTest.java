package com.personal.budget.repository;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

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
    			+ "CATEGORY varchar(50) NOT NULL, LAST_NAME varchar(50) NOT NULL);");
    }
	
    @AfterEach
    void deleteTable() {
    	jdbcTemplate.execute("DROP TABLE IF EXISTS EXPENSES");
    }

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void test() {
		fail("Not yet implemented");
	}

}
