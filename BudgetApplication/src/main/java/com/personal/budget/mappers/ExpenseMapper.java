package com.personal.budget.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.personal.budget.model.Category;
import com.personal.budget.model.Expense;

public class ExpenseMapper implements RowMapper<Expense> {

	@Override
	public Expense mapRow(ResultSet rs, int rowNum) throws SQLException {
		Expense ingredient = new Expense();
		ingredient.setId(rs.getLong("id"));
		ingredient.setUserId(rs.getLong("user_id"));
		ingredient.setAmount(rs.getBigDecimal("amount"));
		ingredient.setCategory(Category.valueOf(rs.getString("category")));
		ingredient.setDescription(rs.getString("description"));
		ingredient.setPurchaseDate(rs.getTimestamp(("purchase_date")));
		
		return ingredient;
	}

}
