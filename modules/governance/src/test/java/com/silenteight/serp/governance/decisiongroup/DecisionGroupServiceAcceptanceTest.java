package com.silenteight.serp.governance.decisiongroup;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DecisionGroupServiceAcceptanceTest {

  private static final String GROUP_NAME = "group";

  private DecisionGroupService service;
  private DecisionGroupFinder finder;

  private DecisionGroupRepository repository;

  private DecisionGroupCache decisionGroupCache;

  @Mock
  private ApplicationEventPublisher eventPublisher;

  @BeforeEach
  void setUp() {
    repository = new InMemoryDecisionGroupRepository();
    DecisionGroupModuleConfiguration config =
        new DecisionGroupModuleConfiguration(repository, eventPublisher);
    decisionGroupCache = new DecisionGroupCache(repository);

    finder = config.decisionGroupFinder();
    service = config.decisionGroupService(decisionGroupCache);
  }

  @Test
  void whenStoredOneGroup_canFindOne() {
    service.store(GROUP_NAME);

    Collection<Long> actual = finder.findAllDecisionGroupIds();

    assertThat(actual).hasSize(1);
  }

  @Test
  void whenStoringTwoSameDecisionGroups_onlyOneIsStored() {
    service.store(GROUP_NAME);
    service.store(GROUP_NAME);

    assertThat(finder.findAllDecisionGroupIds()).hasSize(1);
  }

  @Test
  void givenEmptyName_throwsException() {
    ThrowingCallable when = () -> service.store("");

    assertThatExceptionOfType(RuntimeException.class).isThrownBy(when);
  }

  @Test
  void whenStoring_persistsCorrectly() {
    service.store(GROUP_NAME);

    Collection<DecisionGroup> actual = repository.findAll();

    assertThat(actual)
        .extracting(DecisionGroup::getName)
        .containsOnlyOnce(GROUP_NAME);
  }

  @Test
  void afterStoring_emitEventTheDecisionGroupWasCreated() {
    // when
    Long groupId = service.store(GROUP_NAME).orElseGet(null);

    // then
    verify(eventPublisher).publishEvent(new DecisionGroupCreated(groupId));
  }

  @Test
  void whenStoringDecisionGroup_itIsAddedToTheCache() {
    // when
    service.store(GROUP_NAME);

    // then
    assertThat(decisionGroupCache.exists(GROUP_NAME)).isTrue();
  }

  @Test
  void whenStoringGroupWhichIsNotInCache_itIsAddedToTheRepository() {
    // when
    service.store(GROUP_NAME);

    // then
    assertThat(repository.existsByName(GROUP_NAME)).isTrue();
  }

  @Test
  void whenStoringGroupWhichIsInCache_theRepositoryIsNotTouched() {
    // when
    decisionGroupCache.add(GROUP_NAME);

    // and
    service.store(GROUP_NAME);

    // then
    assertThat(repository.existsByName(GROUP_NAME)).isFalse();
  }

  @Test
  void whenApplicationStarts_decisionGroupNamesAreLoadedIntoCache() {
    // given
    repository.save(new DecisionGroup(GROUP_NAME));

    // when
    decisionGroupCache.applicationStarted();

    // then
    decisionGroupCache.exists(GROUP_NAME);
  }

  @Test
  void shouldReturnIdOnlyWhenStoreNewGroup() {
    assertThat(service.store(GROUP_NAME)).isPresent();
    assertThat(service.store(GROUP_NAME)).isEmpty();
  }
}
