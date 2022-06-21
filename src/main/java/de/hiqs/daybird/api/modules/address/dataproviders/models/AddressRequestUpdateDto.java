package de.hiqs.daybird.api.modules.address.dataproviders.models;

import de.hiqs.daybird.api.shared.dataproviders.models.AbstractRequestUpdateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequestUpdateDto extends AbstractRequestUpdateDto {
    String country;
    String street;
    String houseNumber;
    String city;
    String zipCode;
}
