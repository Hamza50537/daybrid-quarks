package de.hiqs.daybird.api.modules.address.domains.models;

import de.hiqs.daybird.api.shared.domains.models.AbstractUpdateModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressUpdate extends AbstractUpdateModel {
    String country;
    String street;
    String houseNumber;
    String city;
    String zipCode;
}
