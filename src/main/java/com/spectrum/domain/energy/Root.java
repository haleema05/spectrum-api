package com.spectrum.domain.energy;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class Root{
    public String agency;
    public MeasurementType measurementType;
    public List<Release> releases;
    public String version;
}
