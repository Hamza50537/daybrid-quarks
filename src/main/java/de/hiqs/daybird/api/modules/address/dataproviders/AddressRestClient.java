package de.hiqs.daybird.api.modules.address.dataproviders;

import de.hiqs.daybird.api.modules.address.dataproviders.models.AddressRequestDto;
import de.hiqs.daybird.api.modules.address.dataproviders.models.AddressRequestPostDto;
import de.hiqs.daybird.api.modules.address.dataproviders.models.AddressRequestUpdateDto;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import io.quarkus.oidc.token.propagation.reactive.AccessTokenRequestReactiveFilter;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RegisterRestClient(configKey = "database-api")
@RegisterProvider(AccessTokenRequestReactiveFilter.class)
@Path(AddressRestClient.PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AddressRestClient {


    String PATH = "/addresses";

    String PARAM_COUNTRY = "country";
    String PARAM_CITY = "city";
    String PARAM_STREET = "street";
    String PARAM_HOUSE_NUMBER = "house-number";
    String PARAM_ZIP_CODE = "zip-code";
    String PARAM_ARCHIVED = "archived";

    @POST
    Uni<AddressRequestDto> addPost(AddressRequestPostDto addressRequestPostDto);

    @GET
    Uni<List<AddressRequestDto>> list(@QueryParam(PARAM_COUNTRY) String country,
                                      @QueryParam(PARAM_CITY) String city,
                                      @QueryParam(PARAM_STREET) String street,
                                      @QueryParam(PARAM_HOUSE_NUMBER) String houseNumber,
                                      @QueryParam(PARAM_ZIP_CODE) String zipCode,
                                      @DefaultValue("false") @QueryParam(PARAM_ARCHIVED) boolean archived);

    @GET
    @Path("/{uuid}")
    Uni<AddressRequestDto> getAddress(@PathParam("uuid") String uuid);

    @DELETE
    @Path("/{uuid}")
    Uni<Void> deleteAddress(@PathParam("uuid") String uuid);

    @PUT
    @Path("/{uuid}")
    Uni<AddressRequestDto> update(@PathParam("uuid") String uuid, AddressRequestUpdateDto addressRequestUpdateDto);

    @PUT
    @Path("/archive/{uuid}")
    Uni<AddressRequestDto> updateArchived(@PathParam("uuid") String uuid,
                                          @QueryParam(PARAM_ARCHIVED) boolean archived,
                                          @QueryParam("entity_type") EntityTypeEnum entityTypeEnum,
                                          @QueryParam("recursive") boolean recursive);

    @GET
    @Path("/uuid")
    Uni<AddressRequestDto> getByUuid(@QueryParam("uuid") String uuid);
}
