package com.example.hospital.dto;

import java.util.List;

import org.json.simple.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponse {

	private String message;
	private String code;
	private List<JSONObject> details;
	
}
