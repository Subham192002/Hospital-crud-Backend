package com.example.hospital.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class HospitalPK implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int patientId;
	private int doctorId;
	private String deptName;
	

}
