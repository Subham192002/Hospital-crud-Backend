package com.example.hospital.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.hospital.dto.HospitalDTO;
import com.example.hospital.dto.ServiceResponse;
import com.example.hospital.dto.User;
import com.example.hospital.entity.Department;
import com.example.hospital.entity.HospitalEntity;
import com.example.hospital.entity.HospitalPK;
import com.example.hospital.entity.UserEntity;
import com.example.hospital.exception.IdNotFoundException;
import com.example.hospital.repository.DepartmentRepo;
import com.example.hospital.repository.HospitalRepo;
import com.example.hospital.repository.UserRepo;
import com.example.hospital.specification.HospitalSpecification;
import com.example.hospital.utils.Constants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class HospitalServiceImpl implements HospitalService {

	private final HospitalRepo hospitalRepo;
	private final DepartmentRepo departmentRepo;
	private final RestTemplate restTemplate;
	private final UserRepo userRepo;
	private final MessageSource messageSource;

	@Value("${oauth.token-url}")
	private String tokenUrl;
	@Value("${oauth.client-id}")
	private String clientId;
	@Value("${oauth.client-secret}")
	private String clientSecret;
	@Value("${oauth.client-grant-type}")
	private String grantType;

	@Override
	public ServiceResponse add(HospitalDTO hospitalDTO) {
		HospitalPK id = HospitalPK.builder().patientId(hospitalDTO.getPatientId()).doctorId(hospitalDTO.getDoctorId())
				.deptName(hospitalDTO.getDeptName()).build();
		Optional<HospitalEntity> hospitalEntityOptional = hospitalRepo.findById(id);
		try {
			if (hospitalEntityOptional.isPresent()) {
				return new ServiceResponse(Constants.FAILED, message("interland.details.hospital.H003"), List.of());
			} else {
				HospitalEntity hospitalEntity = HospitalEntity.builder().id(id).doctorName(hospitalDTO.getDoctorName())
						.specialization(hospitalDTO.getSpecialization()).patientName(hospitalDTO.getPatientName())
						.gender(hospitalDTO.getGender()).disease(hospitalDTO.getDisease())
						.contactNumber(hospitalDTO.getContactNumber()).admitDate(hospitalDTO.getAdmitDate())
						.status(Constants.ADMITTED).build();

				hospitalRepo.save(hospitalEntity);
				log.info("Hospital record added successfully.");
				return new ServiceResponse(Constants.SUCCESS, message("interland.details.hospital.H001"), List.of());
			}
		} catch (Exception e) {
			log.error("Error adding hospital record", e);
			return new ServiceResponse(Constants.FAILED, message("interland.details.hospital.H002"), List.of());
		}
	}

	@Override
	public HospitalDTO fetchById(int patientId, int doctorId, String deptName) {
		HospitalPK hospitalPK = new HospitalPK(patientId, doctorId, deptName);
		return hospitalRepo.findById(hospitalPK)
				.map(hospitalEntity -> HospitalDTO.builder().patientId(hospitalEntity.getId().getPatientId())
						.doctorId(hospitalEntity.getId().getDoctorId()).deptName(hospitalEntity.getId().getDeptName())
						.doctorName(hospitalEntity.getDoctorName()).specialization(hospitalEntity.getSpecialization())
						.patientName(hospitalEntity.getPatientName()).gender(hospitalEntity.getGender())
						.disease(hospitalEntity.getDisease()).contactNumber(hospitalEntity.getContactNumber())
						.admitDate(hospitalEntity.getAdmitDate()).status(hospitalEntity.getStatus()).build())
				.orElseThrow(() -> new IdNotFoundException(message("interland.details.hospital.H006")));
	}

	@Override
	public ServiceResponse update(int patientId, int doctorId, String deptName, HospitalDTO hospitalDTO) {
		try {
			HospitalPK id = new HospitalPK(patientId, doctorId, deptName);
			Optional<HospitalEntity> hospitalEntityOptional = hospitalRepo.findById(id);
			if (hospitalEntityOptional.isPresent()) {
				HospitalEntity hospitalEntity = HospitalEntity.builder().id(id).doctorName(hospitalDTO.getDoctorName())
						.specialization(hospitalDTO.getSpecialization()).patientName(hospitalDTO.getPatientName())
						.gender(hospitalDTO.getGender()).disease(hospitalDTO.getDisease())
						.contactNumber(hospitalDTO.getContactNumber()).admitDate(hospitalDTO.getAdmitDate())
						.status(hospitalDTO.getStatus()).build();
				hospitalRepo.save(hospitalEntity);
				log.info("Details updated successfully.");
				return new ServiceResponse(Constants.SUCCESS, message("interland.details.hospital.H004"), List.of());
			} else {
				return new ServiceResponse(Constants.FAILED, message("interland.details.hospital.H005"), List.of());
			}
		} catch (Exception e) {
			log.error("Error updating hospital record", e);
			return new ServiceResponse(Constants.FAILED, message("interland.details.hospital.H005"), List.of());
		}
	}

	@Override
	public ServiceResponse remove(int patientId, int doctorId, String deptName) {
		try {
			HospitalPK id = new HospitalPK(patientId, doctorId, deptName);
			Optional<HospitalEntity> hospitalEntityOptional = hospitalRepo.findById(id);

			if (hospitalEntityOptional.isPresent()) {
				HospitalEntity hospitalEntity = hospitalEntityOptional.get();
				HospitalEntity updatedEntity = HospitalEntity.builder().id(hospitalEntity.getId())
						.doctorName(hospitalEntity.getDoctorName()).specialization(hospitalEntity.getSpecialization())
						.patientName(hospitalEntity.getPatientName()).gender(hospitalEntity.getGender())
						.disease(hospitalEntity.getDisease()).contactNumber(hospitalEntity.getContactNumber())
						.admitDate(hospitalEntity.getAdmitDate()).status(Constants.DISCHARGED).build();
				hospitalRepo.save(updatedEntity);
				return new ServiceResponse(Constants.SUCCESS, message("interland.details.hospital.H004"), List.of());
			}
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return new ServiceResponse(Constants.FAILED, message("interland.details.hospital.H008"), List.of());
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject search(String searchDataJson, Integer pageIndex, Integer pageSize) {
		JSONObject response = new JSONObject();
		try {
			Page<HospitalEntity> page = hospitalRepo.findAll(
					Specification.where(HospitalSpecification.search(searchDataJson)),
					PageRequest.of(pageIndex, pageSize));
			List<HospitalDTO> hospitalDTOs = page.getContent().stream()
					.map(hospitalEntity -> HospitalDTO.builder().patientId(hospitalEntity.getId().getPatientId())
							.doctorId(hospitalEntity.getId().getDoctorId())
							.deptName(hospitalEntity.getId().getDeptName()).doctorName(hospitalEntity.getDoctorName())
							.specialization(hospitalEntity.getSpecialization())
							.patientName(hospitalEntity.getPatientName()).gender(hospitalEntity.getGender())
							.disease(hospitalEntity.getDisease()).contactNumber(hospitalEntity.getContactNumber())
							.admitDate(hospitalEntity.getAdmitDate()).status(hospitalEntity.getStatus()).build())
					.toList();
			JSONArray resultArray = new JSONArray();
			resultArray.addAll(hospitalDTOs);
			response.put(Constants.AA_DATA, resultArray);
			response.put(Constants.TOTAL_DISPLAY_RECORD, page.getTotalElements());
			response.put(Constants.TOTAL_RECORD, page.getTotalElements());
		} catch (Exception e) {
			log.error("Error occurred during search operation", e);
			response.put(Constants.AA_DATA, new JSONArray());
			response.put(Constants.TOTAL_DISPLAY_RECORD, 0);
			response.put(Constants.TOTAL_RECORD, 0);
		}
		return response;
	}

	@Override
	public List<Department> getAll() {
		try {
			return departmentRepo.findAll();
		} catch (Exception e) {
			log.error("Error fetching departments", e);
			return Collections.emptyList();
		}
	}

	@Override
	public ServiceResponse getAccessToken(User user) {
		Optional<UserEntity> userEntityOptional = userRepo.findById(user.getUserName());
		if (!userEntityOptional.isPresent()) {
			throw new IllegalArgumentException(message("interland.details.hospital.H009"));
		}
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData(user), headers);
			return new ServiceResponse(Constants.SUCCESS, message("interland.details.hospital.H007"),
					List.of(restTemplate.exchange(tokenUrl, HttpMethod.POST, request, JSONObject.class).getBody()));
		} catch (Exception e) {
			log.error(Constants.FAILED);
			throw e;
		}
	}

	private MultiValueMap<String, String> formData(User user) {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("grant_type", grantType);
		formData.add("client_id", clientId);
		formData.add("username", user.getUserName());
		formData.add("password", user.getPassword());
		formData.add("client_secret", clientSecret);
		return formData;
	}

	private String message(String key) {
		return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
	}

}
