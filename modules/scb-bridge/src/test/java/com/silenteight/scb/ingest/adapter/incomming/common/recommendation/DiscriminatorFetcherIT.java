package com.silenteight.scb.ingest.adapter.incomming.common.recommendation;

import com.silenteight.scb.ingest.adapter.incomming.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.SyncTestInitializer;
import com.silenteight.scb.ingest.adapter.incomming.common.config.SyncDataSourcesConfiguration;
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.DiscriminatorFetcher.DiscriminatorNotFoundException;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.transaction.annotation.Propagation.NEVER;

@ContextConfiguration(initializers = SyncTestInitializer.class, classes = {
    SyncDataSourcesConfiguration.class,
    DiscriminatorFetcherConfiguration.class })
@Import({ RecommendationOrderProperties.class, ScbBridgeConfigProperties.class })
@Sql(scripts = "testDiscriminatorFetcher.sql")
@Sql(scripts = "clearFetcherData.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@SqlConfig(
    dataSource = "externalOnDemandDataSource",
    transactionManager = "externalOnDemandTransactionManager")
@Transactional(propagation = NEVER, transactionManager = "externalOnDemandTransactionManager")
class DiscriminatorFetcherIT extends BaseJdbcTest {

  @Autowired
  private DiscriminatorFetcher fetcher;

  @Test
  void shouldRaiseDiscriminatorNotFoundWhenRecordNotFound() {
    //given
    String systemId = "dummySystemId";

    //then
    assertThrows(DiscriminatorNotFoundException.class, () -> fetcher.fetch(systemId));
  }

  @Test
  void shouldReturnEmptyWhenNoDecisionAndNoFilteredValue() {
    //given
    String systemId = "sysId-1";

    //when
    Optional<String> foundDiscriminator = fetcher.fetch(systemId);

    //then
    assertThat(foundDiscriminator.isPresent()).isFalse();
  }

  @Test
  void shouldFetchFilteredValueWhenNoDecisions() {
    //given
    String systemId = "sysId-2";

    //when
    Optional<String> foundDiscriminator = fetcher.fetch(systemId);

    //then
    assertThat(foundDiscriminator.isPresent()).isTrue();
    assertThat(foundDiscriminator.get()).isEqualTo("2019-09-20T00:48:58Z");
  }

  @Test
  void shouldFetchFilteredValueWhenThereIsNoResetDecisions() {
    //given
    String systemId = "sysId-3";

    //when
    Optional<String> foundDiscriminator = fetcher.fetch(systemId);

    //then
    assertThat(foundDiscriminator.isPresent()).isTrue();
    assertThat(foundDiscriminator.get()).isEqualTo("2019-10-20T00:48:58Z");
  }

  @Test
  void shouldFetchLastResetDecision() {
    //given
    String systemId = "sysId-4";

    //when
    Optional<String> foundDiscriminator = fetcher.fetch(systemId);

    //then
    assertThat(foundDiscriminator.isPresent()).isTrue();
    assertThat(foundDiscriminator.get()).isEqualTo("2019-12-21T00:48:58Z");
  }
}
