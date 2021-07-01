package com.silenteight.hsbc.bridge.amqp;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.amqp")
@Value
class AmqpProperties {

  Outgoing outgoing;

  @Value
  static class Outgoing {

    String warehouseExchangeName;
    String warehouseRoutingKey;
    String isPepExchangeName;
    String isPepRoutingKey;
    String watchlistPersistedExchangeName;
    String watchlistPersistedRoutingKey;
    String modelPersistedExchangeName;
    String modelPersistedRoutingKey;
  }
}
