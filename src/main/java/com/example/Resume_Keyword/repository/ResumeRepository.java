package com.example.Resume_Keyword.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Resume_Keyword.entity.ResumeEntity;

public interface ResumeRepository extends JpaRepository<ResumeEntity , Integer>{
    
}
