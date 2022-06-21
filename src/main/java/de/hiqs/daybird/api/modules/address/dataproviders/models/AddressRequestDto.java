package de.hiqs.daybird.api.modules.address.dataproviders.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.hiqs.daybird.api.shared.dataproviders.models.AbstractRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressRequestDto extends AbstractRequestDto {
    private String country;
    private String street;
    private String houseNumber;
    private String city;
    private String zipCode;
}
