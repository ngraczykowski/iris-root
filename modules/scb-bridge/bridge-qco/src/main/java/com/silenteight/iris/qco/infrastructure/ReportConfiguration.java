/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.infrastructure;

import com.silenteight.iris.qco.infrastructure.amqp.ReportAmqpConfigurationProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    CommentsPrefixProperties.class,
    ReportAmqpConfigurationProperties.class
})
class ReportConfiguration {
}
