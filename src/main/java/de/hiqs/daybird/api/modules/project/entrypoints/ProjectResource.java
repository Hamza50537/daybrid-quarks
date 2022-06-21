package de.hiqs.daybird.api.modules.project.entrypoints;

import de.hiqs.daybird.api.modules.project.dataproviders.ProjectRestClient;
import de.hiqs.daybird.api.modules.project.domains.ProjectComplexService;
import de.hiqs.daybird.api.modules.project.domains.ProjectService;
import de.hiqs.daybird.api.modules.project.domains.models.Project;
import de.hiqs.daybird.api.modules.project.domains.models.ProjectUpdate;
import de.hiqs.daybird.api.modules.project.entrypoints.models.ProjectDto;
import de.hiqs.daybird.api.modules.project.entrypoints.models.ProjectOverviewDto;
import de.hiqs.daybird.api.modules.project.entrypoints.models.ProjectPostDto;
import de.hiqs.daybird.api.modules.project.entrypoints.models.ProjectUpdateDto;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import de.hiqs.daybird.api.shared.entrypoints.AbstractReactiveResource;
import io.smallrye.mutiny.Uni;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(ProjectResource.PATH)
@ApplicationScoped
public class ProjectResource extends AbstractReactiveResource<ProjectDto> {

    public static final String PATH = "/projects";

    private final ProjectService projectService;
    private final ProjectComplexService projectComplexService;
    private final ModelMapper modelMapper;

    public ProjectResource(ProjectService projectService, ProjectComplexService projectComplexService,
                           ModelMapper modelMapper) {
        this.projectService = projectService;
        this.projectComplexService = projectComplexService;
        this.modelMapper = modelMapper;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<ProjectDto> addProject(ProjectPostDto projectDto) {
        return projectService.addProject(modelMapper.map(projectDto, Project.class))
                .map(project -> modelMapper.map(project, ProjectDto.class));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<ProjectDto>> list(
            @QueryParam(ProjectRestClient.PATH_NAME) String name,
            @QueryParam(ProjectRestClient.PATH_SIGN) String sign,
            @QueryParam(ProjectRestClient.PATH_START_DATE) String startDate,
            @QueryParam(ProjectRestClient.PATH_END_DATE) String endDate,
            @QueryParam(ProjectRestClient.PATH_SEARCH_DATE) String searchDate,
            @QueryParam(ProjectRestClient.PARAM_CUSTOMER_UUID) String customerUuid,
            @DefaultValue("false") @QueryParam(ProjectRestClient.PARAM_ARCHIVED) boolean archived) {
        return projectService.list(name, sign, startDate, endDate, searchDate, customerUuid, archived)
                .map(projects -> modelMapper.map(projects, new TypeToken<List<ProjectDto>>() {
                }.getType()));
    }

    @GET
    @Path("/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<ProjectDto> getProject(@PathParam("uuid") String uuid) {
        return projectService.getProject(uuid)
                .map(project -> modelMapper.map(project, ProjectDto.class));
    }

    @DELETE
    @Path("/{uuid}")
    public Uni<Void> deleteProject(@PathParam("uuid") String uuid) {
        return projectService.deleteProject(uuid);
    }

    @PUT
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<ProjectDto> update(@PathParam("uuid") String uuid, ProjectUpdateDto projectUpdateDto) {
        return projectService.update(uuid, modelMapper.map(projectUpdateDto, ProjectUpdate.class))
                .map(project -> modelMapper.map(project, ProjectDto.class));
    }

    @PUT
    @Path("/archive/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<ProjectDto> updateArchived(@PathParam("uuid") String uuid,
                                          @QueryParam("archived") boolean archived,
                                          @QueryParam("entity_type") EntityTypeEnum entityTypeEnum,
                                          @QueryParam("recursive") boolean recursive) {
        return projectService.updateArchived(uuid, archived, entityTypeEnum, recursive)
                .map(project -> modelMapper.map(project, ProjectDto.class));
    }

    @GET
    @Path("/uuid")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<ProjectDto> getByUuid(@QueryParam("uuid") String uuid) {
        return projectService.getByUuid(uuid)
                .map(project -> modelMapper.map(project, ProjectDto.class));
    }

    @GET
    @Path("/overview")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<ProjectOverviewDto>> getProjectOverviewDtoListByCustomerUuid(
            @QueryParam(ProjectRestClient.PARAM_CUSTOMER_UUID) String customerUuid,
            @DefaultValue("false") @QueryParam(ProjectRestClient.PARAM_ARCHIVED) boolean archived) {
        return projectComplexService.getProjectOverviewDtoListByCustomerUuid(customerUuid, archived)
                .map(projects -> modelMapper.map(projects, new TypeToken<List<ProjectOverviewDto>>() {
                }.getType()));
    }
}
