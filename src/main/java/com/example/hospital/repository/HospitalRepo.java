package com.example.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.hospital.entity.HospitalEntity;
import com.example.hospital.entity.HospitalPK;


@Repository
public interface HospitalRepo extends JpaRepository<HospitalEntity, HospitalPK> ,JpaSpecificationExecutor<HospitalEntity>{
	
}
