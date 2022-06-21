package de.hiqs.daybird.api.modules.address.dataproviders;

import de.hiqs.daybird.api.modules.address.dataproviders.models.AddressRequestPostDto;
import de.hiqs.daybird.api.modules.address.dataproviders.models.AddressRequestUpdateDto;
import de.hiqs.daybird.api.modules.address.domains.models.Address;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class AddressAdapter {


    private final AddressRestClient addressRestClient;
    private final ModelMapper modelMapper;

    public AddressAdapter(@RestClient AddressRestClient addressRestClient, ModelMapper modelMapper) {
        this.addressRestClient = addressRestClient;
        this.modelMapper = modelMapper;
    }

    public Uni<Address> getAddress(String uuid) {
        return addressRestClient.getAddress(uuid)
                .map(addressRequestDto -> modelMapper.map(addressRequestDto, Address.class));
    }

    public Uni<Address> getByUuid(String uuid) {
        return addressRestClient.getByUuid(uuid)
                .map(addressRequestDto -> modelMapper.map(addressRequestDto, Address.class));
    }

    public Uni<Address> updateArchived(String uuid, boolean archived, EntityTypeEnum entityTypeEnum, boolean recursive) {
        return addressRestClient.updateArchived(uuid, archived, entityTypeEnum, recursive)
                .map(addressRequestDto -> modelMapper.map(addressRequestDto, Address.class));
    }

    public Uni<Address> update(String uuid, AddressRequestUpdateDto addressRequestUpdateDto) {
        return addressRestClient.update(uuid, addressRequestUpdateDto)
                .map(addressRequestDto -> modelMapper.map(addressRequestDto, Address.class));
    }

    public Uni<Void> deleteAddress(String uuid) {
        return addressRestClient.deleteAddress(uuid);
    }

    public Uni<List<Address>> list(String country, String city, String street, String houseNumber, String zipCode, boolean archived) {
        return addressRestClient.list(country, city, street, houseNumber, zipCode, archived)
                .map(addressRequestDtos -> modelMapper.map(addressRequestDtos, new TypeToken<List<Address>>() {
                }.getType()));
    }

    public Uni<Address> addPost(Address address) {
        return addressRestClient.addPost(modelMapper.map(address, AddressRequestPostDto.class))
                .map(addressRequestDto -> modelMapper.map(addressRequestDto, Address.class));
    }
}
