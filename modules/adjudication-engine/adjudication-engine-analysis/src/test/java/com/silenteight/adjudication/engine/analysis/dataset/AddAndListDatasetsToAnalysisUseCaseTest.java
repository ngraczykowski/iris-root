package com.silenteight.adjudication.engine.analysis.dataset;

import com.silenteight.adjudication.api.v1.AnalysisDataset;
import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.adjudication.engine.analysis.analysis.AnalysisAlertsAddedGateway;
import com.silenteight.adjudication.engine.analysis.analysis.DataSetRepository;
import com.silenteight.adjudication.engine.analysis.analysis.DatasetAlertsAdderMock;
import com.silenteight.adjudication.engine.analysis.analysis.DummyAnalysisAlertsAddedGateway;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class AddAndListDatasetsToAnalysisUseCaseTest {

  private static final String[] IGNORED_PROTO_FIELDS = { "memoizedHashCode" };

  private final AnalysisAlertsAddedGateway gateway = new DummyAnalysisAlertsAddedGateway();
  private final DatasetAlertsAdderMock datasetAlertsAdder = new DatasetAlertsAdderMock();
  private final InMemoryAnalysisDatasetRepository analysisDatasetRepository
      = new InMemoryAnalysisDatasetRepository();
  private final AnalysisDatasetQueryRepository analysisDatasetQueryRepository =
      analysisDatasetRepository.getQueryRepository();
  private final DataSetRepository datasetRepository =
      analysisDatasetRepository.getDatasetRepository();

  private final AddDatasetAlertsToAnalysisUseCase addDatasetAlertsToAnalysisUseCase =
      new AddDatasetAlertsToAnalysisUseCase(datasetAlertsAdder, gateway);
  private final AddDatasetToAnalysisUseCase addDatasetToAnalysisUseCase =
      new AddDatasetToAnalysisUseCase(analysisDatasetRepository);
  private final ListAnalysisDatasetUseCase listAnalysisDatasetUseCase =
      new ListAnalysisDatasetUseCase(analysisDatasetQueryRepository);

  private final AnalysisDatasetFacade useCase =
      new AnalysisDatasetFacade(addDatasetAlertsToAnalysisUseCase,
          addDatasetToAnalysisUseCase, listAnalysisDatasetUseCase);

  @Test
  void addDataset() {
    var analysisDataset = AnalysisDataset.newBuilder()
        .setName("analysis/1/datasets/1")
        .setAlertCount(10)
        .setPendingAlerts(0)
        .build();

    var dataset = Dataset.newBuilder().setName("datasets/1").setAlertCount(10).build();

    datasetRepository.save(dataset);

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

    var dataset = Dataset.newBuilder().setName("datasets/1").setAlertCount(10).build();
    var dataset2 = Dataset.newBuilder().setName("datasets/2").setAlertCount(20).build();

    datasetRepository.save(dataset);
    datasetRepository.save(dataset2);

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
