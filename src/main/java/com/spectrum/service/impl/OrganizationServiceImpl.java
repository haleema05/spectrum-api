package com.spectrum.service.impl;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.spectrum.Constants;
import com.spectrum.domain.Organization;
import com.spectrum.rest.OrganizationRestTemplate;
import com.spectrum.service.OrganizationService;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    /**
     * Fetches Organization data
     */
    @Override
    public List<Organization> fetchData(String field, int sortType) {
      
    	JSONObject data = new OrganizationRestTemplate().readExportData();

        return getFetchedData(field, sortType, data);
    }

    public List<Organization> getFilteredData(String field, int sortType, JSONObject data) {
        return getFetchedData(field, sortType, data);
    }

    /**
     * Export the CSV data
     */
    @Override
    public void export(String field, int sortType, PrintWriter printWriter) {
        //Read data
        JSONObject jsonObject = new OrganizationRestTemplate().readExportData();
        Collection<Organization> data = getFetchedData(field, sortType,jsonObject);

        //Adding the header
        printWriter.append(Constants.CSV_HEADER);
        //New Line after the header
        printWriter.append(Constants.LINE_SEPARATOR);

        data.stream().forEach(org -> {
            printWriter.append(org.getOrganization());
            printWriter.append(Constants.COMMA_DELIMITER);
            printWriter.append(String.valueOf(org.getRelease_count()));
            printWriter.append(Constants.COMMA_DELIMITER);
            printWriter.append(String.valueOf(org.getTotal_labor_hours()));
            printWriter.append(Constants.COMMA_DELIMITER);
            printWriter.append(String.valueOf(org.isAll_in_production()));
            printWriter.append(Constants.COMMA_DELIMITER);
            printWriter.append(org.getLicenses().stream().collect(Collectors.joining(" | ")));
            printWriter.append(Constants.COMMA_DELIMITER);
            printWriter.append(org.getMost_active_months().stream().map(String::valueOf).collect(Collectors.joining(" | ")));
            printWriter.append(Constants.LINE_SEPARATOR);
        });
    }


    private List<Organization> getFetchedData(String field, int sortType, JSONObject data) {


        //Populate the data
        Map<String, List<LocalDate>> dates = new HashMap<>();
        Map<String, Organization> result = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        JSONArray releases = data.getJSONArray("releases");
        releases.forEach(release -> {
            JSONObject organization = (JSONObject) release;
            Organization spectrumDetails = new Organization();
            spectrumDetails.setOrganization(organization.getString("organization").trim());
            spectrumDetails.setAll_in_production("Production".equalsIgnoreCase(organization.getString("status")));
            spectrumDetails.setTotal_labor_hours(organization.getDouble("laborHours"));
            spectrumDetails.setRelease_count(1);
            organization.getJSONObject("permissions").getJSONArray("licenses").forEach(license -> {
                JSONObject organizationLicense = (JSONObject) license;
                spectrumDetails.getLicenses().add(organizationLicense.getString("name"));
            });

            if (result.containsKey(spectrumDetails.getOrganization())) {
            	Organization existingSpectrumDetails = result.get(spectrumDetails.getOrganization());
                existingSpectrumDetails.setRelease_count(existingSpectrumDetails.getRelease_count() + 1);
                existingSpectrumDetails.setTotal_labor_hours(existingSpectrumDetails.getTotal_labor_hours() + spectrumDetails.getTotal_labor_hours());
                existingSpectrumDetails.getLicenses().addAll(spectrumDetails.getLicenses());
                if (!spectrumDetails.isAll_in_production()) {
                    existingSpectrumDetails.setAll_in_production(false);
                }
                List<LocalDate> existingDates = new ArrayList<>(dates.get(spectrumDetails.getOrganization()));
                existingDates.add(LocalDate.parse(organization.getJSONObject("date").getString("created"), formatter));
                result.put(spectrumDetails.getOrganization(), existingSpectrumDetails);
                dates.put(spectrumDetails.getOrganization(), existingDates);
            } else {
                result.put(spectrumDetails.getOrganization(), spectrumDetails);
                dates.put(spectrumDetails.getOrganization(), Arrays.asList(LocalDate.parse(organization.getJSONObject("date").getString("created"), formatter)));
            }
        });

        populateMostActiveMonth(dates, result.values());
        return sortData(field, sortType, result.values());
    }

    private void populateMostActiveMonth(Map<String, List<LocalDate>> dates, Collection<Organization> result) {
        result.stream().forEach(org -> {
            org.getMost_active_months().addAll(dates.get(org.getOrganization()).stream()
                    .map(LocalDate::getMonthValue)
                    .collect(Collectors.toList())
                    .stream()
                    .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                    .entrySet()
                    .stream()
                    .sorted(Comparator.comparing(Map.Entry<Integer, Long>::getValue).reversed())
                    .limit(5)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList()));
        });
    }

    /**
     * Sorting based on sort field
     */
    public List<Organization> sortData(String field, int sortType, Collection<Organization> result) {
        List<Organization> sortedResult;
        switch (field) {
            case Constants.ORGANIZATION:
                if (sortType == 0) {
                    sortedResult = result.stream()
                            .sorted(Comparator.comparing(Organization::getOrganization))
                            .collect(Collectors.toList());
                } else {
                    sortedResult = result.stream()
                            .sorted(Comparator.comparing(Organization::getOrganization).reversed())
                            .collect(Collectors.toList());
                }
                break;
            case Constants.RELEASE_COUNT:
                if (sortType == 0) {
                    sortedResult = result.stream()
                            .sorted(Comparator.comparing(Organization::getRelease_count))
                            .collect(Collectors.toList());
                } else {
                    sortedResult = result.stream()
                            .sorted(Comparator.comparing(Organization::getRelease_count).reversed())
                            .collect(Collectors.toList());
                }
                break;
            case Constants.TOTAL_LABOUR_HOURS:
                if (sortType == 0) {
                    sortedResult = result.stream()
                            .sorted(Comparator.comparing(Organization::getTotal_labor_hours))
                            .collect(Collectors.toList());
                } else {
                    sortedResult = result.stream()
                            .sorted(Comparator.comparing(Organization::getTotal_labor_hours).reversed())
                            .collect(Collectors.toList());
                }
                break;
            default:
                sortedResult = new ArrayList<>(result);
                break;
        }
        return sortedResult;
    }
}
