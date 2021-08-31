package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.api.v1.AnalysisDataset;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddAndListDatasetsToAnalysisUseCaseTest {

  private static final String[] IGNORED_PROTO_FIELDS = { "memoizedHashCode" };

  private final AnalysisAlertsAddedGateway gateway = new DummyAnalysisAlertsAddedGateway();
  private final DatasetAlertsAdderMock datasetAlertsAdder = new DatasetAlertsAdderMock();
  private final InMemoryAnalysisDatasetRepository analysisDatasetRepository
      = new InMemoryAnalysisDatasetRepository();
  private final AnalysisDatasetQueryRepository analysisDatasetQueryRepository =
      analysisDatasetRepository.getQueryRepository();
  private final AddDatasetAlertsToAnalysisUseCase addDatasetAlertsToAnalysisUseCase =
      new AddDatasetAlertsToAnalysisUseCase(datasetAlertsAdder, gateway);
  private final AddDatasetToAnalysisUseCase addDatasetToAnalysisUseCase =
      new AddDatasetToAnalysisUseCase(analysisDatasetRepository);
  private final ListAnalysisDatasetUseCase listAnalysisDatasetUseCase =
      new ListAnalysisDatasetUseCase(analysisDatasetQueryRepository);

  private final AddAndListDatasetInAnalysisUseCase useCase =
      new AddAndListDatasetInAnalysisUseCase(addDatasetAlertsToAnalysisUseCase,
          addDatasetToAnalysisUseCase, listAnalysisDatasetUseCase);

  // TODO(ahaczewski): Fix the addDataset test and enable it.
  @Test
  @Disabled
  void addDataset() {
    var analysisDataset = AnalysisDataset.newBuilder()
        .setName("analysis/1/datasets/1")
        .setAlertCount(10)
        .setPendingAlerts(0)
        .build();

    List<String> datasets = List.of("datasets/1");
    List<AnalysisDataset> result = useCase.batchAddAndListDataset("analysis/1", datasets);

    assertThat(result).hasSize(1);

    assertThat(result.get(0))
        .usingRecursiveComparison()
        .ignoringFields(IGNORED_PROTO_FIELDS)
        .isEqualTo(analysisDataset);

    assertThat(analysisDatasetRepository
        .getAnalysisDatasetList())
        .containsExactly(new AnalysisDatasetEntity(1L, 1L));
  }

  // TODO(ahaczewski): Fix the addMultipleDatasets test and enable it.
  @Test
  @Disabled
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
    List<AnalysisDataset> result = useCase.batchAddAndListDataset("analysis/1", datasets);

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
