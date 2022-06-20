/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gender;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gender.Gender.UNKNOWN;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NameSeparatingGenderDetectorTest {

  private NameSeparatingGenderDetector nameSeparatingGenderDetector;

  @Mock
  private NameGenderDetector genderDetectorMock;

  @Captor
  private ArgumentCaptor<List<String>> genderDetectorArgumentCaptor;

  @BeforeEach
  void setUp() {
    nameSeparatingGenderDetector = new NameSeparatingGenderDetector(genderDetectorMock);
    when(genderDetectorMock.detect(any())).thenReturn(UNKNOWN);
  }

  @ParameterizedTest(name = "Separator {0}")
  @ValueSource(strings = { ",", "\\", "`", "~", "[", "]", " " })
  void twoNamesSeparatedByKnownSymbols_passesThemAsSeparateCollectionEntries(String separator) {
    List<String> input = singletonList("Kamil" + separator + "Piotr");

    nameSeparatingGenderDetector.detect(input);

    verify(genderDetectorMock).detect(genderDetectorArgumentCaptor.capture());
    assertThat(genderDetectorArgumentCaptor.getValue()).containsOnly("Kamil", "Piotr");
  }
}
