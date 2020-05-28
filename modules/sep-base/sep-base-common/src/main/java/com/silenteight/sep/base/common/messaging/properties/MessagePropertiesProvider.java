package com.silenteight.sep.base.common.messaging.properties;

import org.springframework.amqp.core.MessageProperties;

import java.util.function.Supplier;

public interface MessagePropertiesProvider extends Supplier<MessageProperties> {
}
