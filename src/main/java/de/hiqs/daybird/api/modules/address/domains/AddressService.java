package de.hiqs.daybird.api.modules.address.domains;

import de.hiqs.daybird.api.modules.address.dataproviders.AddressAdapter;
import de.hiqs.daybird.api.modules.address.dataproviders.models.AddressRequestUpdateDto;
import de.hiqs.daybird.api.modules.address.domains.models.Address;
import de.hiqs.daybird.api.modules.address.domains.models.AddressUpdate;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import io.smallrye.mutiny.Uni;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class AddressService {

    private final AddressAdapter addressAdapter;
    private final ModelMapper modelMapper;

    public AddressService(AddressAdapter addressAdapter, ModelMapper modelMapper) {
        this.addressAdapter = addressAdapter;
        this.modelMapper = modelMapper;
    }

    public Uni<Address> getAddress(String uuid) {
        return addressAdapter.getAddress(uuid);
    }

    public Uni<Address> getByUuid(String uuid) {
        return addressAdapter.getByUuid(uuid);
    }

    public Uni<Address> updateArchived(String uuid, boolean archived, EntityTypeEnum entityTypeEnum, boolean recursive) {
        return addressAdapter.updateArchived(uuid, archived, entityTypeEnum, recursive);
    }

    public Uni<Address> update(String uuid, AddressUpdate addressUpdate) {
        return addressAdapter.update(uuid, modelMapper.map(addressUpdate, AddressRequestUpdateDto.class));
    }

    public Uni<Void> deleteAddress(String uuid) {
        return addressAdapter.deleteAddress(uuid);
    }

    public Uni<List<Address>> list(String country, String city, String street, String houseNumber, String zipCode, boolean archived) {
        return addressAdapter.list(country, city, street, houseNumber, zipCode, archived);
    }

    public Uni<Address> addPost(Address address) {
        return addressAdapter.addPost(address);
    }
}
