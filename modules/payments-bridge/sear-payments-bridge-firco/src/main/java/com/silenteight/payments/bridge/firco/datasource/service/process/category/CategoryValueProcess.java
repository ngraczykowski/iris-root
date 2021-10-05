package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.event.AlertRegisteredEvent;
import com.silenteight.payments.bridge.svb.etl.response.HitData;

interface CategoryValueProcess {

  CategoryValue extract(AlertRegisteredEvent data, HitData hitData, String matchName);
}
