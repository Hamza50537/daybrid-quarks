package de.hiqs.daybird.api.modules.employee.domains;

import de.hiqs.daybird.api.modules.employee.dataproviders.EmployeeAdapter;
import de.hiqs.daybird.api.modules.employee.dataproviders.models.EmployeeRequestUpdateDto;
import de.hiqs.daybird.api.modules.employee.domains.models.Employee;
import de.hiqs.daybird.api.modules.employee.domains.models.EmployeeUpdate;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import io.smallrye.mutiny.Uni;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class EmployeeService {

    private final EmployeeAdapter employeeAdapter;
    private final ModelMapper modelMapper;

    public EmployeeService(EmployeeAdapter employeeAdapter, ModelMapper modelMapper) {
        this.employeeAdapter = employeeAdapter;
        this.modelMapper = modelMapper;
    }

    public Uni<Employee> getByUuid(String uuid) {
        return employeeAdapter.getByUuid(uuid);
    }


    public Uni<Employee> getByEmail(String email) {
        return employeeAdapter.getByEmail(email);
    }

    public Uni<Employee> updateArchived(String uuid, boolean archived, EntityTypeEnum entityTypeEnum, boolean recursive) {
        return employeeAdapter.updateArchived(uuid, archived, entityTypeEnum, recursive);
    }

    public Uni<Employee> update(String uuid, EmployeeUpdate employeeUpdate) {
        return employeeAdapter.update(uuid, modelMapper.map(employeeUpdate, EmployeeRequestUpdateDto.class));
    }

    public Uni<Void> deleteEmployee(String uuid) { return employeeAdapter.deleteEmployee(uuid); }

    public Uni<List<Employee>> list(String firstname, String lastname, String email, String addressUuid, String jobTitle, boolean archived) {
        return employeeAdapter.list(firstname, lastname, email, addressUuid, jobTitle, archived);
    }

    public Uni<Employee> addEmployee(Employee employee) {
        return employeeAdapter.addEmployee(employee);
    }
}
