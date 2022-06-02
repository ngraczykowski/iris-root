package com.silenteight.adjudication.engine.solving.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "ae.solving.event-store")
class EventStoreConfigurationProperties {
  private int poolSize = 6;
  private JournalProperties journal;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  static class JournalProperties {

    private String exchange = "ae.journal";
    private String routingKey = "";
  }
}
