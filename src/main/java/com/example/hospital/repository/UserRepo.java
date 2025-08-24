package com.example.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hospital.entity.UserEntity;



public interface UserRepo  extends JpaRepository<UserEntity,String>{
	
}