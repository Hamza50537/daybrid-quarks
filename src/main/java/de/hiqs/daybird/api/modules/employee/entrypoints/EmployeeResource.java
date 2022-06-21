package de.hiqs.daybird.api.modules.employee.entrypoints;

import de.hiqs.daybird.api.modules.employee.dataproviders.EmployeeRestClient;
import de.hiqs.daybird.api.modules.employee.domains.EmployeeService;
import de.hiqs.daybird.api.modules.employee.domains.models.Employee;
import de.hiqs.daybird.api.modules.employee.domains.models.EmployeeUpdate;
import de.hiqs.daybird.api.modules.employee.entrypoints.models.EmployeeDto;
import de.hiqs.daybird.api.modules.employee.entrypoints.models.EmployeePostDto;
import de.hiqs.daybird.api.modules.employee.entrypoints.models.EmployeeUpdateDto;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import de.hiqs.daybird.api.shared.entrypoints.AbstractReactiveResource;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(EmployeeResource.PATH)
@ApplicationScoped
public class EmployeeResource extends AbstractReactiveResource<EmployeeDto> {

    public static final String PATH = "/employees";

    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;

    @Inject
    JsonWebToken jsonWebToken;

    public EmployeeResource(EmployeeService employeeService, ModelMapper modelMapper) {
        this.employeeService = employeeService;
        this.modelMapper = modelMapper;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<EmployeeDto> addEmployee(EmployeePostDto employeeDto) {
        return employeeService.addEmployee(modelMapper.map(employeeDto, Employee.class))
                .map(employee -> modelMapper.map(employee, EmployeeDto.class));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<EmployeeDto>> list(@QueryParam(EmployeeRestClient.PARAM_FIRST_NAME) String firstname,
                                      @QueryParam(EmployeeRestClient.PARAM_LAST_NAME) String lastname,
                                      @QueryParam(EmployeeRestClient.PARAM_EMAIL) String Email,
                                      @QueryParam(EmployeeRestClient.PARAM_ADDRESS_UUID) String addressUuid,
                                      @QueryParam(EmployeeRestClient.PARAM_JOB_TITLE) String jobTitle,
                                      @DefaultValue("false") @QueryParam(EmployeeRestClient.PARAM_ARCHIVED) boolean archived) {
        return employeeService.list(firstname, lastname, Email, addressUuid, jobTitle, archived)
                .map(employees -> modelMapper.map(employees, new TypeToken<List<EmployeeDto>>() {
                }.getType()));
    }

    @GET
    @Path("/{uuid}")
    public Uni<EmployeeDto> getEmployee(@PathParam("uuid") String uuid) {
        return employeeService.getByUuid(uuid)
                .map(employee -> modelMapper.map(employee, EmployeeDto.class));
    }

    @GET
    @Path("/me")
    public Uni<EmployeeDto> getMeEmployee() {
        return employeeService.getByEmail(jsonWebToken.getClaim("email"))
                .map(employee -> modelMapper.map(employee, EmployeeDto.class));
    }

    @DELETE
    @Path("/{uuid}")
    public Uni<Void> deleteEmployee(@PathParam("uuid") String uuid) { return employeeService.deleteEmployee(uuid); }

    @PUT
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<EmployeeDto> update(@PathParam("uuid") String uuid, EmployeeUpdateDto employeeUpdateDto) {
        return employeeService.update(uuid, modelMapper.map(employeeUpdateDto, EmployeeUpdate.class))
                .map(employee -> modelMapper.map(employee, EmployeeDto.class));
    }

    @PUT
    @Path("/archive/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<EmployeeDto> updateArchived(@PathParam("uuid") String uuid,
                                           @QueryParam(EmployeeRestClient.PARAM_ARCHIVED) boolean archived,
                                           @QueryParam("entity_type")EntityTypeEnum entityTypeEnum,
                                           @QueryParam("recursive") boolean recursive) {
        return employeeService.updateArchived(uuid, archived, entityTypeEnum, recursive)
                .map(employee -> modelMapper.map(employee, EmployeeDto.class));
    }

    @GET
    @Path("/uuid")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<EmployeeDto> getByUuid(@QueryParam("uuid") String uuid) {
        return employeeService.getByUuid(uuid)
                .map(employee -> modelMapper.map(employee, EmployeeDto.class));
    }
}
