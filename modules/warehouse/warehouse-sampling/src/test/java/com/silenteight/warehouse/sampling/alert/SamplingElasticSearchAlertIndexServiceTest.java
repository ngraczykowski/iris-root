package com.silenteight.warehouse.sampling.alert;

import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.PRODUCTION_ELASTIC_INDEX_NAME;
import static com.silenteight.warehouse.sampling.alert.SamplingTestFixtures.*;

@TestPropertySource(properties = "warehouse.alert-provider.is-sql-support-enabled=false")
class SamplingElasticSearchAlertIndexServiceTest extends SamplingAlertIndexService {

  @Autowired
  private SimpleElasticTestClient simpleElasticTestClient;

  protected void removeData() {
    simpleElasticTestClient.removeIndex(PRODUCTION_ELASTIC_INDEX_NAME);
  }

  protected void initDateForCorrectSamplingNumber() {
    saveAlert(DOCUMENT_ID_1, ALERT_1_MAP);
    saveAlert(DOCUMENT_ID_2, ALERT_2_MAP);
    saveAlert(DOCUMENT_ID_3, ALERT_3_MAP);
    saveAlert(DOCUMENT_ID_4, ALERT_4_MAP);
    saveAlert(DOCUMENT_ID_5, ALERT_5_MAP);
  }

  @Override
  protected void initDateForIncludedFilters() {
    saveAlert(DOCUMENT_ID_4, ALERT_4_MAP);
    saveAlert(DOCUMENT_ID_5, ALERT_5_MAP);
    saveAlert(DOCUMENT_ID_6, ALERT_6_MAP);
  }

  private void saveAlert(String discriminator, Map<String, Object> alert) {
    simpleElasticTestClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, discriminator, alert);
  }
}
