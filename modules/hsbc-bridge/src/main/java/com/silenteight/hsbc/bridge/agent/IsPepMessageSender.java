package com.silenteight.hsbc.bridge.agent;

import com.silenteight.proto.learningstore.ispep.v1.api.IsPepLearningStoreExchangeRequest;

public interface IsPepMessageSender {

  void send(IsPepLearningStoreExchangeRequest isPepLearningStoreExchangeRequest);
}
