package com.silenteight.connector.ftcc.callback.response;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
@Value
@ConstructorBinding
@ConfigurationProperties(prefix = "ftcc.cmapi.callback")
public class RecommendationSenderProperties {

  @NotBlank
  String endpoint;
  @NotBlank
  String login;
  @NotBlank
  String password;
  @NotNull
  Duration readTimeout;
  @NotNull
  Duration connectionTimeout;
  String keystorePath;
  String keystorePassword;
  @NotBlank
  String defaultRecommendationComment;
}
