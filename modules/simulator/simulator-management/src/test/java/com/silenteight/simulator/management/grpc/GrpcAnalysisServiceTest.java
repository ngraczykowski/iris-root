package com.silenteight.simulator.management.grpc;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;
import com.silenteight.adjudication.api.v1.GetAnalysisRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.simulator.management.SimulationFixtures.ANALYSIS_NAME_1;
import static com.silenteight.simulator.management.SimulationFixtures.ANALYSIS_STATE;
import static com.silenteight.simulator.management.SimulationFixtures.createAnalysis;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrpcAnalysisServiceTest {

  @InjectMocks
  private GrpcAnalysisService underTest;

  @Mock
  private AnalysisServiceBlockingStub analysisStub;

  @Test
  void shouldGetAnalysis() {
    // given
    when(analysisStub.getAnalysis(makeGetAnalysisRequest(ANALYSIS_NAME_1)))
        .thenReturn(createAnalysis(ANALYSIS_NAME_1, 0));

    // when
    Analysis analysis = underTest.getAnalysis(ANALYSIS_NAME_1);

    // then
    assertThat(analysis.getName()).isEqualTo(ANALYSIS_NAME_1);
    assertThat(analysis.getState()).isEqualTo(ANALYSIS_STATE);
  }

  private static GetAnalysisRequest makeGetAnalysisRequest(String analysis) {
    return GetAnalysisRequest.newBuilder()
        .setAnalysis(analysis)
        .build();
  }
}
