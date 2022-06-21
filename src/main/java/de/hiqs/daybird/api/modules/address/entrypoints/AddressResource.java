package de.hiqs.daybird.api.modules.address.entrypoints;

import de.hiqs.daybird.api.modules.address.dataproviders.AddressRestClient;
import de.hiqs.daybird.api.modules.address.domains.AddressService;
import de.hiqs.daybird.api.modules.address.domains.models.Address;
import de.hiqs.daybird.api.modules.address.domains.models.AddressUpdate;
import de.hiqs.daybird.api.modules.address.entrypoints.models.AddressDto;
import de.hiqs.daybird.api.modules.address.entrypoints.models.AddressPostDto;
import de.hiqs.daybird.api.modules.address.entrypoints.models.AddressUpdateDto;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import de.hiqs.daybird.api.shared.entrypoints.AbstractReactiveResource;
import io.smallrye.mutiny.Uni;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(AddressResource.PATH)
@ApplicationScoped
public class AddressResource extends AbstractReactiveResource<AddressDto> {

    public static final String PATH = "/addresses";

    private final AddressService addressService;
    private final ModelMapper modelMapper;

    public AddressResource(AddressService addressService, ModelMapper modelMapper) {
        this.addressService = addressService;
        this.modelMapper = modelMapper;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<AddressDto> addPost(AddressPostDto addressDto) {
        return addressService.addPost(modelMapper.map(addressDto, Address.class))
                .map(address -> modelMapper.map(address, AddressDto.class));

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<AddressDto>> list(@QueryParam(AddressRestClient.PARAM_COUNTRY) String country,
                                     @QueryParam(AddressRestClient.PARAM_CITY) String city,
                                     @QueryParam(AddressRestClient.PARAM_STREET) String street,
                                     @QueryParam(AddressRestClient.PARAM_HOUSE_NUMBER) String houseNumber,
                                     @QueryParam(AddressRestClient.PARAM_ZIP_CODE) String zipCode,
                                     @DefaultValue("false") @QueryParam(AddressRestClient.PARAM_ARCHIVED) boolean archived) {
        return addressService.list(country, city, street, houseNumber, zipCode, archived)
                .map(addresses -> modelMapper.map(addresses, new TypeToken<List<AddressDto>>() {
                }.getType()));
    }

    @GET
    @Path("/{uuid}")
    public Uni<AddressDto> getAddress(@PathParam("uuid") String uuid) {
        return addressService.getAddress(uuid)
                .map(address -> modelMapper.map(address, AddressDto.class));
    }

    @DELETE
    @Path("/{uuid}")
    public Uni<Void> deleteAddress(@PathParam("uuid") String uuid) {
        return addressService.deleteAddress(uuid);
    }

    @PUT
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<AddressDto> update(@PathParam("uuid") String uuid, AddressUpdateDto addressUpdateDto) {
        return addressService.update(uuid, modelMapper.map(addressUpdateDto, AddressUpdate.class))
                .map(address -> modelMapper.map(address, AddressDto.class));
    }

    @PUT
    @Path("/archive/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<AddressDto> updateArchived(@PathParam("uuid") String uuid,
                                          @QueryParam(AddressRestClient.PARAM_ARCHIVED) boolean archived,
                                          @QueryParam("entity_type") EntityTypeEnum entityTypeEnum,
                                          @QueryParam("recursive") boolean recursive) {
        return addressService.updateArchived(uuid, archived, entityTypeEnum, recursive)
                .map(address -> modelMapper.map(address, AddressDto.class));
    }

    @GET
    @Path("/uuid")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<AddressDto> getByUuid(@QueryParam("uuid") String uuid) {
        return addressService.getByUuid(uuid)
                .map(address -> modelMapper.map(address, AddressDto.class));
    }

}
