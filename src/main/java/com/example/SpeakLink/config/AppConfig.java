package com.example.SpeakLink.config;

import com.example.SpeakLink.dto.UserDto;
import com.example.SpeakLink.entity.User;
import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class AppConfig {
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper.getConfiguration()
				.setPropertyCondition(mappingContext -> {
					if (mappingContext.getSource() instanceof PersistentCollection<?> value && !value.isInitializing())
						return false;
					return true;
				})
				.setSkipNullEnabled(true);
		modelMapper.typeMap(User.class, UserDto.class).addMappings(mapper -> mapper.map(user -> null, UserDto::setPassword));
		return modelMapper;
	}
}
