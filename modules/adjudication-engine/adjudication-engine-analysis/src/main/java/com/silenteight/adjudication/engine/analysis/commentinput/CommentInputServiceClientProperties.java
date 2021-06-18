package com.silenteight.adjudication.engine.analysis.commentinput;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@ConfigurationProperties("ae.grpc.client.comment-input")
@Data
@Validated
class CommentInputServiceClientProperties {

  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

  private Duration timeout = DEFAULT_TIMEOUT;
}
