package com.silenteight.payments.bridge.ae.alertregistration.service;

import com.silenteight.payments.bridge.ae.alertregistration.port.AnalysisDataAccessPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCurrentAnalysisUseCaseTest {

  private GetCurrentAnalysisUseCase getCurrentAnalysisUseCase;
  @Mock
  private AnalysisDataAccessPort analysisDataAccessPort;
  @Mock
  private CreateAnalysisService createAnalysisService;

  @BeforeEach
  void setUp() {
    getCurrentAnalysisUseCase =
        new GetCurrentAnalysisUseCase(analysisDataAccessPort, createAnalysisService);
  }

  @Test
  void shouldReturnCurrentAnalysis() {
    when(analysisDataAccessPort.findCurrentAnalysis()).thenReturn(Optional.of(420L));
    assertThat(getCurrentAnalysisUseCase.getOrCreateAnalysis()).isEqualTo(420L);
  }

  @Test
  void shouldReturnNewAnalysis() {
    when(analysisDataAccessPort.findCurrentAnalysis()).thenReturn(Optional.empty());
    when(createAnalysisService.createAnalysis()).thenReturn(2L);
    assertThat(getCurrentAnalysisUseCase.getOrCreateAnalysis()).isEqualTo(2L);
  }
}
