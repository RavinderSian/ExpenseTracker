package com.personal.budget.export;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.personal.budget.model.Expense;
import com.personal.budget.service.ExpenseService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvExport {
	
	@Autowired
	private ExpenseService service;

	public void generateCSV(Long userId) throws IOException {

        // name of generated csv
        final String CSV_LOCATION = "Expenses.csv ";
  
        try {
  
            // Creating writer class to generate
            // csv file
            FileWriter writer = new FileWriter(CSV_LOCATION);
  
            // create a list of employee
            List<Expense> expenseList = service.findByUserId(userId);
  
            // Create Mapping Strategy to arrange the 
            // column name in order
            ColumnPositionMappingStrategy<Expense> mappingStrategy = new ColumnPositionMappingStrategy<>();
           
            mappingStrategy.setType(Expense.class);
  
            // Arrange column name as provided in below array.
            String[] columns = new String[] 
                    { "id", "user_id", "category", "amount", "description", "purchaseDate" };
            mappingStrategy.setColumnMapping(columns);
  
            // Creating StatefulBeanToCsv object
            StatefulBeanToCsvBuilder<Expense> builder = new StatefulBeanToCsvBuilder<>(writer);
            
            StatefulBeanToCsv<Expense> beanWriter = builder.withMappingStrategy(mappingStrategy).build();
  
            // Write list to StatefulBeanToCsv object
            beanWriter.write(expenseList);
  
            // closing the writer object
            writer.close();
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
    }


}
