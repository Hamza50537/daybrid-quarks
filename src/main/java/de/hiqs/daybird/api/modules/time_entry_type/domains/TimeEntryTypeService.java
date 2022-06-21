package de.hiqs.daybird.api.modules.time_entry_type.domains;

import de.hiqs.daybird.api.modules.time_entry_type.dataproviders.TimeEntryTypeAdapter;
import de.hiqs.daybird.api.modules.time_entry_type.domains.models.TimeEntryType;
import de.hiqs.daybird.api.modules.time_entry_type.domains.models.TimeEntryTypeUpdate;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class TimeEntryTypeService {

    private final TimeEntryTypeAdapter timeEntryTypeAdapter;

    public TimeEntryTypeService(TimeEntryTypeAdapter timeEntryTypeAdapter) {
        this.timeEntryTypeAdapter = timeEntryTypeAdapter;
    }

    public Uni<TimeEntryType> addTimeEntryType(TimeEntryType timeEntryType) {
        return timeEntryTypeAdapter.addTimeEntryType(timeEntryType);
    }

    public Uni<TimeEntryType> getTimeEntryType(String uuid) {
        return timeEntryTypeAdapter.getTimeEntryType(uuid);
    }

    public Uni<TimeEntryType> getByUuid(String uuid) {
        return timeEntryTypeAdapter.getByUuid(uuid);
    }

    public Uni<List<TimeEntryType>> list(String typeName, String name, String startTime, String endTime, String date,
                                        String description, String workPackageUuid, String employeeUuid, boolean archived) {
        return timeEntryTypeAdapter.list(typeName, name, startTime, endTime, date, description, workPackageUuid, employeeUuid, archived);
    }

    public Uni<Void> deleteTimeEntryType(String uuid) {
        return timeEntryTypeAdapter.deleteTimeEntryType(uuid);
    }

    public Uni<TimeEntryType> update(String uuid, TimeEntryTypeUpdate timeEntryTypeUpdate) {
        return timeEntryTypeAdapter.update(uuid, timeEntryTypeUpdate);
    }

    public Uni<TimeEntryType> updateArchived(String uuid, boolean archived, EntityTypeEnum entityTypeEnum, boolean recursive) {
        return timeEntryTypeAdapter.updateArchived(uuid, archived, entityTypeEnum, recursive);
    }
}
