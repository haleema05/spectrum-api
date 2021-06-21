package com.spectrum.service;

import java.io.PrintWriter;
import java.util.List;

import com.spectrum.domain.Organization;

public interface OrganizationService {

    List<Organization> fetchData(String field, int sortType);

    void export(String field, int sortType, PrintWriter writer);
}
