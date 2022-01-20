package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.dto.common.MessageStructure;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_MESSAGE_STRUCTURE;

@Service
@RequiredArgsConstructor
class MessageStructureExtractor extends BaseCategoryValueExtractor {

  @Override
  protected String getCategoryName() {
    return CATEGORY_MESSAGE_STRUCTURE;
  }

  @Override
  protected String getValue(LearningMatch learningMatch) {
    var tag = learningMatch.getHitTag();
    return MessageStructure.ofTag(tag).name();
  }
}
