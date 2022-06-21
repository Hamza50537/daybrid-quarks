package de.hiqs.daybird.api.modules.customer.domains.models;

import de.hiqs.daybird.api.shared.domains.models.AbstractUpdateModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerUpdate extends AbstractUpdateModel {
    String name;
    String sign;
    String email;
    String phoneNumber;
    String addressUuid;
}
