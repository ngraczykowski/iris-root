package com.silenteight.warehouse.statistics.properties;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.statistics.daily")
public class DailyStatisticsProperties {

  Number recalculationPeriod;
}
