package com.personal.budget.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.personal.budget.model.User;

@SpringBootTest
@AutoConfigureTestDatabase
class UserRepositoryImplTest {
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
    @Autowired
	UserRepository repository;

    @BeforeEach
    void createTable() {
    	jdbcTemplate.execute("CREATE TABLE USERS ( ID bigint NOT NULL PRIMARY KEY AUTO_INCREMENT, "
    			+ "EMAIL varchar(50) NOT NULL, USER_ID bigint NOT NULL,"
    			+ "USERNAME varchar(50) NOT NULL, PASSWORD varchar(50) NOT NULL);");
    }
	
    @AfterEach
    void deleteTable() {
    	jdbcTemplate.execute("DROP TABLE IF EXISTS USERS");
    }

	@Test
	void test_Save_SavesUserCorrectly_WhenGivenValidUser() {
		
		User user = new User();
		user.setId(1L);
		user.setUsername("test");
		user.setPassword("test");
		user.setEmail("test@gmail.com");
		
		repository.save(user);
	}

}
