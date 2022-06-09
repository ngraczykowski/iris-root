/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CategoryResolveProcessTest {

  private CategoryResolveProcess categoryResolveProcess;
  private InMemoryAlertSolvingRepositoryMock inMemoryAlertSolvingRepositoryMock;
  private ReadyMatchFeatureVectorPortMock readyMatchFeatureVectorPortMock;

  @BeforeEach
  void setUp() {
    CategoryValuesClientMock categoryValuesClientMock = new CategoryValuesClientMock();
    readyMatchFeatureVectorPortMock = new ReadyMatchFeatureVectorPortMock();

    var dataAccess = new InMemoryMatchFeatureDataAccess();
    inMemoryAlertSolvingRepositoryMock = new InMemoryAlertSolvingRepositoryMock(dataAccess);

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
