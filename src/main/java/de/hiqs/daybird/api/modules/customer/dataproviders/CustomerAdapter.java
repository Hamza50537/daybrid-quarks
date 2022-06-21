package de.hiqs.daybird.api.modules.customer.dataproviders;

import de.hiqs.daybird.api.modules.customer.dataproviders.models.CustomerRequestPostDto;
import de.hiqs.daybird.api.modules.customer.dataproviders.models.CustomerRequestUpdateDto;
import de.hiqs.daybird.api.modules.customer.domains.models.Customer;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CustomerAdapter {

    private final CustomerRestClient customerRestClient;
    private final ModelMapper modelMapper;

    public CustomerAdapter(@RestClient CustomerRestClient customerRestClient, ModelMapper modelMapper) {
        this.customerRestClient = customerRestClient;
        this.modelMapper = modelMapper;
    }

    public Uni<Customer> getByUuid(String uuid) {
        return customerRestClient.getByUuid(uuid)
                .map(customerRequestDto -> modelMapper.map(customerRequestDto, Customer.class));
    }

    public Uni<Customer> getCustomer(String uuid) {
        return customerRestClient.getCustomer(uuid)
                .map(customerRequestDto -> modelMapper.map(customerRequestDto, Customer.class));
    }

    public Uni<Customer> updateArchived(String uuid, boolean archived, EntityTypeEnum entityTypeEnum, boolean recursive) {
        return customerRestClient.updateArchived(uuid, archived, entityTypeEnum, recursive)
                .map(customerRequestDto -> modelMapper.map(customerRequestDto, Customer.class));
    }

    public Uni<Customer> update(String uuid, CustomerRequestUpdateDto customerRequestUpdateDto) {
        return customerRestClient.update(uuid, customerRequestUpdateDto)
                .map(customerRequestDto -> modelMapper.map(customerRequestDto, Customer.class));
    }

    public Uni<Void> deleteCustomer(String uuid) {
        return customerRestClient.deleteCustomer(uuid);
    }

    public Uni<List<Customer>> list(String name, String sign, String email, String phoneNumber, String addressUuid, boolean archived) {
        return customerRestClient.list(name, sign, email, phoneNumber, addressUuid, archived)
                .map(customerRequestDtos -> modelMapper.map(customerRequestDtos, new TypeToken<List<Customer>>() {
                }.getType()));
    }

    public Uni<Customer> addCustomer(Customer customer) {
        return customerRestClient.addCustomer(modelMapper.map(customer, CustomerRequestPostDto.class))
                .map(customerRequestDto -> modelMapper.map(customerRequestDto, Customer.class));
    }
}
