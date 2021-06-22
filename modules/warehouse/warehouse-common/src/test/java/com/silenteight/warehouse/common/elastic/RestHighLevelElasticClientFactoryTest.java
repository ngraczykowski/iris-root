package com.silenteight.warehouse.common.elastic;

import lombok.SneakyThrows;

import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;
import com.silenteight.warehouse.common.testing.rest.WithElasticAccessCredentials;
import com.silenteight.warehouse.common.testing.rest.WithElasticForbiddenCredentials;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;

import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.PRODUCTION_ELASTIC_INDEX_NAME;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.*;
import static org.elasticsearch.client.RequestOptions.DEFAULT;
import static org.elasticsearch.rest.RestStatus.UNAUTHORIZED;

@SpringBootTest(classes = RestHighLevelElasticClientTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class
})
class RestHighLevelElasticClientFactoryTest {

  private static final String DOC_ID = "id";

  @Autowired
  SimpleElasticTestClient simpleElasticTestClient;

  @Autowired
  RestHighLevelElasticClientFactory underTest;

  @BeforeEach
  void init() {
    simpleElasticTestClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOC_ID, emptyMap());
  }

  @AfterEach
  void cleanup() {
    simpleElasticTestClient.removeIndex(PRODUCTION_ELASTIC_INDEX_NAME);
  }

  @Test
  @SneakyThrows
  @WithElasticForbiddenCredentials
  void shouldAccessElasticSearchUsingAdminCredentialsWhenUserHasNoAccess() {
    RestHighLevelClient adminClient = underTest.getAdminClient();
    testMethod(adminClient).call();

    assertThatNoException();
  }

  @Test
  @SneakyThrows
  @WithElasticAccessCredentials
  void shouldAccessElasticSearchUsingUserCredentialsWhenUserHasAccess() {
    RestHighLevelClient userClient = underTest.getUserAwareClient();
    testMethod(userClient).call();

    assertThatNoException();
  }

  @Test
  @WithElasticForbiddenCredentials
  void shouldThrowElasticSearchUsingUserCredentialsWhenUserHasNoAccess() {
    RestHighLevelClient userClient = underTest.getUserAwareClient();
    assertThatThrownBy(testMethod(userClient))
        .isInstanceOf(ElasticsearchStatusException.class)
        .extracting("status")
        .isEqualTo(UNAUTHORIZED);
  }

  @SneakyThrows
  private ThrowingCallable testMethod(RestHighLevelClient client) {
    GetRequest getRequest = new GetRequest(PRODUCTION_ELASTIC_INDEX_NAME);
    getRequest.id(DOC_ID);

    return () -> {
      try {
        client.get(getRequest, DEFAULT);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    };
  }
}
