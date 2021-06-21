package com.spectrum.domain.energy;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class Release{
    public Contact contact;
    public Date date;
    public String description;
    public double laborHours;
    public String name;
    public String organization;
    public Permissions permissions;
    public String repositoryURL;
    public String status;
    public List<String> tags;
    public String vcs;
    public List<Object> languages;
}
