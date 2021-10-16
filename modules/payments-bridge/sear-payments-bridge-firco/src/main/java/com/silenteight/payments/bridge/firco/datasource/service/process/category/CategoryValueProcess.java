package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

interface CategoryValueProcess {

  CategoryValue extract(HitData hitData, String matchName);
}
