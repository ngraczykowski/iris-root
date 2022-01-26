package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import java.util.List;

public interface CreateCategoryValue {

  List<CategoryValue> createStructuredCategoryValues(AeAlert alert, List<HitData> hitsData);

  List<CategoryValue> createUnstructuredCategoryValues(
      String alertName, String matchName, HitAndWatchlistPartyData hitAndWatchlistPartyData);


}
