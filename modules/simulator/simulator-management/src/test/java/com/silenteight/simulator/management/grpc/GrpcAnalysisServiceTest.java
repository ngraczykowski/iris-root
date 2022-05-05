package com.silenteight.simulator.management.grpc;

import com.silenteight.adjudication.api.v1.AddDatasetRequest;
import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;
import com.silenteight.adjudication.api.v1.CreateAnalysisRequest;
import com.silenteight.adjudication.api.v1.GetAnalysisRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.simulator.management.SimulationFixtures.*;
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

  @Test
  void shouldCreateRequestToCreateAnalysis() {
    //given + when
    underTest.createAnalysis(SOLVING_MODEL);

    //then
    ArgumentCaptor<CreateAnalysisRequest> createAnalysisRequestArgumentCaptor
        = ArgumentCaptor.forClass(CreateAnalysisRequest.class);

    verify(analysisStub, timeout(1)).createAnalysis(createAnalysisRequestArgumentCaptor.capture());

    CreateAnalysisRequest createAnalysisRequest = createAnalysisRequestArgumentCaptor.getValue();
    assertThat(createAnalysisRequest.getAnalysis().getPolicy()).isEqualTo(POLICY_NAME);
    assertThat(createAnalysisRequest.getAnalysis().getStrategy()).isEqualTo(STRATEGY_NAME);
    assertThat(createAnalysisRequest.getAnalysis().getCategoriesCount()).isEqualTo(2);
    assertThat(createAnalysisRequest.getAnalysis().getFeaturesList()).isEmpty();
  }

  @Test
  void shouldCreateRequestToAddDataset() {
    //given + when
    underTest.addDatasetToAnalysis(ANALYSIS_NAME_1, DATASET_NAME_1);

    //then
    ArgumentCaptor<AddDatasetRequest> addDatasetRequestArgumentCaptor
        = ArgumentCaptor.forClass(AddDatasetRequest.class);

    verify(analysisStub, timeout(1)).addDataset(addDatasetRequestArgumentCaptor.capture());

    AddDatasetRequest createAnalysisRequest = addDatasetRequestArgumentCaptor.getValue();
    assertThat(createAnalysisRequest.getAnalysis()).isEqualTo(ANALYSIS_NAME_1);
    assertThat(createAnalysisRequest.getDataset()).isEqualTo(DATASET_NAME_1);
  }

  private static GetAnalysisRequest makeGetAnalysisRequest(String analysis) {
    return GetAnalysisRequest.newBuilder()
        .setAnalysis(analysis)
        .build();
  }
}
