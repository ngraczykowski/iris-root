/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.infrastructure.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "amqp.output-recommendation.incoming."
    + "notify-batch-completed.solving")
public record OutputNotifyBatchCompletedRabbitProperties(
    String exchangeName,
    String queueName,
    String deadLetterQueueName,
    String deadLetterExchangeName,
    Integer deadLetterQueueTimeToLiveInMilliseconds,
    String batchRoutingKey,
    Integer queueMaxPriority
) {}
