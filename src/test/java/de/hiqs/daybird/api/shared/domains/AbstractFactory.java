package de.hiqs.daybird.api.shared.domains;

public abstract class AbstractFactory<T, U> {

    protected abstract T createInstanceDto();

    protected abstract U createUpdateDto();
}
