package de.hiqs.daybird.api.modules.time_entry.entrypoints;

import de.hiqs.daybird.api.modules.time_entry.dataproviders.TimeEntryRestClient;
import de.hiqs.daybird.api.modules.time_entry.domains.TimeEntryService;
import de.hiqs.daybird.api.modules.time_entry.domains.models.TimeEntry;
import de.hiqs.daybird.api.modules.time_entry.domains.models.TimeEntryUpdate;
import de.hiqs.daybird.api.modules.time_entry.entrypoints.models.TimeEntryDto;
import de.hiqs.daybird.api.modules.time_entry.entrypoints.models.TimeEntryPostDto;
import de.hiqs.daybird.api.modules.time_entry.entrypoints.models.TimeEntryUpdateDto;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import de.hiqs.daybird.api.shared.entrypoints.AbstractReactiveResource;
import io.smallrye.mutiny.Uni;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(TimeEntryResource.PATH)
@ApplicationScoped
public class TimeEntryResource extends AbstractReactiveResource<TimeEntryDto> {

    public static final String PATH = "/time-entry";

    private final TimeEntryService timeEntryService;
    private final ModelMapper modelMapper;

    public TimeEntryResource(TimeEntryService timeEntryService, ModelMapper modelMapper) {
        this.timeEntryService = timeEntryService;
        this.modelMapper = modelMapper;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<TimeEntryDto> addTimeEntry(TimeEntryPostDto timeEntryDto) {
        return timeEntryService.addTimeEntry(modelMapper.map(timeEntryDto, TimeEntry.class))
                .map(timeEntry -> modelMapper.map(timeEntry, TimeEntryDto.class));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<TimeEntryDto>> list(
            @QueryParam(TimeEntryRestClient.PARAM_NAME) String name,
            @QueryParam(TimeEntryRestClient.PARAM_DATE_FROM) String dateFrom,
            @QueryParam(TimeEntryRestClient.PARAM_DATE_TO) String dateTo,
            @QueryParam(TimeEntryRestClient.PARAM_DATE) String date,
            @QueryParam(TimeEntryRestClient.PARAM_WORKPACKAGE_UUID) String workPackageUuid,
            @QueryParam(TimeEntryRestClient.PARAM_TIME_ENTRY_TYPE_UUID) String timeEntryTypeUuid,
            @QueryParam(TimeEntryRestClient.PARAM_EMPLOYEE_UUID) String employeeUuid,
            @DefaultValue("false") @QueryParam(TimeEntryRestClient.PARAM_ARCHIVED) boolean archived) {
        return timeEntryService.list(name, dateFrom, dateTo, date, workPackageUuid, timeEntryTypeUuid, employeeUuid, archived)
                .map(timeEntries -> modelMapper.map(timeEntries, new TypeToken<List<TimeEntryDto>>() {
                }.getType()));
    }

    @GET
    @Path("/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<TimeEntryDto> getTimeEntry(@PathParam("uuid") String uuid) {
        return timeEntryService.getTimeEntry(uuid)
                .map(timeEntry -> modelMapper.map(timeEntry, TimeEntryDto.class));
    }

    @DELETE
    @Path("/{uuid}")
    public Uni<Void> deleteTimeEntry(@PathParam("uuid") String uuid) {
        return timeEntryService.deleteTimeEntry(uuid);
    }

    @PUT
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<TimeEntryDto> update(@PathParam("uuid") String uuid, TimeEntryUpdateDto timeEntryUpdateDto) {
        return timeEntryService.update(uuid, modelMapper.map(timeEntryUpdateDto, TimeEntryUpdate.class))
                .map(timeEntry -> modelMapper.map(timeEntry, TimeEntryDto.class));
    }

    @PUT
    @Path("/archive/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<TimeEntryDto> updateArchived(@PathParam("uuid") String uuid,
                                            @QueryParam("archived") boolean archived,
                                            @QueryParam("entity_type") EntityTypeEnum entityTypeEnum,
                                            @QueryParam("recursive") boolean recursive){
        return timeEntryService.updateArchived(uuid, archived, entityTypeEnum, recursive)
                .map(timeEntry -> modelMapper.map(timeEntry, TimeEntryDto.class));
    }

    @GET
    @Path("/uuid")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<TimeEntryDto> getByUuid(@QueryParam("uuid") String uuid){
        return timeEntryService.getByUuid(uuid)
                .map(timeEntry -> modelMapper.map(timeEntry, TimeEntryDto.class));
    }
}
