package com.silenteight.payments.bridge.firco.core.alertmessage.port;

import java.util.UUID;

public interface IssueRecommendationUseCase {

  void issue(UUID alertMessageId);

}
