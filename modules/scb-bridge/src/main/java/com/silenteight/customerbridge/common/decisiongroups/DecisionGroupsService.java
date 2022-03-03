package com.silenteight.customerbridge.common.decisiongroups;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.integration.DecisionGroups;
import com.silenteight.proto.serp.v1.integration.DecisionGroups.Builder;
import com.silenteight.sep.base.common.messaging.MessageSender;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.silenteight.customerbridge.common.messaging.MessagingConstants.ROUTE_ALERT_DECISION_GROUPS;
import static java.util.Arrays.asList;

@Slf4j
@RequiredArgsConstructor
public class DecisionGroupsService {

  @NonNull
  private final DecisionGroupsReader reader;

  @NonNull
  private final MessageSender messageSender;

  private final Set<String> decisionGroups = new ConcurrentHashMap<String, Integer>().keySet(0);

  @EventListener(ApplicationStartedEvent.class)
  public void applicationStarted() {
    decisionGroups.clear();
    decisionGroups.addAll(reader.readAll());
    publishDecisionGroups();
  }

  private void publishDecisionGroups() {
    Builder builder = DecisionGroups.newBuilder();
    String[] groups = decisionGroups.toArray(String[]::new);
    builder.addAllDecisionGroup(asList(groups));

    messageSender.send(ROUTE_ALERT_DECISION_GROUPS, builder.build());
  }
}
