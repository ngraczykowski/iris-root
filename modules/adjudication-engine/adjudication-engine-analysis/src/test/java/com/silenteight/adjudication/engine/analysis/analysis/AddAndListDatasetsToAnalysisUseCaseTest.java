package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.api.v1.AnalysisDataset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddAndListDatasetsToAnalysisUseCaseTest {

  private static final String[] IGNORED_PROTO_FIELDS = { "memoizedHashCode" };

  @Mock
  private ListAnalysisDatasetUseCase listAnalysisDatasetUseCase;

  @Mock
  private ApplicationEventPublisher applicationEventPublisher;

  private final InMemoryAnalysisDatasetRepository analysisDatasetRepository
      = new InMemoryAnalysisDatasetRepository();

  private AddAndListDatasetsInAnalysisUseCase useCase;

  @BeforeEach
  void setUp() {
    var addUseCase = new AddDatasetsToAnalysisUseCase(
        analysisDatasetRepository, applicationEventPublisher);
    useCase = new AddAndListDatasetsInAnalysisUseCase(addUseCase, listAnalysisDatasetUseCase);
  }

  @Test
  void addDataset() {
    when(listAnalysisDatasetUseCase.listAnalysisDatasets(
        Collections.singletonList(
            AnalysisDatasetKey.builder().analysisId(1L).datasetId(1L).build())))
        .thenReturn(Collections.singletonList(AnalysisDataset
            .newBuilder()
            .setName("analysis/1/datasets/1")
            .setAlertCount(10)
            .setPendingAlerts(0)
            .build()));

    List<String> datasets = Collections.singletonList("datasets/1");
    List<AnalysisDataset> result = useCase.addAndListDatasets("analysis/1", datasets);

    assertThat(result).hasSize(1);

    assertThat(result.get(0))
        .usingRecursiveComparison()
        .ignoringFields(IGNORED_PROTO_FIELDS)
        .isEqualTo(AnalysisDataset.newBuilder()
            .setName("analysis/1/datasets/1")
            .setAlertCount(10)
            .setPendingAlerts(0)
            .build());

    assertThat(analysisDatasetRepository
        .getAnalysisDatasetList())
        .containsExactly(AnalysisDatasetEntity.builder()
            .id(AnalysisDatasetKey.builder().analysisId(1L).datasetId(1L).build()).build());
  }

  @MockitoSettings(strictness = Strictness.LENIENT)
  @Test
  void addMultipleDatasets() {
    when(listAnalysisDatasetUseCase.listAnalysisDatasets(
        asList(
            AnalysisDatasetKey.builder().analysisId(1L).datasetId(1L).build(),
            AnalysisDatasetKey.builder().analysisId(1L).datasetId(2L).build())))
        .thenReturn(asList(AnalysisDataset
            .newBuilder()
            .setName("analysis/1/datasets/1")
            .setAlertCount(10)
            .setPendingAlerts(0)
            .build(), AnalysisDataset
            .newBuilder()
            .setName("analysis/1/datasets/2")
            .setAlertCount(20)
            .setPendingAlerts(0)
            .build()));

    List<String> datasets = asList("datasets/1", "datasets/2");
    List<AnalysisDataset> result = useCase.addAndListDatasets("analysis/1", datasets);

    assertThat(result).hasSize(2);

    assertThat(result.get(0))
        .usingRecursiveComparison()
        .ignoringFields(IGNORED_PROTO_FIELDS)
        .isEqualTo(AnalysisDataset.newBuilder()
            .setName("analysis/1/datasets/1")
            .setAlertCount(10)
            .setPendingAlerts(0)
            .build());

    assertThat(result.get(1))
        .usingRecursiveComparison()
        .ignoringFields(IGNORED_PROTO_FIELDS)
        .isEqualTo(AnalysisDataset.newBuilder()
            .setName("analysis/1/datasets/2")
            .setAlertCount(20)
            .setPendingAlerts(0)
            .build());

    assertThat(analysisDatasetRepository
        .getAnalysisDatasetList())
        .containsExactly(
            AnalysisDatasetEntity.builder().id(
                AnalysisDatasetKey.builder().analysisId(1L).datasetId(1L).build()).build(),
            AnalysisDatasetEntity.builder().id(
                AnalysisDatasetKey.builder().analysisId(1L).datasetId(2L).build()).build());
  }
}
