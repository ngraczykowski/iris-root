package com.silenteight.payments.bridge.svb.etl.port;

import com.silenteight.payments.bridge.svb.etl.model.GetAccountNumberRequest;

import java.util.Optional;

public interface GetAccountNumberUseCase {

  Optional<String> getAccountNumber(GetAccountNumberRequest request);
}
