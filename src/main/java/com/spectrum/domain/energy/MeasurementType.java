package com.spectrum.domain.energy;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class MeasurementType{
    public String ifOther;
    public String method;
}

