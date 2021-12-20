package com.silenteight.payments.bridge.svb.newlearning.step.reservation;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Slf4j
@Validated
@ConfigurationProperties(prefix = "pb.svb-learning.reservation")
class AlertReservationProperties {

  private int chunkSize = 1;

}
