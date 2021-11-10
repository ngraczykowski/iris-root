package com.silenteight.warehouse.retention.simulation;

import com.silenteight.dataretention.api.v1.AnalysisExpired;
import com.silenteight.warehouse.indexer.alert.indexing.AlertIndexService;
import com.silenteight.warehouse.indexer.alert.indexing.ElasticsearchProperties;
import com.silenteight.warehouse.indexer.alert.indexing.MapWithIndex;
import com.silenteight.warehouse.indexer.query.streaming.AllDataProvider;
import com.silenteight.warehouse.indexer.query.streaming.FetchAllDataRequest;
import com.silenteight.warehouse.indexer.query.streaming.FetchedDocument;
import com.silenteight.warehouse.indexer.simulation.analysis.SimulationNamingStrategy;

import org.apache.commons.collections4.keyvalue.DefaultMapEntry;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.DISCRIMINATOR;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RetentionSimulationUseCaseTest {

  private static final String FIRST_ANALYSIS_ID = "b249b1b2-f56c-4363-bfbc-c103ffda362c";
  private static final String FIRST_ANALYSIS = "analysis/" + FIRST_ANALYSIS_ID;
  private static final String SECOND_ANALYSIS_ID = "5c460815-2e38-426d-b2d7-13c6b1aef4e4";
  private static final String SECOND_ANALYSIS = "analysis/" + SECOND_ANALYSIS_ID;
  private static final AnalysisExpired ANALYSIS_EXPIRED = AnalysisExpired
      .newBuilder().addAllAnalysis(of(FIRST_ANALYSIS, SECOND_ANALYSIS)).build();

  private static final String FIRST_ANALYSIS_ID1 = "FIRST_ID1";
  private static final String SECOND_ANALYSIS_ID1 = "SECOND_ID1";
  private static final String SECOND_ANALYSIS_ID2 = "SECOND_ID2";
  private static final SimulationNamingStrategy SIMULATION_NAMING_STRATEGY =
      new SimulationNamingStrategy("test");

  private static final String FIRST_INDEX_NAME = SIMULATION_NAMING_STRATEGY
      .getElasticIndexNameForAnalysisResource(FIRST_ANALYSIS_ID);

  private static final String SECOND_INDEX_NAME = SIMULATION_NAMING_STRATEGY
      .getElasticIndexNameForAnalysisResource(SECOND_ANALYSIS_ID);

  private static final String FIRST_FIELD_TO_ERASE = "alert_recommendation_comment";
  private static final String SECOND_FIELD_TO_ERASE = "alert_firco_analyst_comment";
  private static final List<String> FIELDS_TO_ERASE =
      of(FIRST_FIELD_TO_ERASE, SECOND_FIELD_TO_ERASE);

  @Mock
  private AlertIndexService alertIndexService;
  @Mock
  private ElasticsearchProperties elasticsearchProperties;

  private RetentionSimulationUseCase underTest;

  @Captor
  private ArgumentCaptor<List<MapWithIndex>> mapWithIndexCaptor;

  @BeforeEach
  void setUp() {
    when(elasticsearchProperties.getUpdateRequestBatchSize()).thenReturn(250);

    RetentionSimulationConfiguration retentionSimulationConfiguration =
        new RetentionSimulationConfiguration();
    AllDocumentsInIndexProcessor allDocumentsInIndexProcessor = retentionSimulationConfiguration
        .allDocumentsInIndexProcessor(new TestAllDataProvider());
    RetentionSimulationProperties retentionSimulationProperties =
        new RetentionSimulationProperties(FIELDS_TO_ERASE);

    underTest = retentionSimulationConfiguration.simulationRetentionUseCase(
        alertIndexService,
        SIMULATION_NAMING_STRATEGY,
        allDocumentsInIndexProcessor,
        retentionSimulationProperties,
        elasticsearchProperties);
  }

  @Test
  void shouldSendARequestWithEraseDataWhenActivated() {
    underTest.activate(ANALYSIS_EXPIRED);

    verify(alertIndexService, times(2)).saveAlerts(mapWithIndexCaptor.capture());
    List<List<MapWithIndex>> allValues = mapWithIndexCaptor.getAllValues();
    assertThat(allValues).hasSize(2);
    List<MapWithIndex> firstIndex = allValues.get(1);
    assertThat(firstIndex.size()).isEqualTo(1);
    assertThat(firstIndex.get(0).getDocumentId()).isEqualTo(FIRST_ANALYSIS_ID1);
    assertThat(firstIndex.get(0).getPayload().size()).isEqualTo(2);
    assertThat(firstIndex.get(0).getPayload()).containsOnlyKeys(FIELDS_TO_ERASE);
    assertThat(firstIndex.get(0).getPayload()).containsOnly(getPayload());
    List<MapWithIndex> secondIndex = allValues.get(0);
    assertThat(secondIndex).hasSize(2);
    assertThat(secondIndex.get(0).getDocumentId()).isEqualTo(SECOND_ANALYSIS_ID1);
    assertThat(secondIndex.get(0).getPayload()).hasSize(2);
    assertThat(secondIndex.get(0).getPayload()).containsOnly(getPayload());
    assertThat(secondIndex.get(1).getDocumentId()).isEqualTo(SECOND_ANALYSIS_ID2);
    assertThat(secondIndex.get(1).getPayload()).hasSize(2);
    assertThat(secondIndex.get(1).getPayload()).containsOnly(getPayload());
  }

  @Test
  void shouldDoNothingOnEmptySimList() {
    underTest.activate(AnalysisExpired.newBuilder().build());

    verifyNoInteractions(alertIndexService);
  }

  private DefaultMapEntry<String, String>[] getPayload() {
    return new DefaultMapEntry[] {
        getEntry(FIRST_FIELD_TO_ERASE), getEntry(SECOND_FIELD_TO_ERASE) };
  }

  @NotNull
  private DefaultMapEntry<String, String> getEntry(String firstFieldToErase) {
    return new DefaultMapEntry<>(firstFieldToErase, "");
  }

  private static class TestAllDataProvider implements AllDataProvider {

    @Override
    public void fetchData(
        FetchAllDataRequest request, Consumer<Collection<FetchedDocument>> consumer) {

      if (request.getIndexes().contains(FIRST_INDEX_NAME))
        consumer.accept(getFirstAnalysisIds());
      if (request.getIndexes().contains(SECOND_INDEX_NAME))
        consumer.accept(getSecondAnalysisIds());
    }

    private Collection<FetchedDocument> getSecondAnalysisIds() {
      return of(getFetchedDocument(FIRST_ANALYSIS_ID1));
    }

    private Collection<FetchedDocument> getFirstAnalysisIds() {
      return of(getFetchedDocument(SECOND_ANALYSIS_ID1), getFetchedDocument(SECOND_ANALYSIS_ID2));
    }

    private FetchedDocument getFetchedDocument(String discriminator) {
      return new FetchedDocument(Map.of(DISCRIMINATOR, discriminator));
    }
  }
}
