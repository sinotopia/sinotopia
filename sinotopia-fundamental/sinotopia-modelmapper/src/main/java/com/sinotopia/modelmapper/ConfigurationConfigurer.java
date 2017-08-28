package com.sinotopia.modelmapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;


public abstract class ConfigurationConfigurer {

    void configureImpl(ModelMapper mapper) {
        configure(mapper.getConfiguration());
    }

    public abstract void configure(Configuration configuration);
}
