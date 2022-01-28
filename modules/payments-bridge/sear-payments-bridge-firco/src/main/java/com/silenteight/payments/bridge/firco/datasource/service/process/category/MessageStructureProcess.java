package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.common.dto.common.MessageStructure;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_NAME_MESSAGE_STRUCTURE;

@Service
@RequiredArgsConstructor
class MessageStructureProcess implements CreateCategoryValueUnstructured {

  @Override
  public CategoryValue createCategoryValue(
      String alertName, String matchName, HitAndWatchlistPartyData hitAndWatchlistPartyData) {
    return CategoryValue
        .newBuilder()
        .setName(CATEGORY_NAME_MESSAGE_STRUCTURE)
        .setAlert(alertName)
        .setMatch(matchName)
        .setSingleValue(getValue(hitAndWatchlistPartyData.getTag()))
        .build();
  }

  private static String getValue(String tag) {
    return MessageStructure.ofTag(tag).name();
  }
}
