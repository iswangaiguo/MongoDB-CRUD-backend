package com.example.controller;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Student;
import com.example.service.StudentService;

@RestController
@RequestMapping("/student")
public class StudentController {

	private static final Log log = LogFactory.getLog(StudentController.class);
	
	@Autowired
	StudentService service;
	
	@PutMapping
	public ResponseEntity<?> updateStudent(@RequestBody Student student) {
		System.out.println(student);
		return service.updateStudent(student);
	}
	
	@GetMapping("/{id:\\d+}")
	public ResponseEntity<?> getStudent(@PathVariable int id) {
		return service.findOneStudent(id);
	}
	
	@PostMapping
	public ResponseEntity<?> addStudent(@RequestBody Student student) {
		return service.addStudent(student);
	}
	
	@DeleteMapping("/{id:\\d+}")
	public ResponseEntity<?> deleteStudent(@PathVariable int id) {
		return service.deleteStudent(id);
	}
	
	@GetMapping()
	public ResponseEntity<?> getAllStudent() {
		return service.findAll();
	}
	
	@GetMapping("/page/{pageNumber}")
	public ResponseEntity<?> getAllStudent(@PathVariable int pageNumber) {
		return service.findAll(pageNumber);
	}
	
	@PostMapping("/query")
	public ResponseEntity<?> getQueryResult(@RequestBody Map<String, String> map) {
		return service.query(map);
	}
	
	
}
