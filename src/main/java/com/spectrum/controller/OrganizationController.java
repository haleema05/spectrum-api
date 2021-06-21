package com.spectrum.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.domain.Organization;
import com.spectrum.service.OrganizationService;

@RestController
public class OrganizationController {

    @Autowired
    OrganizationService organizationService;

    @GetMapping("/organizations")
    public ResponseEntity<List<Organization>> fetchData(@RequestParam("sortFiled") String field, @RequestParam("sortType") int sortType) {
    	System.out.println("inside fetchData");
        List<Organization> spectrumDetails = organizationService.fetchData(field, sortType);
        if (spectrumDetails == null || spectrumDetails.size() == 0) {
            return new ResponseEntity<>(spectrumDetails, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(spectrumDetails, HttpStatus.OK);
        }
    }

    @GetMapping(value = "organizations/exportData",produces = "text/csv")
    public void exportCSV(HttpServletResponse response, @RequestParam("sortFiled") String field, @RequestParam("sortType") int sortType) throws IOException {
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"Organization.csv\"");
        organizationService.export(field, sortType,response.getWriter());
    }

}
