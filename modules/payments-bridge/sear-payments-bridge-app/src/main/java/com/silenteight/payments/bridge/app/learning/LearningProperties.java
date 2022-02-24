package com.silenteight.payments.bridge.app.learning;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.sear-learning")
class LearningProperties {

  private Boolean useNewLearning = false;
}
