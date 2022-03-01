package com.silenteight.payments.bridge.svb.learning.step.etl.category.service;

import com.silenteight.payments.bridge.common.dto.common.MessageStructure;
import com.silenteight.payments.bridge.svb.learning.domain.HitComposite;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_MESSAGE_STRUCTURE;

@Service
class MessageStructureCategoryExtractor extends BaseUnstructuredCategoryValueExtractor {

  @Override
  protected String getValue(HitComposite hitComposite) {
    var tag = hitComposite.getFkcoVMatchedTag();
    return MessageStructure.ofTag(tag).name();
  }

  @Override
  protected String getCategoryName() {
    return CATEGORY_MESSAGE_STRUCTURE;
  }

}
