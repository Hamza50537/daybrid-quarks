package de.hiqs.daybird.api.modules.address.domains.models;

import de.hiqs.daybird.api.shared.domains.models.AbstractModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address extends AbstractModel {
    private String country;
    private String street;
    private String houseNumber;
    private String city;
    private String zipCode;
}
