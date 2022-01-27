package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.common.dto.common.MessageStructure;
import com.silenteight.payments.bridge.firco.datasource.model.CategoryValueExtractModel;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_NAME_MESSAGE_STRUCTURE;

@Service
@RequiredArgsConstructor
class MessageStructureProcess implements CategoryValueProcess {

  @Override
  public CategoryValue createCategoryValue(CategoryValueExtractModel categoryValueExtractModel) {
    return CategoryValue
        .newBuilder()
        .setName(CATEGORY_NAME_MESSAGE_STRUCTURE)
        .setAlert(categoryValueExtractModel.getAlertName())
        .setMatch(categoryValueExtractModel.getMatchName())
        .setSingleValue(getValue(categoryValueExtractModel.getHitData()))
        .build();
  }

  private static String getValue(HitData hitData) {
    var tag = hitData.getTag();
    return MessageStructure.ofTag(tag).name();
  }
}
