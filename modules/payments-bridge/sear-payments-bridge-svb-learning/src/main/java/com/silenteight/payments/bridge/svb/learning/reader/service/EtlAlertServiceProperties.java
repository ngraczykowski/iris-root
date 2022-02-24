package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.Data;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.ZoneId;
import javax.annotation.PostConstruct;
import javax.validation.constraints.NotEmpty;

@ConfigurationProperties("pb.sear-learning.etl.alert")
@Data
class EtlAlertServiceProperties {

  static final String DEFAULT_TIME_ZONE = "America/Los_Angeles";

  static final int BATCH_SIZE = 50;

  @NotEmpty
  private String timeZone = DEFAULT_TIME_ZONE;

  private int batchSize = BATCH_SIZE;

  @PostConstruct
  void initialize() {
    if (!ZoneId.getAvailableZoneIds().contains(timeZone)) {
      throw new BeanCreationException("Invalid time zone name " + timeZone);
    }
  }
}
