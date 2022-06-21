package de.hiqs.daybird.api.modules.time_entry_type.entrypoints;

import de.hiqs.daybird.api.modules.time_entry_type.dataproviders.TimeEntryTypeRestClient;
import de.hiqs.daybird.api.modules.time_entry_type.domains.TimeEntryTypeService;
import de.hiqs.daybird.api.modules.time_entry_type.domains.models.TimeEntryType;
import de.hiqs.daybird.api.modules.time_entry_type.domains.models.TimeEntryTypeUpdate;
import de.hiqs.daybird.api.modules.time_entry_type.entrypoints.models.TimeEntryTypeDto;
import de.hiqs.daybird.api.modules.time_entry_type.entrypoints.models.TimeEntryTypePostDto;
import de.hiqs.daybird.api.modules.time_entry_type.entrypoints.models.TimeEntryTypeUpdateDto;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import de.hiqs.daybird.api.shared.entrypoints.AbstractReactiveResource;
import io.smallrye.mutiny.Uni;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(TimeEntryTypeResource.PATH)
@ApplicationScoped
public class TimeEntryTypeResource extends AbstractReactiveResource<TimeEntryTypeDto> {

    public static final String PATH = "/time-entry-type";

    private final TimeEntryTypeService timeEntryTypeService;
    private final ModelMapper modelMapper;

    public TimeEntryTypeResource(TimeEntryTypeService timeEntryTypeService, ModelMapper modelMapper) {
        this.timeEntryTypeService = timeEntryTypeService;
        this.modelMapper = modelMapper;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<TimeEntryTypeDto> addTimeEntryType(TimeEntryTypePostDto timeEntryTypeDto) {
        return timeEntryTypeService.addTimeEntryType(modelMapper.map(timeEntryTypeDto, TimeEntryType.class))
                .map(timeEntryType -> modelMapper.map(timeEntryType, TimeEntryTypeDto.class));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<TimeEntryTypeDto>> list(
            @QueryParam(TimeEntryTypeRestClient.PARAM_TYPE_NAME) String typeName,
            @QueryParam(TimeEntryTypeRestClient.PARAM_NAME) String name,
            @QueryParam(TimeEntryTypeRestClient.PARAM_START) String startTime,
            @QueryParam(TimeEntryTypeRestClient.PARAM_END) String endTime,
            @QueryParam(TimeEntryTypeRestClient.PARAM_DATE) String date,
            @QueryParam(TimeEntryTypeRestClient.PARAM_DESCRIPTION) String description,
            @QueryParam(TimeEntryTypeRestClient.PARAM_WORKPACKAGE_UUID) String workPackageUuid,
            @QueryParam(TimeEntryTypeRestClient.PARAM_EMPLOYEE_UUID) String employeeUuid,
            @DefaultValue("false") @QueryParam(TimeEntryTypeRestClient.PARAM_ARCHIVED) boolean archived) {
        return timeEntryTypeService.list(typeName, name, startTime, endTime, date, description, workPackageUuid, employeeUuid, archived)
                .map(timeEntryTypes -> modelMapper.map(timeEntryTypes, new TypeToken<List<TimeEntryTypeDto>>() {
                }.getType()));
    }

    @GET
    @Path("/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<TimeEntryTypeDto> getTimeEntryType(@PathParam("uuid") String uuid) {
        return timeEntryTypeService.getTimeEntryType(uuid)
                .map(timeEntryType -> modelMapper.map(timeEntryType, TimeEntryTypeDto.class));
    }

    @DELETE
    @Path("/{uuid}")
    public Uni<Void> deleteTimeEntryType(@PathParam("uuid") String uuid) {
        return timeEntryTypeService.deleteTimeEntryType(uuid);
    }

    @PUT
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<TimeEntryTypeDto> update(@PathParam("uuid") String uuid, TimeEntryTypeUpdateDto timeEntryTypeUpdateDto) {
        return timeEntryTypeService.update(uuid, modelMapper.map(timeEntryTypeUpdateDto, TimeEntryTypeUpdate.class))
                .map(timeEntryType -> modelMapper.map(timeEntryType, TimeEntryTypeDto.class));
    }

    @PUT
    @Path("/archive/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<TimeEntryTypeDto> updateArchived(@PathParam("uuid") String uuid,
                                                @QueryParam("archived") boolean archived,
                                                @QueryParam("entity_type") EntityTypeEnum entityTypeEnum,
                                                @QueryParam("recursive") boolean recursive) {
        return timeEntryTypeService.updateArchived(uuid, archived, entityTypeEnum, recursive)
                .map(timeEntryType -> modelMapper.map(timeEntryType, TimeEntryTypeDto.class));
    }

    @GET
    @Path("/uuid")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<TimeEntryTypeDto> getByUuid(@QueryParam("uuid") String uuid) {
        return timeEntryTypeService.getByUuid(uuid)
                .map(timeEntryType -> modelMapper.map(timeEntryType, TimeEntryTypeDto.class));
    }
}
