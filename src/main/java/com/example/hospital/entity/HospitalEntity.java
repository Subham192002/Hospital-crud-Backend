package com.example.hospital.entity;

import java.time.LocalDate;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="HOSPITAL")
@Getter  
@Builder   
@AllArgsConstructor
@NoArgsConstructor
public class HospitalEntity {

	@EmbeddedId
	HospitalPK id;
	
	private String doctorName;
	private String specialization;
	private String patientName;
	private String gender;
	private String disease;
	private long contactNumber;
	private LocalDate admitDate;
	private String status;

}
