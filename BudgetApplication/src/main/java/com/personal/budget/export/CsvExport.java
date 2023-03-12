package com.personal.budget.export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.opencsv.CSVWriter;
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

	private CSVWriter buildCSVWriter(OutputStreamWriter streamWriter) {
	    return new CSVWriter(streamWriter, ',', Character.MIN_VALUE, '"', System.lineSeparator());
	}
	
	public List<S3ObjectSummary> listBucketFiles(Long userId) {
		ObjectListing bucketListing = getS3().listObjects("budget-app-spreadsheets");
		
		return bucketListing.getObjectSummaries();
	}

	public void writeRecords(List<String[]> lines, Long userId) throws IOException {
	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    OutputStreamWriter streamWriter = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
	    try (CSVWriter writer = buildCSVWriter(streamWriter)) {
	        writer.writeAll(lines);
            writer.flush();
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(stream.toByteArray().length);
            getS3().putObject("budget-app-spreadsheets", userId + "/" + ZonedDateTime.now().getYear()
            		+ "/" + ZonedDateTime.now().getMonthValue() + "/" + ZonedDateTime.now().getDayOfMonth()
            		+ "/" + ZonedDateTime.now().getMinute()
            		 + "/testing.csv", 
            		new ByteArrayInputStream(stream.toByteArray()), meta);
        
	    }
	}
	
	public void generateCSVAndUploadToS3(Long userId) throws IOException {
		
		List<Expense> expenseList = service.findByUserId(userId);

        List<String[]> listToUse = new ArrayList<>();
        
        listToUse.add(new String[] { "id", "Purchase Date", "Amount", "Category", "Description"});
        
        expenseList.forEach(expense -> 
        			listToUse.add(new String[] {expense.getId().toString(), 
        					expense.getPurchaseDate().toString(), expense.getAmount().toString(),
        					expense.getCategory(), expense.getDescription()}));
        listBucketFiles(userId);
        this.writeRecords(listToUse, userId);

	}
	
    private AmazonS3 getS3() {
        return AmazonS3ClientBuilder.standard()
                                    .withCredentials(new ProfileCredentialsProvider())
                                    .withRegion(Regions.EU_WEST_2)
                                    .build();
    }
    
	//This writes locally
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

}
