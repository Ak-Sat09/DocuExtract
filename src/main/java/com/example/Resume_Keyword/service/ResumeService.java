package com.example.Resume_Keyword.service;

import java.io.IOException;
import java.util.Map;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.utils.ObjectUtils;
import com.example.Resume_Keyword.config.ResumeConfig;
import com.example.Resume_Keyword.entity.ResumeEntity;
import com.example.Resume_Keyword.repository.ResumeRepository;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final ResumeConfig resumeConfig;
    private final Tika tika;

    // Define the maximum length for the "skills" column based on your database schema
    private static final int MAX_SKILLS_LENGTH = 2000;

    // Initialize Tika in the constructor
    public ResumeService(ResumeRepository resumeRepository, ResumeConfig resumeConfig) {
        this.resumeRepository = resumeRepository;
        this.resumeConfig = resumeConfig;
        this.tika = new Tika();  // Initialize Tika here
    }

    // Method to save and process the resume file
    public ResumeEntity saveAndProcess(MultipartFile file) throws IOException, TikaException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty. Please provide a valid resume.");
        }

        // Upload the file to Cloudinary
        @SuppressWarnings("unchecked")
        Map<String, Object> uploadResult = resumeConfig.cloudinary().uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

        // Extract the URL from the upload result
        String resumeUrl = (String) uploadResult.get("url");

        // Extract the file name
        String fileName = file.getOriginalFilename();

        // Extract text from the uploaded resume using Apache Tika
        String extractedResumeText = tika.parseToString(file.getInputStream());

        // Truncate the extracted text to fit the database column
        if (extractedResumeText.length() > MAX_SKILLS_LENGTH) {
            extractedResumeText = extractedResumeText.substring(0, MAX_SKILLS_LENGTH);
        }

        // Create a ResumeEntity object and populate it with the extracted data
        ResumeEntity resumeEntity = new ResumeEntity();
        resumeEntity.setUrl(resumeUrl);
        resumeEntity.setFileName(fileName);
        resumeEntity.setSkills(extractedResumeText);  
        return resumeRepository.save(resumeEntity);
    }
}
