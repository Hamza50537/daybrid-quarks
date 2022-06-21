package de.hiqs.daybird.api.modules.project.domains.models;

import de.hiqs.daybird.api.modules.customer.domains.models.Customer;
import de.hiqs.daybird.api.modules.employee.domains.models.Employee;
import de.hiqs.daybird.api.modules.work_package.domains.models.WorkPackage;
import de.hiqs.daybird.api.shared.domains.models.AbstractBaseModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProjectOverview extends AbstractBaseModel {
    private String name;
    private String sign;
    private LocalDate startDate;
    private LocalDate endDate;
    private Customer customer;
    private List<WorkPackage> workPackageList = new ArrayList<>();
    private List<Employee> employeeList = new ArrayList<>();

}
