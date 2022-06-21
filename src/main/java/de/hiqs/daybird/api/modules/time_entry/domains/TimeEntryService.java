package de.hiqs.daybird.api.modules.time_entry.domains;

import de.hiqs.daybird.api.modules.time_entry.dataproviders.TimeEntryAdapter;
import de.hiqs.daybird.api.modules.time_entry.domains.models.TimeEntry;
import de.hiqs.daybird.api.modules.time_entry.domains.models.TimeEntryUpdate;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class TimeEntryService {

    private final TimeEntryAdapter timeEntryAdapter;

    public TimeEntryService(TimeEntryAdapter timeEntryAdapter) {
        this.timeEntryAdapter = timeEntryAdapter;
    }

    public Uni<TimeEntry> addTimeEntry(TimeEntry timeEntry) {
        return timeEntryAdapter.addTimeEntry(timeEntry);
    }

    public Uni<TimeEntry> getTimeEntry(String uuid) {
        return timeEntryAdapter.getTimeEntry(uuid);
    }

    public Uni<TimeEntry> getByUuid(String uuid) {
        return timeEntryAdapter.getByUuid(uuid);
    }

    public Uni<List<TimeEntry>> list(String name, String dateFrom, String dateTo, String date, String workPackageUuid,
                                    String timeEntryTypeUuid, String employeeUuid, boolean archived) {
        return timeEntryAdapter.list(name, dateFrom, dateTo, date, workPackageUuid, timeEntryTypeUuid, employeeUuid, archived);
    }

    public Uni<Void> deleteTimeEntry(String uuid) {
        return timeEntryAdapter.deleteTimeEntry(uuid);
    }

    public Uni<TimeEntry> update(String uuid, TimeEntryUpdate timeEntryUpdate) {
        return timeEntryAdapter.update(uuid, timeEntryUpdate);
    }

    public Uni<TimeEntry> updateArchived(String uuid, boolean archived, EntityTypeEnum entityTypeEnum, boolean recursive) {
        return timeEntryAdapter.updateArchived(uuid, archived, entityTypeEnum, recursive);
    }
}
