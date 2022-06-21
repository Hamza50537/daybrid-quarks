package de.hiqs.daybird.api.modules.customer.dataproviders.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerRequestPostDto {
    private String name;
    private String sign;
    private String email;
    private String phoneNumber;
    private String addressUuid;
}
