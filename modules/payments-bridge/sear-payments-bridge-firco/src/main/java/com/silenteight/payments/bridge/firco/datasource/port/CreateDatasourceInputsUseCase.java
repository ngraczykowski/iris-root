package com.silenteight.payments.bridge.firco.datasource.port;

import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import java.util.List;
import java.util.Map;

public interface CreateDatasourceInputsUseCase {

  void processStructured(AeAlert alert, List<HitData> hitsData);

  void processUnstructured(
      String alertName, Map<String, HitAndWatchlistPartyData> hitAndWatchlistPartyData);

}
