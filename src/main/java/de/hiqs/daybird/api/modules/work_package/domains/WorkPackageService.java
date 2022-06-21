package de.hiqs.daybird.api.modules.work_package.domains;

import de.hiqs.daybird.api.modules.work_package.dataproviders.WorkPackageAdapter;
import de.hiqs.daybird.api.modules.work_package.domains.models.WorkPackage;
import de.hiqs.daybird.api.modules.work_package.domains.models.WorkPackageUpdate;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class WorkPackageService {

    private final WorkPackageAdapter workPackageAdapter;

    public WorkPackageService(WorkPackageAdapter workPackageAdapter) {
        this.workPackageAdapter = workPackageAdapter;
    }

    public Uni<WorkPackage> addWorkPackage(WorkPackage workPackage) {
        return workPackageAdapter.addWorkPackage(workPackage);
    }

    public Uni<WorkPackage> getWorkPackage(String uuid) {
        return workPackageAdapter.getWorkPackage(uuid);
    }

    public Uni<WorkPackage> getByUuid(String uuid) {
        return workPackageAdapter.getByUuid(uuid);
    }

    public Uni<List<WorkPackage>> list(String name, String sign, String startDate, String endDate,
                                      String searchDate, String projectUuid, String employeeUuid, boolean archived) {
        return workPackageAdapter.list(name, sign, startDate, endDate, searchDate, projectUuid, employeeUuid, archived);
    }

    public Uni<Void> deleteWorkPackage(String uuid) {
        return workPackageAdapter.deleteWorkPackage(uuid);
    }

    public Uni<WorkPackage> update(String uuid, WorkPackageUpdate workPackageUpdate) {
        return workPackageAdapter.update(uuid, workPackageUpdate);
    }

    public Uni<WorkPackage> updateArchived(String uuid, boolean archived, EntityTypeEnum entityTypeEnum, boolean recursive) {
        return workPackageAdapter.updateArchived(uuid, archived, entityTypeEnum, recursive);
    }
}
