package de.hiqs.daybird.api.modules.time_entry.dataproviders;

import de.hiqs.daybird.api.modules.time_entry.dataproviders.models.TimeEntryRequestPostDto;
import de.hiqs.daybird.api.modules.time_entry.dataproviders.models.TimeEntryRequestUpdateDto;
import de.hiqs.daybird.api.modules.time_entry.domains.models.TimeEntry;
import de.hiqs.daybird.api.modules.time_entry.domains.models.TimeEntryUpdate;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class TimeEntryAdapter {

    private final TimeEntryRestClient timeEntryRestClient;
    private final ModelMapper modelMapper;

    public TimeEntryAdapter(@RestClient TimeEntryRestClient timeEntryRestClient, ModelMapper modelMapper) {
        this.timeEntryRestClient = timeEntryRestClient;
        this.modelMapper = modelMapper;
    }

    public Uni<TimeEntry> addTimeEntry(TimeEntry timeEntry) {
        return timeEntryRestClient.addTimeEntry(modelMapper.map(timeEntry, TimeEntryRequestPostDto.class))
                .map(timeEntryRequestDto -> modelMapper.map(timeEntryRequestDto, TimeEntry.class));
    }

    public Uni<TimeEntry> getTimeEntry(String uuid) {
        return timeEntryRestClient.getTimeEntry(uuid)
                .map(timeEntryRequestDto -> modelMapper.map(timeEntryRequestDto, TimeEntry.class));
    }

    public Uni<TimeEntry> getByUuid(String uuid) {
        return timeEntryRestClient.getByUuid(uuid)
                .map(timeEntryRequestDto -> modelMapper.map(timeEntryRequestDto, TimeEntry.class));
    }

    public Uni<List<TimeEntry>> list(String name, String dateFrom, String dateTo, String date, String workPackageUuid,
                                    String timeEntryTypeUuid, String employeeUuid, boolean archived) {
        return timeEntryRestClient.list(name, dateFrom, dateTo, date, workPackageUuid, timeEntryTypeUuid, employeeUuid, archived)
                .map(timeEntryRequestDtos -> modelMapper.map(timeEntryRequestDtos, new TypeToken<List<TimeEntry>>() {
                }.getType()));
    }

    public Uni<Void> deleteTimeEntry(String uuid) {
        return timeEntryRestClient.deleteTimeEntry(uuid);
    }

    public Uni<TimeEntry> update(String uuid, TimeEntryUpdate timeEntryUpdate) {
        return timeEntryRestClient.update(uuid, modelMapper.map(timeEntryUpdate, TimeEntryRequestUpdateDto.class))
                .map(timeEntryRequestDto -> modelMapper.map(timeEntryRequestDto, TimeEntry.class));
    }

    public Uni<TimeEntry> updateArchived(String uuid, boolean archived, EntityTypeEnum entityTypeEnum, boolean recursive) {
        return timeEntryRestClient.updateArchived(uuid, archived, entityTypeEnum, recursive)
                .map(timeEntryRequestDto -> modelMapper.map(timeEntryRequestDto, TimeEntry.class));
    }
}
