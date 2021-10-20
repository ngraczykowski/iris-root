package com.silenteight.payments.bridge.ae.alertregistration.service;

import com.silenteight.payments.bridge.ae.alertregistration.port.AnalysisDataAccessPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.BuildCreateAnalysisRequestPort;

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
  @Mock
  private BuildCreateAnalysisRequestPort buildCreateAnalysisRequestPort;

  @BeforeEach
  void setUp() {
    getCurrentAnalysisUseCase = new GetCurrentAnalysisUseCase(
        analysisDataAccessPort, createAnalysisService, buildCreateAnalysisRequestPort);
  }

  @Test
  void shouldReturnCurrentAnalysis() {
    when(analysisDataAccessPort.findCurrentAnalysis()).thenReturn(Optional.of("analysis/420"));
    assertThat(getCurrentAnalysisUseCase.getOrCreateAnalysis()).isEqualTo("analysis/420");
  }

  @Test
  void shouldReturnNewAnalysis() {
    when(analysisDataAccessPort.findCurrentAnalysis()).thenReturn(Optional.empty());
    when(createAnalysisService.createAnalysis(any())).thenReturn("analysis/2");
    assertThat(getCurrentAnalysisUseCase.getOrCreateAnalysis()).isEqualTo("analysis/2");
  }
}
