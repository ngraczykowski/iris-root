/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.domain;

import org.aspectj.lang.Aspects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.aspectj.AnnotationTransactionAspect;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GnsSyncServiceTest {

  @Mock
  private GnsSyncRepository gnsSyncRepository;

  @Mock
  private GnsSync gnsSyncMock;

  private GnsSyncService gnsSyncService;

  private String someGnsSyncMode = "SOLVING_AL";

  @BeforeEach
  void setUp() {
    Aspects.aspectOf(AnnotationTransactionAspect.class).destroy();
    gnsSyncService = new GnsSyncService(gnsSyncRepository);
  }

  @Test
  void shouldDoNothingWhenBatchTypeHasNotBeenFound() {
    //given
    when(gnsSyncRepository.findAllBySyncModeAndFinishedAtIsNull(someGnsSyncMode)).thenReturn(
        emptyList());

    //when
    gnsSyncService.abandonSync(someGnsSyncMode);

    //then
    verify(gnsSyncRepository, times(0)).save(any(GnsSync.class));
  }

  @Test
  void shouldUpdateStateAndFinishedAt() {
    //given
    when(gnsSyncRepository.findAllBySyncModeAndFinishedAtIsNull(someGnsSyncMode)).thenReturn(
        singletonList(gnsSyncMock));

    //when
    gnsSyncService.abandonSync(someGnsSyncMode);

    //then
    verify(gnsSyncMock, times(1)).finishSyncWithError("ABANDONED");
  }

  @Test
  void shouldReturnFalseWhenNoActiveSyncs() {
    // given
    when(gnsSyncRepository.findAllBySyncModeAndFinishedAtIsNull(someGnsSyncMode)).thenReturn(
        emptyList());

    // when, then
    assertThat(gnsSyncService.isRunningSync(someGnsSyncMode)).isFalse();
  }
}
