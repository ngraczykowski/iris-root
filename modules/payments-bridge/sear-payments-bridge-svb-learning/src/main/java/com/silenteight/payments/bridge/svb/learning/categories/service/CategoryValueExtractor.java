package com.silenteight.payments.bridge.svb.learning.categories.service;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

interface CategoryValueExtractor {

  CategoryValue extract(LearningMatch learningMatch);
}
