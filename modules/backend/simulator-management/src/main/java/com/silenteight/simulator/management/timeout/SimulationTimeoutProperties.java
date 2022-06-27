package com.silenteight.simulator.management.timeout;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties(prefix = "simulator.simulation.timeout")
class SimulationTimeoutProperties {

  @NotNull
  Duration durationTime;
}
