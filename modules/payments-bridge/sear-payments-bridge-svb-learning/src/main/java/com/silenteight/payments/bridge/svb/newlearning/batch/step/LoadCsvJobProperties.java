
package com.silenteight.payments.bridge.svb.newlearning.batch.step;

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
public class LoadCsvJobProperties {

  private int chunkSize = 10;
  private String fileEncoding = "CP1250";
  private long retryPeriodMilliseconds = 30 * 1000;
  @NotEmpty
  private String timeZone = "America/Chicago";

  @PostConstruct
  void initialize() {
    if (!ZoneId.getAvailableZoneIds().contains(timeZone)) {
      throw new BeanCreationException("Invalid time zone name " + timeZone);
    }
    log.info("Using timezone for {}", timeZone);
  }
}
