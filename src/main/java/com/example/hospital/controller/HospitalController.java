package com.example.hospital.controller;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.hospital.dto.HospitalDTO;
import com.example.hospital.dto.ServiceResponse;
import com.example.hospital.dto.User;
import com.example.hospital.entity.Department;
import com.example.hospital.service.HospitalService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins ="http://localhost:4200") 
@AllArgsConstructor
public class HospitalController {

	private HospitalService service;

	@PostMapping("/create")
	public ResponseEntity<ServiceResponse> add(@Valid @RequestBody HospitalDTO entity) {
		return new ResponseEntity<>(service.add(entity), HttpStatus.OK);
	}

	@GetMapping("/get/{patientId}/{doctorId}/{deptName}")
	public ResponseEntity<HospitalDTO> getEmpById(@PathVariable int patientId, @PathVariable int doctorId,
			@PathVariable String deptName) {
		return new ResponseEntity<>(service.fetchById(patientId, doctorId, deptName),HttpStatus.OK);
	}

	@PutMapping("/update/{patientId}/{doctorId}/{deptName}")
	public ResponseEntity<ServiceResponse> updateProduct(@PathVariable int patientId, @PathVariable int doctorId,
			@PathVariable String deptName, @Valid @RequestBody HospitalDTO entity) {
		return new ResponseEntity<>(service.update(patientId, doctorId, deptName, entity), HttpStatus.OK);
	}

	@DeleteMapping("/remove/{patientId}/{doctorId}/{deptName}")
	public ResponseEntity<ServiceResponse> delete(@PathVariable int patientId, @PathVariable int doctorId,
			@PathVariable String deptName) {
		return new ResponseEntity<>(service.remove(patientId, doctorId, deptName), HttpStatus.OK);
	}
  
	@GetMapping("/search")
	public ResponseEntity<JSONObject> search(@RequestParam("searchParam") String searchDataJson,
			@RequestParam("iDisplayStart") Integer iDisplayStart,
			@RequestParam("iDisplayLength") Integer iDisplayLength) {
		return new ResponseEntity<>(service.search(searchDataJson, iDisplayStart, iDisplayLength),HttpStatus.OK);                                                                                                    			   
	}
        
	@PostMapping("/getaccesstoken")   
	public ResponseEntity<ServiceResponse> getAccessToken(@RequestBody User user) {
		return new ResponseEntity<>(service.getAccessToken(user),HttpStatus.OK);
	}  
 
	@GetMapping("/dept/getAll")
	public ResponseEntity<List<Department>> getAll() {    
		return ResponseEntity.ok (service.getAll());             
	}
}
