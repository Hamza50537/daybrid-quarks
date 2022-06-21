package de.hiqs.daybird.api.modules.project.dataproviders;

import de.hiqs.daybird.api.modules.project.dataproviders.models.ProjectRequestPostDto;
import de.hiqs.daybird.api.modules.project.dataproviders.models.ProjectRequestUpdateDto;
import de.hiqs.daybird.api.modules.project.domains.models.Project;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ProjectAdapter {

    private final ProjectRestClient projectRestClient;
    private final ModelMapper modelMapper;

    public ProjectAdapter(@RestClient ProjectRestClient projectRestClient, ModelMapper modelMapper) {
        this.projectRestClient = projectRestClient;
        this.modelMapper = modelMapper;
    }

    public Uni<Project> addProject(Project project) {
        return projectRestClient.addProject(modelMapper.map(project, ProjectRequestPostDto.class))
                .map(projectRequestDto -> modelMapper.map(projectRequestDto, Project.class));
    }

    public Uni<Project> getProject(String uuid) {
        return projectRestClient.getProject(uuid)
                .map(projectRequestDto -> modelMapper.map(projectRequestDto, Project.class));
    }

    public Uni<Project> getByUuid(String uuid) {
        return projectRestClient.getByUuid(uuid)
                .map(projectRequestDto -> modelMapper.map(projectRequestDto, Project.class));
    }

    public Uni<List<Project>> list(String name, String sign, String startDate, String endDate,
                                  String searchDate, String customerUuid, boolean archived) {
        return projectRestClient.list(name, sign, startDate, endDate, searchDate, customerUuid, archived)
                .map(projectRequestDtos -> modelMapper.map(projectRequestDtos, new TypeToken<List<Project>>() {
                }.getType()));
    }

    public Uni<Void> deleteProject(String uuid) {
        return projectRestClient.deleteProject(uuid);
    }

    public Uni<Project> update(String uuid, ProjectRequestUpdateDto projectRequestUpdateDto) {
        return projectRestClient.update(uuid, projectRequestUpdateDto)
                .map(projectRequestDto -> modelMapper.map(projectRequestDto, Project.class));
    }

    public Uni<Project> updateArchived(String uuid, boolean archived, EntityTypeEnum entityTypeEnum, boolean recursive) {
        return projectRestClient.updateArchived(uuid, archived, entityTypeEnum, recursive)
                .map(projectRequestDto -> modelMapper.map(projectRequestDto, Project.class));
    }
}
