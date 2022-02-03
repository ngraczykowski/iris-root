package com.silenteight.warehouse.indexer.query.grouping;

import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static com.silenteight.warehouse.indexer.IndexerFixtures.PRODUCTION_ELASTIC_READ_ALIAS_NAME;
import static com.silenteight.warehouse.indexer.IndexerFixtures.PRODUCTION_ELASTIC_WRITE_INDEX_NAME;

class GroupingQueryElasticSearchServiceTest extends GroupingQueryServiceTest {

  @Autowired
  SimpleElasticTestClient testClient;

  @Autowired
  GroupingQueryService underTest;

  @BeforeEach
  void init() {
    testClient.createDefaultIndexTemplate(
        PRODUCTION_ELASTIC_WRITE_INDEX_NAME, PRODUCTION_ELASTIC_READ_ALIAS_NAME);
  }

  @AfterEach
  void cleanup() {
    testClient.removeDefaultIndexTemplate();
    removeData();
  }

  private void removeData() {
    testClient.removeIndex(PRODUCTION_ELASTIC_WRITE_INDEX_NAME);
  }

  @Override
  protected GroupingQueryService provideServiceForTest() {
    return underTest;
  }

  @Override
  protected void insertRow(
      String id, Map<String, Object> alertMapping, String unused, String unused2) {
    testClient.storeData(PRODUCTION_ELASTIC_WRITE_INDEX_NAME, id, alertMapping);
  }
}
