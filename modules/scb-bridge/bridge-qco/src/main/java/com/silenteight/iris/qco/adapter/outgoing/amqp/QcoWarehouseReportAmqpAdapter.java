/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.adapter.outgoing.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.qco.domain.model.MatchSolution;
import com.silenteight.iris.qco.domain.model.QcoRecommendationMatch;
import com.silenteight.iris.qco.domain.port.outgoing.QcoWarehouseReportAdapter;
import com.silenteight.iris.qco.infrastructure.amqp.ReportAmqpConfigurationProperties;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class QcoWarehouseReportAmqpAdapter implements QcoWarehouseReportAdapter {

  private final RabbitTemplate rabbitTemplate;
  private final ReportAmqpConfigurationProperties properties;

  @Override
  public void send(QcoRecommendationMatch recommendationMatch, MatchSolution matchSolution) {
    var request = QcoReportRequestMapper.toRequest(recommendationMatch, matchSolution);
    rabbitTemplate.convertAndSend(properties.exchangeName(), properties.routingKey(), request);
    log.info("The overridden match recommendation (matchName={}, targetSolution={}) was sent",
        recommendationMatch.matchName(), matchSolution.solution());
  }
}
