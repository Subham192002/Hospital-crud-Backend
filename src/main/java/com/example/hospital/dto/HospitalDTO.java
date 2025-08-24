package com.example.hospital.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor  
public class HospitalDTO {

	@Positive(message = "PatientID must be a positive integer.")
	private int patientId;

	@Positive(message = "DoctorID must be a positive integer.")
	private int doctorId;

	@NotBlank(message = "Department name cannot be blank.")
	@Size(max = 50, message = "Department name cannot exceed 50 characters.")
	private String deptName;

	@NotBlank(message = "Doctorname cannot be blank.")
	@Size(max = 100, message = "Doctor name cannot exceed 100 characters.")
	private String doctorName;

	@NotBlank(message = "Specialization cannot be blank.")
	@Size(max = 100, message = "Specialization cannot exceed 100 characters.")
	private String specialization;

	@NotEmpty(message = "PatientName cannot be blank.")
	@Size(max = 100, message = "PatientName cannot exceed 100 characters.")
	private String patientName;

	@NotEmpty(message = "Gender cannot be blank.")
	private String gender;

	@NotEmpty(message = "Disease cannot be blank.")
	@Size(max = 50, message = "Disease description cannot exceed 200 characters.")
	private String disease;

	@Min(value = 1000000000L, message = "Contact must be a 10-digit number")
	@Max(value = 9999999999L, message = "Contact must be a 10-digit number")
	private long contactNumber;

	@NotNull(message = "Admit date cannot be null.")
	@PastOrPresent(message = "Admit date cannot be in the future.")
	private LocalDate admitDate;

	private String status;


}
