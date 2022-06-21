package de.hiqs.daybird.api.modules.address.entrypoints.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressPostDto {
    private String country;
    private String street;
    private String houseNumber;
    private String city;
    private String zipCode;
}

