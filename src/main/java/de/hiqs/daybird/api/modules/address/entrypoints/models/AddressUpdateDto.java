package de.hiqs.daybird.api.modules.address.entrypoints.models;

import de.hiqs.daybird.api.shared.entrypoints.models.AbstractUpdateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressUpdateDto extends AbstractUpdateDto {
    String country;
    String street;
    String houseNumber;
    String city;
    String zipCode;
}
