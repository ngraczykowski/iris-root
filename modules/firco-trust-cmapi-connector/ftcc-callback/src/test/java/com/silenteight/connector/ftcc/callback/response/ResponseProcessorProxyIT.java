package com.silenteight.connector.ftcc.callback.response;

import com.silenteight.connector.ftcc.callback.response.ResponseProcessorConfiguration.TestConfigProxy;
import com.silenteight.connector.ftcc.common.database.partition.DatabasePartitionConfiguration;
import com.silenteight.connector.ftcc.common.resource.BatchResource;
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@TestPropertySource("classpath:/data-test.properties")
@ActiveProfiles("mockcorebridge")
@ContextConfiguration(classes = { TestConfigProxy.class, DatabasePartitionConfiguration.class })
class ResponseProcessorProxyIT extends BaseDataJpaTest {

  @Autowired
  private ResponseProcessor underTest;

  @DisplayName("Remove this!!! Show only differences between Proxy and AspectJ async mode.")
  @Test
  void testWithRandomDataShouldThrowExceptionWhileGeneratingCallbackResponse() {
    var messageBatchCompleted = MessageBatchCompleted
        .newBuilder()
        .setBatchId(BatchResource.toResourceName(randomUUID()))
        .setAnalysisName("analysis/1")
        .build();

    // NOTE: expected NonRecoverableCallbackException.class but isn't
    assertDoesNotThrow(() -> underTest.process(messageBatchCompleted));
  }
}
