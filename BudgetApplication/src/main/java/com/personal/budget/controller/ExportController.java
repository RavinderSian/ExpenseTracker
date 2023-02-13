package com.personal.budget.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.personal.budget.export.CsvExport;
import com.personal.budget.service.ExpenseService;

@RestController
public class ExportController {
	
	private final ExpenseService service;
	private final CsvExport export;

	
	public ExportController(ExpenseService service, CsvExport export) {
		this.service = service;
		this.export = export;
	}

	@GetMapping("/export/{id}")
	public ResponseEntity<?> exportForUser(@PathVariable Long id) throws IOException {
		export.generateCSV(id);
		
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
}
