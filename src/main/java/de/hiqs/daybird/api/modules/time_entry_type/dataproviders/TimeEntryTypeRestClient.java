package de.hiqs.daybird.api.modules.time_entry_type.dataproviders;

import de.hiqs.daybird.api.modules.time_entry_type.dataproviders.models.TimeEntryTypeRequestDto;
import de.hiqs.daybird.api.modules.time_entry_type.dataproviders.models.TimeEntryTypeRequestPostDto;
import de.hiqs.daybird.api.modules.time_entry_type.dataproviders.models.TimeEntryTypeRequestUpdateDto;
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
@Path(TimeEntryTypeRestClient.PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TimeEntryTypeRestClient {

    String PATH = "/time-entry-type";
    String PARAM_TYPE_NAME = "type-name";
    String PARAM_NAME = "name";
    String PARAM_START = "start-time";
    String PARAM_END = "end-time";
    String PARAM_DATE = "date";
    String PARAM_DESCRIPTION = "description";
    String PARAM_WORKPACKAGE_UUID = "work-package-uuid";
    String PARAM_EMPLOYEE_UUID = "employee-uuid";
    String PARAM_ARCHIVED = "archived";

    @POST
    Uni<TimeEntryTypeRequestDto> addTimeEntryType(TimeEntryTypeRequestPostDto timeEntryTypeRequestPostDto);

    @GET
    Uni<List<TimeEntryTypeRequestDto>> list(
            @QueryParam(PARAM_TYPE_NAME) String typeName,
            @QueryParam(PARAM_NAME) String name,
            @QueryParam(PARAM_START) String startTime,
            @QueryParam(PARAM_END) String endTime,
            @QueryParam(PARAM_DATE) String date,
            @QueryParam(PARAM_DESCRIPTION) String description,
            @QueryParam(PARAM_WORKPACKAGE_UUID) String workPackageUuid,
            @QueryParam(PARAM_EMPLOYEE_UUID) String employeeUuid,
            @DefaultValue("false") @QueryParam(PARAM_ARCHIVED) boolean archived);

    @GET
    @Path("/{uuid}")
    Uni<TimeEntryTypeRequestDto> getTimeEntryType(@PathParam("uuid") String uuid);

    @DELETE
    @Path("/{uuid}")
    Uni<Void> deleteTimeEntryType(@PathParam("uuid") String uuid);

    @PUT
    @Path("/{uuid}")
    Uni<TimeEntryTypeRequestDto> update(@PathParam("uuid") String uuid, TimeEntryTypeRequestUpdateDto timeEntryTypeRequestUpdateDto);

    @PUT
    @Path("/archive/{uuid}")
    Uni<TimeEntryTypeRequestDto> updateArchived(@PathParam("uuid") String uuid,
                                                @QueryParam("archived") boolean archived,
                                                @QueryParam("entity_type") EntityTypeEnum entityTypeEnum,
                                                @QueryParam("recursive") boolean recursive);

    @GET
    @Path("/uuid")
    Uni<TimeEntryTypeRequestDto> getByUuid(@QueryParam("uuid") String uuid);
}
