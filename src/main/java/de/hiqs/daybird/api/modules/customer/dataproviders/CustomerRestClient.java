package de.hiqs.daybird.api.modules.customer.dataproviders;

import de.hiqs.daybird.api.modules.customer.dataproviders.models.CustomerRequestDto;
import de.hiqs.daybird.api.modules.customer.dataproviders.models.CustomerRequestPostDto;
import de.hiqs.daybird.api.modules.customer.dataproviders.models.CustomerRequestUpdateDto;
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
@Path(CustomerRestClient.PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface CustomerRestClient {

    String PATH = "/customers";
    String PARAM_NAME = "name";
    String PARAM_SIGN = "sign";
    String PARAM_EMAIL = "email";
    String PARAM_PHONE_NUMBER = "phone-number";
    String PARAM_ADDRESS_UUID = "address-uuid";
    String PARAM_ARCHIVED = "archived";

    @POST
    Uni<CustomerRequestDto> addCustomer(CustomerRequestPostDto customerRequestPostDto);

    @GET
    Uni<List<CustomerRequestDto>> list(
            @QueryParam(PARAM_NAME) String name,
            @QueryParam(PARAM_SIGN) String sign,
            @QueryParam(PARAM_EMAIL) String email,
            @QueryParam(PARAM_PHONE_NUMBER) String phoneNumber,
            @QueryParam(PARAM_ADDRESS_UUID) String addressUuid,
            @DefaultValue("false") @QueryParam(PARAM_ARCHIVED) boolean archived);

    @GET
    @Path("/{uuid}")
    Uni<CustomerRequestDto> getCustomer(@PathParam("uuid") String uuid);

    @GET
    @Path("/uuid")
    Uni<CustomerRequestDto> getByUuid(@QueryParam("uuid") String uuid);

    @DELETE
    @Path("/{uuid}")
    Uni<Void> deleteCustomer(@PathParam("uuid") String uuid);

    @PUT
    @Path("/{uuid}")
    Uni<CustomerRequestDto> update(@PathParam("uuid") String uuid, CustomerRequestUpdateDto customerRequestUpdateDto);

    @PUT
    @Path("/archive/{uuid}")
    Uni<CustomerRequestDto> updateArchived(@PathParam("uuid") String uuid,
                                           @QueryParam(CustomerRestClient.PARAM_ARCHIVED) boolean archived,
                                           @QueryParam("entity_type") EntityTypeEnum entityTypeEnum,
                                           @QueryParam("recursive") boolean recursive);
}
