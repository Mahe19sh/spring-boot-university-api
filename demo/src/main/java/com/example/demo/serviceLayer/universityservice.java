package com.example.demo.serviceLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Pojoa;
import com.example.demo.model.Pojob;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.*;

@Service
public class Universityservice {
    @Autowired
    private RestTemplate restTemplate;
    private static final String BASE_URL = "http://universities.hipolabs.com/search";

    @Autowired
    public void Universityservice(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    // Single country API request
    public List<Pojob> getUniversitiesByCountry(String country) {
        try {
            String url = BASE_URL + "?country=" + country;
            Pojoa[] response = restTemplate.getForObject(url, Pojoa[].class);
            if (response != null) {
                return List.of(response).stream()
                        .map(Pojoa::getData)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching data for country: " + country, e);
        }
        return List.of();
    }

    // Multiple countries API request with multithreading
    public List<Pojob> getUniversitiesForMultipleCountries(List<String> countries) {
        ExecutorService executorService = Executors.newFixedThreadPool(10); // Adjust the pool size
        List<CompletableFuture<List<Pojob>>> futures = countries.stream()
                .map(country -> CompletableFuture.supplyAsync(() -> getUniversitiesByCountry(country), executorService))
                .collect(Collectors.toList());

        return futures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
