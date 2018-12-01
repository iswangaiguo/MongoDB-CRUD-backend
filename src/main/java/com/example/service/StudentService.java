package com.example.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.entity.Student;
import com.example.student.db.StudentRepository;

@Service
public class StudentService {
	
	@Autowired
	StudentRepository rs;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	public ResponseEntity addStudent(Student student) {
		int id = student.getId();
		if (rs.existsById(id)) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			rs.save(student);
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
	public ResponseEntity deleteStudent(int id) {
		if (rs.existsById(id)) {
			rs.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public ResponseEntity updateStudent(Student student) {
		if (rs.existsById(student.getId())) {
			rs.save(student);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	public ResponseEntity<?> findOneStudent(int id) {
		if (rs.existsById(id)) {
			return new ResponseEntity<Student>(rs.findById(id).get(),HttpStatus.OK);
		} else {
			return new ResponseEntity<Student>(HttpStatus.NOT_FOUND);
		}
	}
	
	public ResponseEntity<?> findAll(int pageNumber) {
		if (pageNumber < 1) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		List<Student> lists = new ArrayList();
		PageRequest of = PageRequest.of(pageNumber - 1, 10);
		Iterator<Student> iterator = rs.findAll(of).iterator();
		while (iterator.hasNext()) {
			lists.add(iterator.next());	
		}
		return new ResponseEntity<List<Student>>(lists,HttpStatus.OK);
		
	}
	
	public ResponseEntity<?> findAll() {
		List<Student> students = rs.findAll();
		return new ResponseEntity<List<Student>>(students,HttpStatus.OK);
	}
	
	public ResponseEntity<?> query(Map<String, String> map) {
		String name = map.get("name");
		String value = map.get("value");
		List<String> grade = Arrays.asList("大一","大二","大三","大四");
		List<Student> students = null;
		Query query = new Query();
		if (value.equals("")) {
			students = mongoTemplate.findAll(Student.class);
		}else if (name.equals("grade") || name.equals("sex")) {
			if (value.indexOf("-") == -1) {
				query.addCriteria(Criteria.where(name).is(value));
				students = mongoTemplate.find(query, Student.class);
			} else {
				String param1 = value.split("-")[0];
				String param2 = value.split("-")[1];
				int start = grade.indexOf(param1);
				int end = grade.indexOf(param2);
				List<String> subList = grade.subList(start, end + 1);
				query.addCriteria(Criteria.where(name).in(subList));
				students = mongoTemplate.find(query, Student.class);
			}
		} else if (name.equals("id") || name.equals("age")) {
			if (value.indexOf("-") == -1) {
				query.addCriteria(Criteria.where(name).is(Integer.parseInt(value)));
				students = mongoTemplate.find(query, Student.class);
			} else {
				String param1 = value.split("-")[0];
				String param2 = value.split("-")[1];
				if (grade.contains(param1)) {
				} else {
					query.addCriteria(Criteria.where(name).gte(Integer.parseInt(param1)).lte(Integer.parseInt(param2)));
				}
				students = mongoTemplate.find(query, Student.class);
			}
		} else {
			query.addCriteria(Criteria.where(name).regex(value));
			students = mongoTemplate.find(query, Student.class);
		}	
		return new ResponseEntity<List<Student>>(students, HttpStatus.OK);
	}
	
	
}
