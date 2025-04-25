package com.example.webhooksolver.service;

import com.example.webhooksolver.model.GenerateResponse;
import com.example.webhooksolver.model.User;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class StartupService {
    private final RestTemplate restTemplate = new RestTemplate();

    public void processWebhook() throws InterruptedException {
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "John Doe");
        requestBody.put("regNo", "REG12347");
        requestBody.put("email", "john@example.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<GenerateResponse> response = restTemplate.postForEntity(url, request, GenerateResponse.class);
        GenerateResponse res = response.getBody();
        if (res == null) return;

        List<List<Integer>> outcome = solveMutualFollowers(res.getData().getUsers());

        Map<String, Object> resultBody = new HashMap<>();
        resultBody.put("regNo", "REG12347");
        resultBody.put("outcome", outcome);

        HttpHeaders resultHeaders = new HttpHeaders();
        resultHeaders.setContentType(MediaType.APPLICATION_JSON);
        resultHeaders.set("Authorization", res.getAccessToken());

        HttpEntity<Map<String, Object>> finalRequest = new HttpEntity<>(resultBody, resultHeaders);

        int attempts = 0;
        while (attempts < 4) {
            try {
                restTemplate.postForEntity(res.getWebhook(), finalRequest, String.class);
                break;
            } catch (Exception e) {
                attempts++;
                Thread.sleep(1000);
            }
        }
    }

    private List<List<Integer>> solveMutualFollowers(List<User> users) {
        Map<Integer, Set<Integer>> followMap = new HashMap<>();
        for (User user : users) {
            followMap.put(user.getId(), new HashSet<>(user.getFollows()));
        }

        Set<String> seen = new HashSet<>();
        List<List<Integer>> result = new ArrayList<>();

        for (User user : users) {
            for (int fid : user.getFollows()) {
                if (followMap.containsKey(fid) && followMap.get(fid).contains(user.getId())) {
                    int min = Math.min(user.getId(), fid);
                    int max = Math.max(user.getId(), fid);
                    String key = min + "-" + max;
                    if (!seen.contains(key)) {
                        seen.add(key);
                        result.add(Arrays.asList(min, max));
                    }
                }
            }
        }

        return result;
    }
}
