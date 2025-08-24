package com.example.hospital.specification;

import java.time.LocalDate;

import org.json.simple.JSONObject;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.example.hospital.entity.HospitalEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class HospitalSpecification {
	public static Specification<HospitalEntity> search(String searchParam) {
		return (Root<HospitalEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
			Predicate finalPredicate = criteriaBuilder.conjunction();
			try {
				if (StringUtils.hasLength(searchParam)) {
					JSONObject searchParamJson = new ObjectMapper().readValue(searchParam, JSONObject.class);
					Integer patientId = searchParamJson.get("patientId") != null
							? Integer.parseInt(searchParamJson.get("patientId").toString())
							: null;
					String deptName = (String) searchParamJson.get("deptName");
					String patientName = (String) searchParamJson.get("patientName");
					String disease = (String) searchParamJson.get("disease");
					String fromDate = (String) searchParamJson.get("fromDate");
					String toDate = (String) searchParamJson.get("toDate");

					if (patientId != null) {
						finalPredicate = criteriaBuilder.and(finalPredicate,
								criteriaBuilder.equal(root.get("Id").get("patientId"), patientId));
					}

					if (StringUtils.hasLength(deptName)) {
						finalPredicate = criteriaBuilder.and(finalPredicate,
								criteriaBuilder.like(root.get("Id").get("deptName"), deptName + "%"));
					}

					if (StringUtils.hasLength(patientName)) {
						finalPredicate = criteriaBuilder.and(finalPredicate,
								criteriaBuilder.like(root.get("patientName"), patientName + "%"));
					}

					if (StringUtils.hasLength(disease)) {
						finalPredicate = criteriaBuilder.and(finalPredicate,
								criteriaBuilder.like(root.get("disease"), disease + "%"));
					}

					if (StringUtils.hasLength(fromDate) && StringUtils.hasLength(toDate)) {
						LocalDate start = LocalDate.parse(fromDate);
						LocalDate end = LocalDate.parse(toDate);
						finalPredicate = criteriaBuilder.and(finalPredicate,
								criteriaBuilder.between(root.get("admitDate"), start, end));
					}
				}
				query.orderBy(criteriaBuilder.asc(root.get("status")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return finalPredicate;
		};
	}
}
