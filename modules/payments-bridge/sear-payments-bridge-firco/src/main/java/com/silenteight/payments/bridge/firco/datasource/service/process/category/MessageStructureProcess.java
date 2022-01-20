package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.dto.common.MessageStructure;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_MESSAGE_STRUCTURE;

@Service
@RequiredArgsConstructor
class MessageStructureProcess extends BaseCategoryValueProcess {

  @Override
  protected String getCategoryName() {
    return CATEGORY_MESSAGE_STRUCTURE;
  }

  @Override
  protected String getValue(HitData hitData) {
    var tag = hitData.getTag();
    return MessageStructure.ofTag(tag).name();
  }
}
