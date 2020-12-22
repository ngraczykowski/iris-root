package com.silenteight.serp.governance.decisiongroup;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
class DecisionGroupCache {

  private final DecisionGroupRepository groups;

  private final Set<String> decisionGroupNames =
      new ConcurrentHashMap<String, Integer>().keySet(0);

  @EventListener(ApplicationStartedEvent.class)
  public void applicationStarted() {
    groups.findAll()
        .stream()
        .map(DecisionGroup::getName)
        .forEach(decisionGroupNames::add);
  }

  boolean exists(String groupName) {
    return decisionGroupNames.contains(groupName) || groups.existsByName(groupName);
  }

  void add(String groupName) {
    this.decisionGroupNames.add(groupName);
  }

}
