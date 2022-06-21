package de.hiqs.daybird.api.modules.project.entrypoints.models;

import de.hiqs.daybird.api.modules.customer.entrypoints.models.CustomerDto;
import de.hiqs.daybird.api.modules.employee.entrypoints.models.EmployeeDto;
import de.hiqs.daybird.api.modules.work_package.entrypoints.models.WorkPackageDto;
import de.hiqs.daybird.api.shared.entrypoints.models.AbstractDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ProjectOverviewDto extends AbstractDto {
    private String name;
    private String sign;
    private LocalDate startDate;
    private LocalDate endDate;
    private CustomerDto customer;
    private List<WorkPackageDto> workPackageList;
    private List<EmployeeDto> employeeList;

}
