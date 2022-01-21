package com.silenteight.payments.bridge.svb.newlearning.step.etl.category.service;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.svb.newlearning.domain.HitComposite;

interface UnstructuredCategoryValueExtractor {

  CategoryValue extract(HitComposite hit, RegisterAlertResponse registeredAlert);
}
