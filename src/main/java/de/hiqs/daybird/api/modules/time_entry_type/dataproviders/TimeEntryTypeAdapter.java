package de.hiqs.daybird.api.modules.time_entry_type.dataproviders;

import de.hiqs.daybird.api.modules.time_entry_type.dataproviders.models.TimeEntryTypeRequestPostDto;
import de.hiqs.daybird.api.modules.time_entry_type.dataproviders.models.TimeEntryTypeRequestUpdateDto;
import de.hiqs.daybird.api.modules.time_entry_type.domains.models.TimeEntryType;
import de.hiqs.daybird.api.modules.time_entry_type.domains.models.TimeEntryTypeUpdate;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class TimeEntryTypeAdapter {

    private final TimeEntryTypeRestClient timeEntryTypeRestClient;
    private final ModelMapper modelMapper;

    public TimeEntryTypeAdapter(@RestClient TimeEntryTypeRestClient timeEntryTypeRestClient, ModelMapper modelMapper) {
        this.timeEntryTypeRestClient = timeEntryTypeRestClient;
        this.modelMapper = modelMapper;
    }

    public Uni<TimeEntryType> addTimeEntryType(TimeEntryType timeEntryType) {
        return timeEntryTypeRestClient.addTimeEntryType(modelMapper.map(timeEntryType, TimeEntryTypeRequestPostDto.class))
                .map(timeEntryTypeRequestDto -> modelMapper.map(timeEntryTypeRequestDto, TimeEntryType.class));
    }

    public Uni<TimeEntryType> getTimeEntryType(String uuid) {
        return timeEntryTypeRestClient.getTimeEntryType(uuid)
                .map(timeEntryTypeRequestDto -> modelMapper.map(timeEntryTypeRequestDto, TimeEntryType.class));
    }

    public Uni<TimeEntryType> getByUuid(String uuid) {
        return timeEntryTypeRestClient.getByUuid(uuid)
                .map(timeEntryTypeRequestDto -> modelMapper.map(timeEntryTypeRequestDto, TimeEntryType.class));
    }

    public Uni<List<TimeEntryType>> list(String typeName, String name, String startTime, String endTime, String date,
                                        String description, String workPackageUuid, String employeeUuid, boolean archived) {
        return timeEntryTypeRestClient.list(typeName, name, startTime, endTime, date, description, workPackageUuid, employeeUuid, archived)
                .map(timeEntryTypeRequestDtos -> modelMapper.map(timeEntryTypeRequestDtos, new TypeToken<List<TimeEntryType>>() {
                }.getType()));
    }

    public Uni<Void> deleteTimeEntryType(String uuid) {
        return timeEntryTypeRestClient.deleteTimeEntryType(uuid);
    }

    public Uni<TimeEntryType> update(String uuid, TimeEntryTypeUpdate timeEntryTypeUpdate) {
        return timeEntryTypeRestClient.update(uuid, modelMapper.map(timeEntryTypeUpdate, TimeEntryTypeRequestUpdateDto.class))
                .map(timeEntryTypeRequestDto -> modelMapper.map(timeEntryTypeRequestDto, TimeEntryType.class));
    }

    public Uni<TimeEntryType> updateArchived(String uuid, boolean archived, EntityTypeEnum entityTypeEnum, boolean recursive) {
        return timeEntryTypeRestClient.updateArchived(uuid, archived, entityTypeEnum, recursive)
                .map(timeEntryTypeRequestDto -> modelMapper.map(timeEntryTypeRequestDto, TimeEntryType.class));
    }
}
