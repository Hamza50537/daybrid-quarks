package de.hiqs.daybird.api.modules.work_package.dataproviders;

import de.hiqs.daybird.api.modules.work_package.dataproviders.models.WorkPackageRequestPostDto;
import de.hiqs.daybird.api.modules.work_package.dataproviders.models.WorkPackageRequestUpdateDto;
import de.hiqs.daybird.api.modules.work_package.domains.models.WorkPackage;
import de.hiqs.daybird.api.modules.work_package.domains.models.WorkPackageUpdate;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class WorkPackageAdapter {

    private final WorkPackageRestClient workPackageRestClient;
    private final ModelMapper modelMapper;

    public WorkPackageAdapter(@RestClient WorkPackageRestClient workPackageRestClient, ModelMapper modelMapper) {
        this.workPackageRestClient = workPackageRestClient;
        this.modelMapper = modelMapper;
    }

    public Uni<WorkPackage> addWorkPackage(WorkPackage workPackage) {
        return workPackageRestClient.addWorkPackage(modelMapper.map(workPackage, WorkPackageRequestPostDto.class))
                .map(workPackageRequestDto -> modelMapper.map(workPackageRequestDto, WorkPackage.class));
    }

    public Uni<WorkPackage> getWorkPackage(String uuid) {
        return workPackageRestClient.getWorkPackage(uuid)
                .map(workPackageRequestDto -> modelMapper.map(workPackageRequestDto, WorkPackage.class));
    }

    public Uni<WorkPackage> getByUuid(String uuid) {
        return workPackageRestClient.getByUuid(uuid)
                .map(workPackageRequestDto -> modelMapper.map(workPackageRequestDto, WorkPackage.class));
    }

    public Uni<List<WorkPackage>> list(String name, String sign, String startDate, String endDate,
                                      String searchDate, String projectUuid, String employeeUuid, boolean archived) {
        return workPackageRestClient.list(name, sign, startDate, endDate, searchDate, projectUuid, employeeUuid, archived)
                .map(workPackageRequestDtos -> modelMapper.map(workPackageRequestDtos, new TypeToken<List<WorkPackage>>() {
                }.getType()));
    }

    public Uni<Void> deleteWorkPackage(String uuid) {
        return workPackageRestClient.deleteWorkPackage(uuid);
    }

    public Uni<WorkPackage> update(String uuid, WorkPackageUpdate workPackageUpdate) {
        return workPackageRestClient.update(uuid, modelMapper.map(workPackageUpdate, WorkPackageRequestUpdateDto.class))
                .map(workPackageRequestDto -> modelMapper.map(workPackageRequestDto, WorkPackage.class));
    }

    public Uni<WorkPackage> updateArchived(String uuid, boolean archived, EntityTypeEnum entityTypeEnum, boolean recursive) {
        return workPackageRestClient.updateArchived(uuid, archived, entityTypeEnum, recursive)
                .map(workPackageRequestDto -> modelMapper.map(workPackageRequestDto, WorkPackage.class));
    }
}
