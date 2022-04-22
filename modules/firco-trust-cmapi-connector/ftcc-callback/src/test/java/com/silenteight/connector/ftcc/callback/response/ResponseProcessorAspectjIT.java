package com.silenteight.connector.ftcc.callback.response;

import com.silenteight.connector.ftcc.callback.exception.NonRecoverableCallbackException;
import com.silenteight.connector.ftcc.callback.response.ResponseProcessorConfiguration.TestConfigAspectJ;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestPropertySource("classpath:/data-test.properties")
@ActiveProfiles("mockcorebridge")
@ContextConfiguration(classes = { TestConfigAspectJ.class })
class ResponseProcessorAspectjIT extends BaseDataJpaTest {

  @Autowired private ResponseProcessor underTest;

  @DisplayName("When could not build CallbackRequestDto should throw NonRecoverable...Exception")
  @Test
  void testWithRandomDataShouldThrowExceptionWhileGeneratingCallbackResponse() {
    var messageBatchCompleted = MessageBatchCompleted
        .newBuilder()
        .setBatchId(BatchResource.toResourceName(randomUUID()))
        .setAnalysisName("analysis/1")
        .build();

    assertThrows(
        NonRecoverableCallbackException.class,
        () -> underTest.process(messageBatchCompleted)).getMessage();
  }
}

