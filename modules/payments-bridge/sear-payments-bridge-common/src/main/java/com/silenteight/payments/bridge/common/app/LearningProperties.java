package com.silenteight.payments.bridge.common.app;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.ZoneId;
import javax.annotation.PostConstruct;
import javax.validation.constraints.NotEmpty;

@Data
@Slf4j
@Validated
@ConfigurationProperties(prefix = "pb.svb-learning")
public class LearningProperties {

  @NotEmpty
  private String timeZone = "America/Chicago";
  private String fileEncoding = "CP1250";
  private String discriminator = "";
  private int chunkSize = 128;

  @PostConstruct
  void initialize() {
    if (!ZoneId.getAvailableZoneIds().contains(timeZone)) {
      throw new BeanCreationException("Invalid time zone name " + timeZone);
    }
    log.info("Using timezone for {}", timeZone);
  }
}
