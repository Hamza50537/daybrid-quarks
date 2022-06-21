package de.hiqs.daybird.api.shared.application.config;

import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class GlobalBeanConfiguration {

    @Produces
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
