package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.stereotype.Component;


@Component
public class UserObjectMapper extends ObjectMapper {

    public UserObjectMapper() {
        SimpleFilterProvider filterProvider = new SimpleFilterProvider().addFilter("ignoreIdFilter",
                SimpleBeanPropertyFilter.serializeAllExcept("id", "externalId"));
        setFilterProvider(filterProvider);
    }
}
