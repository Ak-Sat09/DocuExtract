package com.example.Resume_Keyword.controller;

import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.Resume_Keyword.service.ResumeService;
import com.example.Resume_Keyword.entity.ResumeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for handling resume upload requests.
 */
@RestController
@RequestMapping("/api/resumes")
@CrossOrigin(origins = "*") // You can restrict this to specific domains if needed
public class ResumeController {

    private static final Logger logger = LoggerFactory.getLogger(ResumeController.class);

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    /**
     * Endpoint to upload a resume file.
     *
     * @param file the resume file to be uploaded
     * @return ResponseEntity with the saved resume data or an error message
     */
    @PostMapping("/upload")
    public ResponseEntity<Object> uploadResume(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No file provided. Please upload a valid resume.");
            }

            // Call the service to process and save the resume
            ResumeEntity savedResume = resumeService.saveAndProcess(file);

            // Log success
            logger.info("Resume uploaded successfully: {}", savedResume.getFileName());

            // Return success response with the saved resume data
            return ResponseEntity.status(HttpStatus.CREATED).body(savedResume);
        } catch (IOException e) {
            // Log the exception
            logger.error("Error uploading file: {}", e.getMessage());

            // Return error response if file upload fails
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload resume: " + e.getMessage());
        } catch (TikaException e) {
            // Log the exception
            logger.error("Error extracting text from the resume: {}", e.getMessage());

            // Return error response for text extraction failure
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to extract text from the resume: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // Log the exception
            logger.error("Invalid file: {}", e.getMessage());

            // Return error response for empty file or invalid file
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid file: " + e.getMessage());
        }
    }
}
