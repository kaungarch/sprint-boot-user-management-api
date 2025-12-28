package com.art.usermanagement.config;

import com.art.usermanagement.dto.response.BlackListDto;
import com.art.usermanagement.model.Blacklist;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilConfig {

    @Bean
    public ModelMapper modelMapper()
    {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(Blacklist.class, BlackListDto.class).addMappings(
                mapper -> {
                    mapper.map(Blacklist::getId, BlackListDto::setId);
                    mapper.map(Blacklist::getPhoneNumber, BlackListDto::setPhoneNumber);
                    mapper.map(Blacklist::getNrcNumber, BlackListDto::setNrcNumber);
                    mapper.map(Blacklist::getAccount, BlackListDto::setBlackListedBy);
                }
        );
        return modelMapper;
    }

}
