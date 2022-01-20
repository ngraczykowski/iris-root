package com.silenteight.payments.bridge.svb.newlearning.step.etl.category.service;

import com.silenteight.payments.bridge.common.dto.common.MessageStructure;
import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_MESSAGE_STRUCTURE;

@Service
class MessageStructureCategoryExtractor extends BaseCategoryValueExtractor {

  @Override
  protected String getCategoryName() {
    return CATEGORY_MESSAGE_STRUCTURE;
  }

  @Override
  protected String getValue(EtlHit etlHit) {
    var tag = etlHit.getTag();
    return MessageStructure.ofTag(tag).name();
  }
}
