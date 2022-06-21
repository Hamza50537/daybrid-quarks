package de.hiqs.daybird.api.modules.employee.dataproviders;

import de.hiqs.daybird.api.modules.employee.dataproviders.models.EmployeeRequestPostDto;
import de.hiqs.daybird.api.modules.employee.dataproviders.models.EmployeeRequestUpdateDto;
import de.hiqs.daybird.api.modules.employee.domains.models.Employee;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class EmployeeAdapter {

    private final EmployeeRestClient employeeRestClient;
    private final ModelMapper modelMapper;

    public EmployeeAdapter(@RestClient EmployeeRestClient employeeRestClient, ModelMapper modelMapper) {
        this.employeeRestClient = employeeRestClient;
        this.modelMapper = modelMapper;
    }


    public Uni<Employee> getByUuid(String uuid) {
        return employeeRestClient.getByUuid(uuid)
                .map(employeeRequestDto -> modelMapper.map(employeeRequestDto, Employee.class));
    }

    public Uni<Employee> getByEmail(String email) {
        return employeeRestClient.getMeEmployee(email)
                .map(employeeRequestDto -> modelMapper.map(employeeRequestDto, Employee.class));
    }


    public Uni<Employee> update(String uuid, EmployeeRequestUpdateDto employeeRequestUpdateDto) {
        return employeeRestClient.update(uuid, employeeRequestUpdateDto)
                .map(employeeRequestDto -> modelMapper.map(employeeRequestDto, Employee.class));
    }

    public Uni<Employee> updateArchived(String uuid, boolean archived, EntityTypeEnum entityTypeEnum, boolean recursive) {
        return employeeRestClient.updateArchived(uuid, archived, entityTypeEnum, recursive)
                .map(employeeRequestDto -> modelMapper.map(employeeRequestDto, Employee.class));
    }

    public Uni<Void> deleteEmployee(String uuid) { return employeeRestClient.deleteEmployee(uuid); }

    public Uni<List<Employee>> list(String firstname, String lastname, String email, String addressUuid, String jobTitle, boolean archived) {
        return employeeRestClient.list(firstname, lastname, email, addressUuid, jobTitle, archived)
                .map(employeeRequestDtos -> modelMapper.map(employeeRequestDtos, new TypeToken<List<Employee>>() {
                }.getType()));
    }

    public Uni<Employee> addEmployee(Employee employee) {
        return employeeRestClient.addEmployee(modelMapper.map(employee, EmployeeRequestPostDto.class))
                .map(employeeRequestDto -> modelMapper.map(employeeRequestDto, Employee.class));
    }
}
