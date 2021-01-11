package com.silenteight.serp.governance.app.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.integration.DecisionGroups;
import com.silenteight.serp.governance.decisiongroup.DecisionGroupService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.StringJoiner;

import static com.silenteight.serp.governance.grpc.MessagingConstants.QUEUE_GOVERNANCE_DECISION_GROUP;

@RequiredArgsConstructor
@Slf4j
class DecisionGroupReceiver {

  private final DecisionGroupService service;

  @RabbitListener(queues = QUEUE_GOVERNANCE_DECISION_GROUP)
  public void process(DecisionGroups decisionGroups) {
    log.info("Received decision groups, size: " + decisionGroups.getDecisionGroupCount());
    StringJoiner decisionGroupNamesJoiner = new StringJoiner(",");
    decisionGroups
        .getDecisionGroupList()
        .stream()
        .peek(decisionGroupNamesJoiner::add)
        .forEach(this::persistDecisionGroup);

    if (log.isDebugEnabled()) {
      log.debug("Received decision groups: {}", decisionGroupNamesJoiner);
    }
  }

  private void persistDecisionGroup(String decisionGroupName) {
    service.store(decisionGroupName);
  }
}
