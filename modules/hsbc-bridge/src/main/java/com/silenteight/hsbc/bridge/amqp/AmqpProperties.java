package com.silenteight.hsbc.bridge.amqp;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.amqp")
@Value
class AmqpProperties {

  String warehouseExchangeName;
  String warehouseRoutingKey;
}
