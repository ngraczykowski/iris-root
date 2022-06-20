/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.infrastructure.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "amqp.ingest.incoming.data-retention")
public record IngestDataRetentionProperties(
    String exchangeName,

    String queueName,
    String routingKey,
    String deadLetterQueueName,
    String deadLetterExchangeName,

    Integer deadLetterQueueTimeToLiveInMilliseconds,
    Integer numberOfRetriesDeadMessages
) {}
