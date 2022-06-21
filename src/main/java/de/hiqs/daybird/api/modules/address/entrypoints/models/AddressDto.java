package de.hiqs.daybird.api.modules.address.entrypoints.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.hiqs.daybird.api.shared.entrypoints.models.AbstractDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressDto extends AbstractDto {
    private String country;
    private String street;
    private String houseNumber;
    private String city;
    private String zipCode;
}
