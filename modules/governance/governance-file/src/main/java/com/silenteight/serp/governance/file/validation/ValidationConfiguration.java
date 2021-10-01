package com.silenteight.serp.governance.file.validation;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(ValidationProperties.class)
class ValidationConfiguration {

  @Bean
  ValidationService validationService(@Valid ValidationProperties properties) {
    return new ValidationService(
        nameCharactersValidator(properties),
        mimeTypeValidator(properties),
        nameLengthValidator(properties),
        sizeValidator(properties));
  }

  FileValidator nameCharactersValidator(@Valid ValidationProperties properties) {
    return new FileNameCharactersValidator(properties.getAllowedCharactersForFileName());
  }

  FileValidator mimeTypeValidator(@Valid ValidationProperties properties) {
    return new FileMimeTypeValidator(properties.getAllowedTypes());
  }

  FileValidator nameLengthValidator(@Valid ValidationProperties properties) {
    return new FileNameLengthValidator(properties.getMaxFileNameLength());
  }

  FileValidator sizeValidator(@Valid ValidationProperties properties) {
    return new FileSizeValidator(properties.getMaxFileSizeInBytes());
  }
}
