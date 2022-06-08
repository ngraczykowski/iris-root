/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.solving.domain.AlertSolving;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CategoryResolveProcessTest {

  private CategoryResolveProcess categoryResolveProcess;
  private InMemoryAlertSolvingRepositoryMock inMemoryAlertSolvingRepositoryMock;
  private ReadyMatchFeatureVectorPortMock readyMatchFeatureVectorPortMock;

  @BeforeEach
  void setUp() {
    CategoryValuesClientMock categoryValuesClientMock = new CategoryValuesClientMock();
    inMemoryAlertSolvingRepositoryMock = new InMemoryAlertSolvingRepositoryMock();
    readyMatchFeatureVectorPortMock = new ReadyMatchFeatureVectorPortMock();

    var dataAccess = new InMemoryMatchFeatureDataAccess();
    var alerts = dataAccess.findAnalysisFeatures(Set.of(1L), Set.of(1L, 3L, 4L));
    alerts.values().stream()
        .map(AlertSolving::new)
        .forEach(a -> inMemoryAlertSolvingRepositoryMock.save(a));

    categoryResolveProcess =
        new CategoryResolveProcess(
            categoryValuesClientMock,
            inMemoryAlertSolvingRepositoryMock,
            readyMatchFeatureVectorPortMock);
  }

  @Test
  void shouldSendReadyFeatureVectorWhenMatchesWithValues() {
    categoryResolveProcess.resolveCategoryValues(1L);

    assertThat(readyMatchFeatureVectorPortMock.getRequestsCount()).isEqualTo(2);
  }

  @Test
  void shouldSendReadyFeatureVectorWhenMatchesWithoutValues() {
    categoryResolveProcess.resolveCategoryValues(3L);

    assertThat(readyMatchFeatureVectorPortMock.getRequestsCount()).isEqualTo(2);
  }

  @Test
  void shouldNotSendWhenFeatureValuesNotPresent() {
    categoryResolveProcess.resolveCategoryValues(4L);

    assertThat(readyMatchFeatureVectorPortMock.getRequestsCount()).isEqualTo(0);
  }
}
