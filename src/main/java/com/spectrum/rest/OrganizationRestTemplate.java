package com.spectrum.rest;

import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class OrganizationRestTemplate {

    /**
     * Fetches data from external API
     * 
     * https://www.energy.gov/sites/prod/files/2020/12/f81/code-12-15-2020.json
     *
     */
    public JSONObject readExportData() {
        RestTemplate restTemplate = new RestTemplate();
        String dataFrom = "https://www.energy.gov/sites/prod/files/2020/12/f81/code-12-15-2020.json";
        ResponseEntity<String> response = restTemplate.exchange(dataFrom, HttpMethod.GET, null, String.class);
        return new JSONObject(response.getBody());
    }
}
