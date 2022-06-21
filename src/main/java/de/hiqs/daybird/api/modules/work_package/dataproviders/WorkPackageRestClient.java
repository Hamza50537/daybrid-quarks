package de.hiqs.daybird.api.modules.work_package.dataproviders;

import de.hiqs.daybird.api.modules.work_package.dataproviders.models.WorkPackageRequestDto;
import de.hiqs.daybird.api.modules.work_package.dataproviders.models.WorkPackageRequestPostDto;
import de.hiqs.daybird.api.modules.work_package.dataproviders.models.WorkPackageRequestUpdateDto;
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
@Path(WorkPackageRestClient.PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface WorkPackageRestClient {

    String PATH = "/work-packages";
    String PATH_NAME = "name";
    String PATH_SIGN = "sign";
    String PATH_START_DATE = "start-date";
    String PATH_END_DATE = "end-date";
    String PATH_SEARCH_DATE = "search-date";
    String PARAM_PROJECT_UUID = "project-uuid";
    String PARAM_EMPLOYEE_UUID = "employee-uuid";
    String PARAM_ARCHIVED = "archived";

    @POST
    Uni<WorkPackageRequestDto> addWorkPackage(WorkPackageRequestPostDto workPackageRequestPostDto);

    @GET
    Uni<List<WorkPackageRequestDto>> list(@QueryParam(PATH_NAME) String name,
                                         @QueryParam(PATH_SIGN) String sign,
                                         @QueryParam(PATH_START_DATE) String startDate,
                                         @QueryParam(PATH_END_DATE) String endDate,
                                         @QueryParam(PATH_SEARCH_DATE) String searchDate,
                                         @QueryParam(PARAM_PROJECT_UUID) String projectUuid,
                                         @QueryParam(PARAM_EMPLOYEE_UUID) String employeeUuid,
                                         @DefaultValue("false") @QueryParam(PARAM_ARCHIVED) boolean archived);

    @GET
    @Path("/{uuid}")
    Uni<WorkPackageRequestDto> getWorkPackage(@PathParam("uuid") String uuid);

    @DELETE
    @Path("/{uuid}")
    Uni<Void> deleteWorkPackage(@PathParam("uuid") String uuid);

    @PUT
    @Path("/{uuid}")
    Uni<WorkPackageRequestDto> update(@PathParam("uuid") String uuid, WorkPackageRequestUpdateDto workPackageRequestUpdateDto);

    @PUT
    @Path("/archive/{uuid}")
    Uni<WorkPackageRequestDto> updateArchived(@PathParam("uuid") String uuid,
                                       @QueryParam("archived") boolean archived,
                                       @QueryParam("entity_type") EntityTypeEnum entityTypeEnum,
                                       @QueryParam("recursive") boolean recursive);

    @GET
    @Path("/uuid")
    Uni<WorkPackageRequestDto> getByUuid(@QueryParam("uuid") String uuid);
}
