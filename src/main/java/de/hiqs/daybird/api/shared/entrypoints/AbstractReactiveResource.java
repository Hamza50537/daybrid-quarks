package de.hiqs.daybird.api.shared.entrypoints;

import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import de.hiqs.daybird.api.shared.entrypoints.models.AbstractDto;
import io.smallrye.mutiny.Uni;

public abstract class AbstractReactiveResource<T extends AbstractDto> {

    protected abstract Uni<T> updateArchived(String uuid, boolean archived, EntityTypeEnum entityTypeEnum, boolean recursive);

    protected abstract Uni<T>getByUuid(String uuid);
}
