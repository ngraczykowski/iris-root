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

  @Mock
  private DatasetAlertsReader datasetAlertsReader;

  @BeforeEach
  void setUp() {
    var addUseCase = new AddDatasetsToAnalysisUseCase(
        analysisDatasetRepository, datasetAlertsReader,
        new PublishAnalysisAlertUseCase(applicationEventPublisher));

    useCase = new AddAndListDatasetsInAnalysisUseCase(addUseCase, listAnalysisDatasetUseCase);
  }

  @Test
  void addDataset() {
    var analysisDataset = AnalysisDataset.newBuilder()
        .setName("analysis/1/datasets/1")
        .setAlertCount(10)
        .setPendingAlerts(0)
        .build();

    when(listAnalysisDatasetUseCase.listAnalysisDatasets(List.of(new AnalysisDatasetKey(1L, 1L))))
        .thenReturn(Collections.singletonList(analysisDataset));

    List<String> datasets = List.of("datasets/1");
    List<AnalysisDataset> result = useCase.addAndListDatasets("analysis/1", datasets);

    assertThat(result).hasSize(1);

    assertThat(result.get(0))
        .usingRecursiveComparison()
        .ignoringFields(IGNORED_PROTO_FIELDS)
        .isEqualTo(analysisDataset);

    assertThat(analysisDatasetRepository
        .getAnalysisDatasetList())
        .containsExactly(new AnalysisDatasetEntity(1L, 1L));
  }

  @MockitoSettings(strictness = Strictness.LENIENT)
  @Test
  void addMultipleDatasets() {
    var analysisDataset1 = AnalysisDataset.newBuilder()
        .setName("analysis/1/datasets/1")
        .setAlertCount(10)
        .setPendingAlerts(0)
        .build();
    var analysisDataset2 = AnalysisDataset.newBuilder()
        .setName("analysis/1/datasets/2")
        .setAlertCount(20)
        .setPendingAlerts(0)
        .build();

    when(listAnalysisDatasetUseCase.listAnalysisDatasets(
        List.of(new AnalysisDatasetKey(1L, 1L), new AnalysisDatasetKey(1L, 2L))))
        .thenReturn(asList(analysisDataset1, analysisDataset2));

    List<String> datasets = List.of("datasets/1", "datasets/2");
    List<AnalysisDataset> result = useCase.addAndListDatasets("analysis/1", datasets);

    assertThat(result).hasSize(2);

    assertThat(result.get(0))
        .usingRecursiveComparison()
        .ignoringFields(IGNORED_PROTO_FIELDS)
        .isEqualTo(analysisDataset1);

    assertThat(result.get(1))
        .usingRecursiveComparison()
        .ignoringFields(IGNORED_PROTO_FIELDS)
        .isEqualTo(analysisDataset2);
  }
}
