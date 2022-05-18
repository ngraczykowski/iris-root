package com.silenteight.payments.bridge.svb.oldetl.service;

import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

public interface AlertedPartyDataFactory {

  AlertedPartyData extract(final ExtractDisposition extractDisposition);
}
