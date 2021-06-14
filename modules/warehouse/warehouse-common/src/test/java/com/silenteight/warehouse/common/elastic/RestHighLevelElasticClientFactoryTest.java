package com.silenteight.warehouse.common.elastic;

import lombok.SneakyThrows;

import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.rest.WithElasticAccessCredentials;
import com.silenteight.warehouse.common.testing.rest.WithElasticForbiddenCredentials;

import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.*;
import static org.elasticsearch.client.RequestOptions.DEFAULT;
import static org.elasticsearch.rest.RestStatus.UNAUTHORIZED;

@SpringBootTest(classes = RestHighLevelElasticClientTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class
})
class RestHighLevelElasticClientFactoryTest {

  @Autowired
  RestHighLevelElasticClientFactory underTest;

  @Test
  @SneakyThrows
  @WithElasticForbiddenCredentials
  void shouldAccessElasticSearchUsingAdminCredentialsWhenUserHasNoAccess() {
    RestHighLevelClient adminClient = underTest.getAdminClient();
    adminClient.ping(DEFAULT);

    assertThatNoException();
  }

  @Test
  @SneakyThrows
  @WithElasticAccessCredentials
  void shouldAccessElasticSearchUsingUserCredentialsWhenUserHasAccess() {
    RestHighLevelClient adminClient = underTest.getUserAwareClient();
    adminClient.ping(DEFAULT);

    assertThatNoException();
  }

  @Test
  @WithElasticForbiddenCredentials
  void shouldThrowElasticSearchUsingUserCredentialsWhenUserHasNoAccess() {
    RestHighLevelClient adminClient = underTest.getUserAwareClient();
    assertThatThrownBy(() -> adminClient.ping(DEFAULT))
        .isInstanceOf(ElasticsearchStatusException.class)
        .extracting("status")
        .isEqualTo(UNAUTHORIZED);
  }
}
