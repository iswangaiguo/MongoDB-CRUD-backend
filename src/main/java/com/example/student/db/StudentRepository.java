package com.example.student.db;

import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.entity.Student;

public interface StudentRepository extends MongoRepository<Student, Integer>{
	
	List<Student> findAll(Sort sort);
}
