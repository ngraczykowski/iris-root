package com.silenteight.payments.bridge.firco.alertmessage.port;

import com.silenteight.proto.payments.bridge.internal.v1.event.MessageStored;

public interface IssueRecommendationUseCase {

  void issue(MessageStored messageStored);

}
