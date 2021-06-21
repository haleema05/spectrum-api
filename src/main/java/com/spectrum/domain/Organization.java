package com.spectrum.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Organization {

    private String organization;
    private int release_count;
    private double total_labor_hours;
    private boolean all_in_production = true;
    private Set<String> licenses = new HashSet<>();
    private List<Integer>  most_active_months = new ArrayList<>();
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public int getRelease_count() {
		return release_count;
	}
	public void setRelease_count(int release_count) {
		this.release_count = release_count;
	}
	public double getTotal_labor_hours() {
		return total_labor_hours;
	}
	public void setTotal_labor_hours(double total_labor_hours) {
		this.total_labor_hours = total_labor_hours;
	}
	public boolean isAll_in_production() {
		return all_in_production;
	}
	public void setAll_in_production(boolean all_in_production) {
		this.all_in_production = all_in_production;
	}
	public Set<String> getLicenses() {
		return licenses;
	}
	public void setLicenses(Set<String> licenses) {
		this.licenses = licenses;
	}
	public List<Integer> getMost_active_months() {
		return most_active_months;
	}
	public void setMost_active_months(List<Integer> most_active_months) {
		this.most_active_months = most_active_months;
	}
    
    
    
}
