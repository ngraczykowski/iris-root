package com.silenteight.hsbc.bridge.ispep;

import com.silenteight.learningstore.v1.api.exchange.IsPepLearningStoreExchangeRequest;

public interface IsPepMessageSender {

  void send(IsPepLearningStoreExchangeRequest isPepLearningStoreExchangeRequest);
}
