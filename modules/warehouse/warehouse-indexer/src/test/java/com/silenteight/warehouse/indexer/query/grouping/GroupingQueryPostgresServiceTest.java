package com.silenteight.warehouse.indexer.query.grouping;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MappedKeys;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static com.silenteight.warehouse.indexer.IndexerFixtures.PRODUCTION_ELASTIC_READ_ALIAS_NAME;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.PROCESSING_TIMESTAMP;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.INDEX_TIMESTAMP;
import static java.time.OffsetDateTime.parse;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = GroupingQueryTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class, PostgresTestInitializer.class })
@ActiveProfiles({ "jpa-test" })
class GroupingQueryPostgresServiceTest {

  @Autowired
  @Qualifier("postgres")
  GroupingQueryService service;

  @Test
  public void dummyTest() {
    assertThat(service.generate(FetchGroupedTimeRangedDataRequest.builder()
        .indexes(of(PRODUCTION_ELASTIC_READ_ALIAS_NAME))
        .fields(of(MappedKeys.RISK_TYPE_KEY))
        .dateField(INDEX_TIMESTAMP)
        .from(parse(PROCESSING_TIMESTAMP))
        .to(parse(PROCESSING_TIMESTAMP).plusSeconds(1))
        .build())).isNull();
  }
}