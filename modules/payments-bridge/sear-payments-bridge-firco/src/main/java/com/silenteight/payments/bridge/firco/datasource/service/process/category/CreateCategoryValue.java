package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import java.util.List;
import java.util.Map;

public interface CreateCategoryValue {

  void createStructuredCategoryValues(AeAlert alert, List<HitData> hitsData);

  void createUnstructuredCategoryValues(
      AeAlert alert, Map<String, HitAndWatchlistPartyData> hitAndWatchlistPartyData);

}
