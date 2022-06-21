package de.hiqs.daybird.api.modules.work_package.entrypoints;

import de.hiqs.daybird.api.modules.work_package.dataproviders.WorkPackageRestClient;
import de.hiqs.daybird.api.modules.work_package.domains.WorkPackageService;
import de.hiqs.daybird.api.modules.work_package.domains.models.WorkPackage;
import de.hiqs.daybird.api.modules.work_package.domains.models.WorkPackageUpdate;
import de.hiqs.daybird.api.modules.work_package.entrypoints.models.WorkPackageDto;
import de.hiqs.daybird.api.modules.work_package.entrypoints.models.WorkPackagePostDto;
import de.hiqs.daybird.api.modules.work_package.entrypoints.models.WorkPackageUpdateDto;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import de.hiqs.daybird.api.shared.entrypoints.AbstractReactiveResource;
import io.smallrye.mutiny.Uni;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(WorkPackageResource.PATH)
@ApplicationScoped
public class WorkPackageResource extends AbstractReactiveResource<WorkPackageDto> {

    public static final String PATH = "/work-packages";

    private final WorkPackageService workPackageService;
    private final ModelMapper modelMapper;

    public WorkPackageResource(WorkPackageService workPackageService, ModelMapper modelMapper) {
        this.workPackageService = workPackageService;
        this.modelMapper = modelMapper;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<WorkPackageDto> addWorkPackage(WorkPackagePostDto workPackageDto) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return workPackageService.addWorkPackage(modelMapper.map(workPackageDto, WorkPackage.class))
                .map(workPackage -> modelMapper.map(workPackage, WorkPackageDto.class));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<WorkPackageDto>> list(@QueryParam(WorkPackageRestClient.PATH_NAME) String name,
                                         @QueryParam(WorkPackageRestClient.PATH_SIGN) String sign,
                                         @QueryParam(WorkPackageRestClient.PATH_START_DATE) String startDate,
                                         @QueryParam(WorkPackageRestClient.PATH_END_DATE) String endDate,
                                         @QueryParam(WorkPackageRestClient.PATH_SEARCH_DATE) String searchDate,
                                         @QueryParam(WorkPackageRestClient.PARAM_PROJECT_UUID) String projectUuid,
                                         @QueryParam(WorkPackageRestClient.PARAM_EMPLOYEE_UUID) String employeeUuid,
                                         @DefaultValue("false") @QueryParam(WorkPackageRestClient.PARAM_ARCHIVED) boolean archived) {
        return workPackageService.list(name, sign, startDate, endDate, searchDate, projectUuid, employeeUuid, archived)
                .map(workPackages -> modelMapper.map(workPackages, new TypeToken<List<WorkPackageDto>>() {
                }.getType()));
    }

    @GET
    @Path("/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<WorkPackageDto> getWorkPackage(@PathParam("uuid") String uuid) {
        return workPackageService.getWorkPackage(uuid)
                .map(workPackage -> modelMapper.map(workPackage, WorkPackageDto.class));
    }

    @DELETE
    @Path("/{uuid}")
    public Uni<Void> deleteWorkPackage(@PathParam("uuid") String uuid) {
        return workPackageService.deleteWorkPackage(uuid);
    }

    @PUT
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<WorkPackageDto> update(@PathParam("uuid") String uuid, WorkPackageUpdateDto workPackageUpdateDto) {
        return workPackageService.update(uuid, modelMapper.map(workPackageUpdateDto, WorkPackageUpdate.class))
                .map(workPackage -> modelMapper.map(workPackage, WorkPackageDto.class));
    }

    @PUT
    @Path("/archive/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<WorkPackageDto> updateArchived(@PathParam("uuid") String uuid,
                                              @QueryParam("archived") boolean archived,
                                              @QueryParam("entity_type") EntityTypeEnum entityTypeEnum,
                                              @QueryParam("recursive") boolean recursive) {
        return workPackageService.updateArchived(uuid, archived, entityTypeEnum, recursive)
                .map(workPackage -> modelMapper.map(workPackage, WorkPackageDto.class));
    }

    @GET
    @Path("/uuid")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<WorkPackageDto> getByUuid(@QueryParam("uuid") String uuid) {
        return workPackageService.getByUuid(uuid)
                .map(workPackage -> modelMapper.map(workPackage, WorkPackageDto.class));
    }
}
