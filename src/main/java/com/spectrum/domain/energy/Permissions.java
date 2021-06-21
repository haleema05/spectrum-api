package com.spectrum.domain.energy;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder

public class Permissions{
    public Object exemptionText;
    public List<License> licenses;
    public String usageType;
    @Tolerate
    public Permissions(){

    }
}
