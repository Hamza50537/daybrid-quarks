package de.hiqs.daybird.api.modules.time_entry.dataproviders;

import de.hiqs.daybird.api.modules.time_entry.dataproviders.models.TimeEntryRequestDto;
import de.hiqs.daybird.api.modules.time_entry.dataproviders.models.TimeEntryRequestPostDto;
import de.hiqs.daybird.api.modules.time_entry.dataproviders.models.TimeEntryRequestUpdateDto;
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
@Path(TimeEntryRestClient.PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TimeEntryRestClient {

    String PATH = "/time-entry";
    String PARAM_NAME = "name";
    String PARAM_DATE_FROM = "date-from";
    String PARAM_DATE_TO = "date-to";
    String PARAM_DATE = "date";
    String PARAM_WORKPACKAGE_UUID = "work-package-uuid";
    String PARAM_TIME_ENTRY_TYPE_UUID = "time-entry-type-uuid";
    String PARAM_EMPLOYEE_UUID = "employee-uuid";
    String PARAM_ARCHIVED = "archived";

    @POST
    Uni<TimeEntryRequestDto> addTimeEntry(TimeEntryRequestPostDto timeEntryRequestPostDto);

    @GET
    Uni<List<TimeEntryRequestDto>> list(
            @QueryParam(PARAM_NAME) String name,
            @QueryParam(PARAM_DATE_FROM) String dateFrom,
            @QueryParam(PARAM_DATE_TO) String dateTo,
            @QueryParam(PARAM_DATE) String date,
            @QueryParam(PARAM_WORKPACKAGE_UUID) String workPackageUuid,
            @QueryParam(PARAM_TIME_ENTRY_TYPE_UUID) String timeEntryTypeUuid,
            @QueryParam(PARAM_EMPLOYEE_UUID) String employeeUuid,
            @DefaultValue("false") @QueryParam(PARAM_ARCHIVED) boolean archived);

    @GET
    @Path("/{uuid}")
    Uni<TimeEntryRequestDto> getTimeEntry(@PathParam("uuid") String uuid);

    @DELETE
    @Path("/{uuid}")
    Uni<Void> deleteTimeEntry(@PathParam("uuid") String uuid);

    @PUT
    @Path("/{uuid}")
    Uni<TimeEntryRequestDto> update(@PathParam("uuid") String uuid, TimeEntryRequestUpdateDto timeEntryRequestUpdateDto);

    @PUT
    @Path("/archive/{uuid}")
    Uni<TimeEntryRequestDto> updateArchived(@PathParam("uuid") String uuid,
                                            @QueryParam("archived") boolean archived,
                                            @QueryParam("entity_type") EntityTypeEnum entityTypeEnum,
                                            @QueryParam("recursive") boolean recursive);

    @GET
    @Path("/uuid")
    Uni<TimeEntryRequestDto> getByUuid(@QueryParam("uuid") String uuid);
}
