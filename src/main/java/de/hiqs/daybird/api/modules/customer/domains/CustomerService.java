package de.hiqs.daybird.api.modules.customer.domains;

import de.hiqs.daybird.api.modules.customer.dataproviders.CustomerAdapter;
import de.hiqs.daybird.api.modules.customer.dataproviders.models.CustomerRequestUpdateDto;
import de.hiqs.daybird.api.modules.customer.domains.models.Customer;
import de.hiqs.daybird.api.modules.customer.domains.models.CustomerUpdate;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import io.smallrye.mutiny.Uni;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CustomerService {

    private final CustomerAdapter customerAdapter;
    private final ModelMapper modelMapper;

    public CustomerService(CustomerAdapter customerAdapter, ModelMapper modelMapper) {
        this.customerAdapter = customerAdapter;
        this.modelMapper = modelMapper;
    }

    public Uni<Customer> getByUuid(String uuid) {
        return customerAdapter.getByUuid(uuid);
    }

    public Uni<Customer> getCustomer(String uuid) {
        return customerAdapter.getCustomer(uuid);
    }

    public Uni<Customer> updateArchived(String uuid, boolean archived, EntityTypeEnum entityTypeEnum, boolean recursive) {
        return customerAdapter.updateArchived(uuid, archived, entityTypeEnum, recursive);
    }

    public Uni<Customer> update(String uuid, CustomerUpdate customerUpdate) {
        return customerAdapter.update(uuid, modelMapper.map(customerUpdate, CustomerRequestUpdateDto.class));
    }

    public Uni<Void> deleteCustomer(String uuid) {
        return customerAdapter.deleteCustomer(uuid);
    }

    public Uni<List<Customer>> list(String name, String sign, String email, String phoneNumber, String addressUuid, boolean archived) {
        return customerAdapter.list(name, sign, email, phoneNumber, addressUuid, archived);
    }

    public Uni<Customer> addCustomer(Customer customer) {
        return customerAdapter.addCustomer(customer);
    }
}
