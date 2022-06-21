package de.hiqs.daybird.api.modules.employee.dataproviders;

import de.hiqs.daybird.api.modules.employee.dataproviders.models.EmployeeRequestDto;
import de.hiqs.daybird.api.modules.employee.dataproviders.models.EmployeeRequestPostDto;
import de.hiqs.daybird.api.modules.employee.dataproviders.models.EmployeeRequestUpdateDto;
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
@Path(EmployeeRestClient.PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface EmployeeRestClient {

    String PATH = "/employees";
    String PARAM_FIRST_NAME = "first-name";
    String PARAM_LAST_NAME = "last-name";
    String PARAM_EMAIL = "email";
    String PARAM_ADDRESS_UUID = "address-uuid";
    String PARAM_JOB_TITLE = "job-title";
    String PARAM_ARCHIVED = "archived";


    @POST
    Uni<EmployeeRequestDto> addEmployee(EmployeeRequestPostDto employeeRequestDto);

    @GET
    Uni<List<EmployeeRequestDto>>list(
            @QueryParam(PARAM_FIRST_NAME) String firstName,
            @QueryParam(PARAM_LAST_NAME) String lastName,
            @QueryParam(PARAM_EMAIL) String email,
            @QueryParam(PARAM_ADDRESS_UUID) String addressUuid,
            @QueryParam(PARAM_JOB_TITLE) String jobTitle,
            @DefaultValue("false") @QueryParam(PARAM_ARCHIVED) boolean archived);


    @GET
    @Path("/{uuid}")
    Uni<EmployeeRequestDto>getEmployee(@PathParam("uuid") String uuid);

    @GET
    @Path("/me")
    Uni<EmployeeRequestDto> getMeEmployee(String email);

    @DELETE
    @Path("/{uuid}")
    Uni<Void>deleteEmployee(@PathParam("uuid") String uuid);

    @PUT
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Uni<EmployeeRequestDto>update(@PathParam("uuid") String uuid, EmployeeRequestUpdateDto employeeRequestUpdateDto);

    @PUT
    @Path("/archive/{uuid}")
    Uni<EmployeeRequestDto>updateArchived(@PathParam("uuid") String uuid,
                                          @QueryParam(PARAM_ARCHIVED) boolean archived,
                                          @QueryParam("entity_type") EntityTypeEnum entityTypeEnum,
                                          @QueryParam("recursive") boolean recursive);

    @GET
    @Path("/uuid")
    Uni<EmployeeRequestDto> getByUuid(@QueryParam("uuid") String uuid);
}

