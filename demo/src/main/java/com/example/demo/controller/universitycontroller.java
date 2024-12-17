package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.pojob;
import com.example.demo.serviceLayer.universityservice;

import java.util.List;

@RestController
@RequestMapping("/api/universities")
public class universitycontroller {
    private final universityservice universityService;

    public universitycontroller(universityservice universityService) {
        this.universityService = universityService;
    }

    @GetMapping("/by-country")
    public ResponseEntity<List<pojob>> getUniversitiesByCountry(@RequestParam String country) {
        List<pojob> response = universityService.getUniversitiesByCountry(country);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/by-multiple-countries")
    public ResponseEntity<List<pojob>> getUniversitiesByMultipleCountries(@RequestBody List<String> countries) {
        List<pojob> response = universityService.getUniversitiesForMultipleCountries(countries);
        return ResponseEntity.ok(response);
    }
}
