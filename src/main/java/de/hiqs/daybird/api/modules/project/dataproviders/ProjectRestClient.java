package de.hiqs.daybird.api.modules.project.dataproviders;

import de.hiqs.daybird.api.modules.project.dataproviders.models.ProjectRequestDto;
import de.hiqs.daybird.api.modules.project.dataproviders.models.ProjectRequestPostDto;
import de.hiqs.daybird.api.modules.project.dataproviders.models.ProjectRequestUpdateDto;
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
@Path(ProjectRestClient.PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ProjectRestClient {

    String PATH = "/projects";
    String PATH_NAME = "name";
    String PATH_SIGN = "sign";
    String PATH_START_DATE = "start-date";
    String PATH_END_DATE = "end-date";
    String PATH_SEARCH_DATE = "search-date";
    String PARAM_CUSTOMER_UUID = "customer-uuid";
    String PARAM_ARCHIVED = "archived";

    @POST
    Uni<ProjectRequestDto> addProject(ProjectRequestPostDto projectRequestPostDto);

    @GET
    Uni<List<ProjectRequestDto>> list(
            @QueryParam(PATH_NAME) String name,
            @QueryParam(PATH_SIGN) String sign,
            @QueryParam(PATH_START_DATE) String startDate,
            @QueryParam(PATH_END_DATE) String endDate,
            @QueryParam(PATH_SEARCH_DATE) String searchDate,
            @QueryParam(PARAM_CUSTOMER_UUID) String customerUuid,
            @DefaultValue("false") @QueryParam(PARAM_ARCHIVED) boolean archived);

    @GET
    @Path("/{uuid}")
    Uni<ProjectRequestDto> getProject(@PathParam("uuid") String uuid);

    @DELETE
    @Path("/{uuid}")
    Uni<Void> deleteProject(@PathParam("uuid") String uuid);

    @PUT
    @Path("/{uuid}")
    Uni<ProjectRequestDto> update(@PathParam("uuid") String uuid, ProjectRequestUpdateDto projectRequestUpdateDto);

    @PUT
    @Path("/archive/{uuid}")
    Uni<ProjectRequestDto> updateArchived(@PathParam("uuid") String uuid,
                                          @QueryParam("archived") boolean archived,
                                          @QueryParam("entity_type") EntityTypeEnum entityTypeEnum,
                                          @QueryParam("recursive") boolean recursive);

    @GET
    @Path("/uuid")
    Uni<ProjectRequestDto> getByUuid(@QueryParam("uuid") String uuid);
}
