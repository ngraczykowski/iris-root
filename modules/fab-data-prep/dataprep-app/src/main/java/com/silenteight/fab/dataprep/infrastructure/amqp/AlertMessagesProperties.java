package com.silenteight.fab.dataprep.infrastructure.amqp;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "amqp.dataprep.incoming.alert-stored")
@Value
public class AlertMessagesProperties {

  String queueName;
}
