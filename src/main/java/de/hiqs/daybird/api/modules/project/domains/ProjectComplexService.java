package de.hiqs.daybird.api.modules.project.domains;

import de.hiqs.daybird.api.modules.customer.dataproviders.CustomerAdapter;
import de.hiqs.daybird.api.modules.customer.domains.models.Customer;
import de.hiqs.daybird.api.modules.employee.dataproviders.EmployeeAdapter;
import de.hiqs.daybird.api.modules.project.dataproviders.ProjectAdapter;
import de.hiqs.daybird.api.modules.project.domains.models.Project;
import de.hiqs.daybird.api.modules.project.domains.models.ProjectOverview;
import de.hiqs.daybird.api.modules.work_package.dataproviders.WorkPackageAdapter;
import de.hiqs.daybird.api.modules.work_package.domains.models.WorkPackage;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ProjectComplexService {

    private final ProjectAdapter projectAdapter;
    private final CustomerAdapter customerAdapter;
    private final WorkPackageAdapter workPackageAdapter;
    private final EmployeeAdapter employeeAdapter;

    public ProjectComplexService(ProjectAdapter projectAdapter, CustomerAdapter customerAdapter,
                                 WorkPackageAdapter workPackageAdapter, EmployeeAdapter employeeAdapter) {
        this.projectAdapter = projectAdapter;
        this.customerAdapter = customerAdapter;
        this.workPackageAdapter = workPackageAdapter;
        this.employeeAdapter = employeeAdapter;
    }

    public Uni<List<ProjectOverview>> getProjectOverviewDtoListByCustomerUuid(String customerUuid, boolean archived) {
        try {
            Uni<List<Project>> uniProjects = projectAdapter.list(null, null, null, null, null, customerUuid, archived);
            Uni<Customer> uniCustomer = customerAdapter.getByUuid(customerUuid);
            Uni<List<WorkPackage>> uniWorkPackageList = workPackageAdapter.list(null, null, null, null, null, null, null, archived);
            return Uni.combine().all().unis(uniProjects, uniCustomer, uniWorkPackageList)
                    .asTuple().flatMap(tuple -> makeNewUniProjectOverviewList(tuple.getItem1(), tuple.getItem2(), tuple.getItem3()));
        } catch (Exception e) {
            return null;
        }
    }

    private Uni<List<ProjectOverview>> makeNewUniProjectOverviewList(List<Project> projectList, Customer customer, List<WorkPackage> workPackageList) {
        List<ProjectOverview> projectOverviewList = new ArrayList<>();
        projectList.forEach(project -> {
            ProjectOverview projectOverview = createProjectOverview(project, customer);
            projectOverview.getWorkPackageList().addAll(workPackageList.stream()
                    .filter(workPackage -> workPackage.getProjectUuid() != null &&
                            workPackage.getProjectUuid().equalsIgnoreCase(projectOverview.getUuid()))
                    .toList());
            projectOverviewList.add(projectOverview);
        });
        return Multi.createFrom().items(projectOverviewList).toUni();
    }

    private ProjectOverview createProjectOverview(Project project, Customer customer) {
        ProjectOverview projectOverview = new ProjectOverview();
        projectOverview.setName(project.getName());
        projectOverview.setUuid(project.getUuid());
        projectOverview.setSign(project.getSign());
        projectOverview.setStartDate(project.getStartDate());
        projectOverview.setEndDate(project.getEndDate());
        projectOverview.setArchived(project.isArchived());
        projectOverview.setCustomer(customer);
        return projectOverview;
    }


}
