package com.silenteight.payments.bridge.app.integration.learning;

import com.silenteight.payments.bridge.svb.learning.reader.domain.IndexRegisterAlertRequest;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.port.IndexLearningAlertPort;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import java.util.List;

import static com.silenteight.payments.bridge.app.integration.learning.IndexLearningEndpoint.INDEX;
import static com.silenteight.payments.bridge.app.integration.learning.IndexLearningEndpoint.INDEX_FOR_LEARNING;

@MessagingGateway
@SuppressWarnings("unused")
interface IndexLearningGateway extends IndexLearningAlertPort {

  @Gateway(requestChannel = INDEX_FOR_LEARNING)
  void indexForLearning(List<IndexRegisterAlertRequest> indexRegisterAlertRequest);

  @Gateway(requestChannel = INDEX)
  void index(List<LearningAlert> learningAlerts);

}
