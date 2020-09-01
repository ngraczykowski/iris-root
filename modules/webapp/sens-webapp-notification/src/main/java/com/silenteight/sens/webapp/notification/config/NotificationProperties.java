package com.silenteight.sens.webapp.notification.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "sens.webapp.notification")
@RequiredArgsConstructor
@ConstructorBinding
@Getter
class NotificationProperties {

  private final boolean enabled;
}
