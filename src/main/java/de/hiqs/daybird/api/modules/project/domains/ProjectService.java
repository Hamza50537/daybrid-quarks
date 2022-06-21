package de.hiqs.daybird.api.modules.project.domains;

import de.hiqs.daybird.api.modules.project.dataproviders.ProjectAdapter;
import de.hiqs.daybird.api.modules.project.dataproviders.models.ProjectRequestUpdateDto;
import de.hiqs.daybird.api.modules.project.domains.models.Project;
import de.hiqs.daybird.api.modules.project.domains.models.ProjectUpdate;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import io.smallrye.mutiny.Uni;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ProjectService {

    private final ProjectAdapter projectAdapter;
    private final ModelMapper modelMapper;

    public ProjectService(ProjectAdapter projectAdapter, ModelMapper modelMapper) {
        this.projectAdapter = projectAdapter;
        this.modelMapper = modelMapper;
    }

    public Uni<Project> addProject(Project project) {
        return projectAdapter.addProject(project);
    }

    public Uni<Project> getProject(String uuid) {
        return projectAdapter.getProject(uuid);
    }

    public Uni<Project> getByUuid(String uuid) {
        return projectAdapter.getByUuid(uuid);
    }

    public Uni<List<Project>> list(String name, String sign, String startDate, String endDate,
                                   String searchDate, String customerUuid, boolean archived) {
        return projectAdapter.list(name, sign, startDate, endDate, searchDate, customerUuid, archived);
    }

    public Uni<Void> deleteProject(String uuid) {
        return projectAdapter.deleteProject(uuid);
    }

    public Uni<Project> update(String uuid, ProjectUpdate projectUpdate) {
        return projectAdapter.update(uuid, modelMapper.map(projectUpdate, ProjectRequestUpdateDto.class));
    }

    public Uni<Project> updateArchived(String uuid, boolean archived, EntityTypeEnum entityTypeEnum, boolean recursive) {
        return projectAdapter.updateArchived(uuid, archived, entityTypeEnum, recursive);
    }
}
