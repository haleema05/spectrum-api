package com.spectrum.domain.energy;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder

public class Date{
    public String created;
    public String metadataLastUpdated;
    @Tolerate
    public Date(){

    }
}
