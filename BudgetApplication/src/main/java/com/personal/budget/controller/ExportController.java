package com.personal.budget.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.personal.budget.export.CsvExport;
import com.personal.budget.service.ExpenseService;

@Controller
public class ExportController {
	
	private final ExpenseService service;
	private final CsvExport export;

	
	public ExportController(ExpenseService service, CsvExport export) {
		this.service = service;
		this.export = export;
	}

	@PostMapping("/export/{id}")
	public String exportForUser(@PathVariable Long id, Model model) throws IOException {
		export.generateCSVAndUploadToS3(id);
		
		return "redirect:/exports/" + id;		
	}
	
	@GetMapping("/exports/{id}")
	public String exportsForUser(@PathVariable Long id, Model model) {
		
		model.addAttribute("files", export.listBucketFiles(id));
		
		return "exports";		
		
	}
}
