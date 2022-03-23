package com.silenteight.scb.ingest.infrastructure.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "amqp.ingest.incoming.batch-processing")
public record AmqpIngestIncomingBatchProcessingProperties(String queueName,
                                                          String exchangeName) {}
