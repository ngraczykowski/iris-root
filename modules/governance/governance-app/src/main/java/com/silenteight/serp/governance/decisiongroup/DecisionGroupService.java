package com.silenteight.serp.governance.decisiongroup;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import liquibase.util.StringUtils;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import javax.transaction.Transactional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class DecisionGroupService {

  private final DecisionGroupRepository groups;
  private final DecisionGroupCache decisionGroupCache;
  private final ApplicationEventPublisher eventPublisher;

  /**
   * @return Newly created decision group id (only if it was created) or empty if group already
   *     exists
   */
  @Transactional
  public Optional<Long> store(@NonNull String decisionGroupName) {
    throwIfEmpty(decisionGroupName);

    if (!decisionGroupCache.exists(decisionGroupName)) {
      DecisionGroup group = groups.save(new DecisionGroup(decisionGroupName));
      decisionGroupCache.add(decisionGroupName);
      eventPublisher.publishEvent(new DecisionGroupCreated(group.getId())); // see ADR-0001
      return ofNullable(group.getId());
    }

    return empty();
  }

  private static void throwIfEmpty(String decisionGroupName) {
    if (StringUtils.isEmpty(decisionGroupName)) {
      throw new DecisionGroupException("Decision group name is empty");
    }
  }

  private static class DecisionGroupException extends IllegalArgumentException {

    private static final long serialVersionUID = -7321976659375925522L;

    DecisionGroupException(String message) {
      super(message);
    }
  }
}
