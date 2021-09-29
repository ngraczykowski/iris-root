package com.silenteight.warehouse.indexer.analysis;

import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static com.silenteight.warehouse.indexer.analysis.NewAnalysisEventFixture.ANALYSIS;
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
class UniqueAnalysisFactoryTest extends BaseDataJpaTest {

  @Autowired
  UniqueAnalysisFactory underTest;

  @Autowired
  TestAnalysisMetadataRepository testAnalysisMetadataRepository;

  @Autowired
  TestNewSimulationAnalysisHandler testNewAnalysisHandler;

  @AfterEach()
  public void cleanup() {
    testNewAnalysisHandler.reset();
    testAnalysisMetadataRepository.truncateAnalysisMetadata();
  }

  @Test
  void shouldProvideAnalysisMetadata() {
    AnalysisMetadataDto uniqueAnalysis =
        underTest.getUniqueAnalysis(ANALYSIS);

    assertThat(uniqueAnalysis.getTenant()).isNotNull();
    assertThat(uniqueAnalysis.getElasticIndexName()).isNotNull();
  }

  @Test
  void shouldHandleMultipleCalls() {
    underTest.getUniqueAnalysis(ANALYSIS);
    underTest.getUniqueAnalysis(ANALYSIS);

    assertThat(testNewAnalysisHandler.getEventReceivedCount()).isEqualTo(1);
  }
}
