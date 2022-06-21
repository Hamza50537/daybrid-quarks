package de.hiqs.daybird.api.modules.customer.domains.models;

import de.hiqs.daybird.api.shared.domains.models.AbstractModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer extends AbstractModel {
    private String name;
    private String sign;
    private String email;
    private String phoneNumber;
    private String addressUuid;
}
