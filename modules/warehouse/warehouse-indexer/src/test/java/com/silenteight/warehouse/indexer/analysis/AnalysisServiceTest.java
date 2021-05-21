package com.silenteight.warehouse.indexer.analysis;

import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

import static com.silenteight.warehouse.indexer.analysis.AnalysisMetadataFixture.ANALYSIS_METADATA;
import static com.silenteight.warehouse.indexer.analysis.AnalysisMetadataFixture.ELASTIC_INDEX_PATTERN;
import static com.silenteight.warehouse.indexer.analysis.AnalysisMetadataFixture.TENANT;
import static com.silenteight.warehouse.indexer.analysis.NewAnalysisEventFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@ActiveProfiles("jpa-test")
@ContextConfiguration(
    classes = AnalysisTestConfiguration.class
)
@Transactional(propagation = NOT_SUPPORTED)
/*
  @Transactional(propagation = NOT_SUPPORTED)
  effectively disables @Transactional applied to BaseDataJpaTest via @DataJpaTest.
  Leveraging @Transaction has its pitfalls - it does not handle Propagation.REQUIRES_NEW
*/
class AnalysisServiceTest extends BaseDataJpaTest {

  @Autowired
  EntityManager testEntityManager;

  @Autowired
  AnalysisService underTest;

  @Autowired
  TestAnalysisMetadataRepository testAnalysisMetadataRepository;

  @Autowired
  AnalysisMetadataRepository analysisMetadataRepository;

  @Autowired
  TestNewAnalysisHandler testNewAnalysisHandler;

  @AfterEach
  public void cleanup() {
    testNewAnalysisHandler.reset();
    testAnalysisMetadataRepository.truncateAnalysisMetadata();
  }

  @Test
  void shouldStoreSimulationAnalysis() {
    underTest.createAnalysisMetadata(ANALYSIS, SIMULATION_NAMING_STRATEGY);

    List<AnalysisMetadataEntity> allEntries = testAnalysisMetadataRepository.findAll();
    assertThat(allEntries).hasSize(1);
    AnalysisMetadataEntity analysisMetadataEntity = allEntries.get(0);
    assertThat(analysisMetadataEntity.getAnalysisId()).isEqualTo(ANALYSIS_ID);
    assertThat(analysisMetadataEntity.getTenant()).isEqualTo(SIMULATION_TENANT);
    assertThat(analysisMetadataEntity.getElasticIndexPattern()).isEqualTo(
        SIMULATION_ELASTIC_SEARCH_INDEX);
  }

  @Test
  void shouldStoreProductionAnalysis() {
    underTest.createAnalysisMetadata(ANALYSIS, PRODUCTION_NAMING_STRATEGY);

    List<AnalysisMetadataEntity> allEntries = testAnalysisMetadataRepository.findAll();
    assertThat(allEntries).hasSize(1);
    AnalysisMetadataEntity analysisMetadataEntity = allEntries.get(0);
    assertThat(analysisMetadataEntity.getAnalysisId()).isEqualTo(ANALYSIS_ID);
    assertThat(analysisMetadataEntity.getTenant()).isEqualTo(PRODUCTION_TENANT);
    assertThat(analysisMetadataEntity.getElasticIndexPattern()).isEqualTo(
        PRODUCTION_ELASTIC_SEARCH_INDEX);
  }

  @Test
  void shouldEmitNewEventOnStore() {
    underTest.createAnalysisMetadata(ANALYSIS, SIMULATION_NAMING_STRATEGY);

    NewAnalysisEvent event = testNewAnalysisHandler.getLastEvent();
    assertThat(event.getDate()).isNotNull();
    assertThat(event.getAnalysis()).isEqualTo(ANALYSIS);
    assertThat(event.isSimulation()).isTrue();
  }

  @Test
  void shouldEmitEventOnlyOnce() {
    // given
    underTest.createAnalysisMetadata(ANALYSIS, SIMULATION_NAMING_STRATEGY);
    assertThat(testNewAnalysisHandler.getEventReceivedCount()).isEqualTo(1);

    // when + then
    assertThatThrownBy(() -> underTest.createAnalysisMetadata(ANALYSIS, SIMULATION_NAMING_STRATEGY))
        .isInstanceOf(DataIntegrityViolationException.class);
    assertThat(testNewAnalysisHandler.getEventReceivedCount()).isEqualTo(1);
  }

  @Test
  void shouldReturnCorrectTenantId() {
    analysisMetadataRepository.save(ANALYSIS_METADATA);

    assertThat(underTest.getTenantIdByAnalysis(ANALYSIS)).isEqualTo(TENANT);
  }

  @Test
  void shouldReturnCorrectMetadata() {
    analysisMetadataRepository.save(ANALYSIS_METADATA);

    Optional<AnalysisMetadataDto> analysisMetadata = underTest.getAnalysisMetadata(ANALYSIS);

    assertThat(analysisMetadata.get().getTenant())
        .isEqualTo(TENANT);
    assertThat(analysisMetadata.get().getElasticIndexName())
        .isEqualTo(ELASTIC_INDEX_PATTERN);
  }
}
