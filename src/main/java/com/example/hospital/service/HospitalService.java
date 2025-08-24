package com.example.hospital.service;

import java.util.List;

import org.json.simple.JSONObject;

import com.example.hospital.dto.HospitalDTO;
import com.example.hospital.dto.ServiceResponse;
import com.example.hospital.dto.User;
import com.example.hospital.entity.Department;

public interface HospitalService {

	ServiceResponse add(HospitalDTO entity);

	HospitalDTO fetchById(int patientId, int doctorId, String deptName);

	ServiceResponse update(int patientId, int doctorId, String deptName, HospitalDTO entity);

	ServiceResponse remove(int patientId, int doctorId, String deptName);

	JSONObject search(String searchDataJson, Integer iDisplayStart, Integer iDisplayLength);

	List<Department> getAll();

	ServiceResponse getAccessToken(User user);

}
