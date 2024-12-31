package com.example.Resume_Keyword.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ResumeConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dcnejt8lw");
        config.put("api_key", "219988794876871");
        config.put("api_secret", "gAcU8lQyQpntXd8zyOT39OSthv8");
        return new Cloudinary(config);
    }
}
