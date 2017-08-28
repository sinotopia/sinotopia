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
package com.sinotopia.modelmapper.mapping;

import com.sinotopia.modelmapper.TypeMapConfigurer;
import com.sinotopia.modelmapper.dtos.UserDto;
import com.sinotopia.modelmapper.entities.User;
import org.modelmapper.TypeMap;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class UserDtoMapping extends TypeMapConfigurer<User, UserDto> {
    @Override
    public void configure(TypeMap<User, UserDto> typeMap) {
        typeMap.addMapping(User::getAge, UserDto::setAge);
        typeMap.addMapping(User::getName, UserDto::setFirstName);
        typeMap.addMapping(User::getName, UserDto::setLastName);
        typeMap.addMapping(User::getName, UserDto::setMiddleName);
    }
}

