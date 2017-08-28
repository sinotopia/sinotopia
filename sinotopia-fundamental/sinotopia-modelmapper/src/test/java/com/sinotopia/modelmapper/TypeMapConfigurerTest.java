/**
 * Copyright (C) 2017 Idan Rozenfeld the original author or authors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sinotopia.modelmapper;


import com.sinotopia.modelmapper.dtos.UserDto;
import com.sinotopia.modelmapper.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests the {@link TypeMapConfigurer} class.
 *
 * @author Idan Rozenfeld
 */
@RunWith(SpringRunner.class)
public class TypeMapConfigurerTest {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void shouldInstantiateMapper() {
        assertThat(modelMapper, is(notNullValue()));
    }

    @Test
    public void shouldMapUserEntity() {
        final User user = new User("John Doe", 23);
        final UserDto userDto = modelMapper.map(user, UserDto.class);
        assertThat(userDto.getFirstName(), equalTo("John"));
        assertThat(userDto.getLastName(), equalTo("Doe"));
        assertThat(userDto.getAge(), equalTo(user.getAge()));
    }

    @Configuration
    @EnableAutoConfiguration
    public static class Application {

        @Bean
        public TypeMapConfigurer<User, UserDto> userMapping() {
            return new TypeMapConfigurer<User, UserDto>() {

                @Override
                public void configure(TypeMap<User, UserDto> typeMap) {
                    typeMap.addMapping(User::getAge, UserDto::setAge);
                    typeMap.setPreConverter(context -> {
                        String[] name = context.getSource().getName().split(" ");
                        context.getDestination().setFirstName(name[0]);
                        context.getDestination().setLastName(name[1]);
                        return context.getDestination();
                    });
                }
            };
        }
    }
}