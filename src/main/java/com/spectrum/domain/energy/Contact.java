package com.spectrum.domain.energy;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class Contact{
    public String email;
    @Tolerate
    public Contact(){

    }
}
