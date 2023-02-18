package com.personal.budget.export;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.personal.budget.model.Expense;
import com.personal.budget.service.ExpenseService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CsvExport {
	
	private ExpenseService service;
	
	public CsvExport(ExpenseService service) {
		this.service = service;
	}



	public void generateCSV(Long userId) throws IOException {
//
//        // name of generated csv
//        final String CSV_LOCATION = "Expenses.csv ";
//  
//        try {
//  
//            // Creating writer class to generate
//            // csv file
//            FileWriter writer = new FileWriter(CSV_LOCATION);
//  
//            // create a list of employee
//            List<Expense> expenseList = service.findByUserId(userId);
//  
//            // Create Mapping Strategy to arrange the 
//            // column name in order
//            HeaderColumnNameMappingStrategy<Expense> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
//           
//            mappingStrategy.setType(Expense.class);
//  
////            // Arrange column name as provided in below array.
////            String[] columns = new String[] 
////                    { "id", "userId", "category", "amount", "description", "purchaseDate" };
////            mappingStrategy.setco
////  
//            // Creating StatefulBeanToCsv object
//            StatefulBeanToCsvBuilder<Expense> builder = new StatefulBeanToCsvBuilder<>(writer);
//            
//            StatefulBeanToCsv<Expense> beanWriter = builder.withMappingStrategy(mappingStrategy).build();
//  
//            // Write list to StatefulBeanToCsv object
//            beanWriter.write(expenseList);
//  
//            // closing the writer object
//            writer.close();
//        }
//        catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
    }
	
	public void generateCSVAndUploadToS3(Long userId) throws IOException {
		
	}
	


}
