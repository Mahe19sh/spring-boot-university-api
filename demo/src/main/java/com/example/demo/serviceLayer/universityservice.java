package com.example.demo.serviceLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.pojoa;
import com.example.demo.model.pojob;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.*;

@Service
public class universityservice {
    @Autowired
    private RestTemplate restTemplate;
    private static final String BASE_URL = "http://universities.hipolabs.com/search";

    // Single country API request
    public List<pojob> getUniversitiesByCountry(String country) {
        try {
            String url = BASE_URL + "?country=" + country;
            pojoa[] response = restTemplate.getForObject(url, pojoa[].class);
            if (response != null) {
                return List.of(response).stream()
                        .map(pojoa::getData)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching data for country: " + country, e);
        }
        return List.of();
    }

    // Multiple countries API request with multithreading
    public List<pojob> getUniversitiesForMultipleCountries(List<String> countries) {
        ExecutorService executorService = Executors.newFixedThreadPool(10); // Adjust the pool size
        List<CompletableFuture<List<pojob>>> futures = countries.stream()
                .map(country -> CompletableFuture.supplyAsync(() -> getUniversitiesByCountry(country), executorService))
                .collect(Collectors.toList());

        return futures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
