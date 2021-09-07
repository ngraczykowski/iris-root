package com.silenteight.payments.bridge.datasource.category.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;
import com.silenteight.payments.bridge.common.resource.ResourceName;
import com.silenteight.payments.bridge.datasource.category.model.MatchCategoryRequest;
import com.silenteight.payments.bridge.datasource.category.port.incoming.GetMatchCategoryValuesUseCase;
import com.silenteight.payments.bridge.datasource.category.port.outgoing.CategoryDataAccess;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
class GetMatchCategoryValuesService implements GetMatchCategoryValuesUseCase {

  private final CategoryDataAccess categoryDataAccess;

  @Override
  public BatchGetMatchCategoryValuesResponse batchGetMatchCategoryValues(
      List<String> matchValuesList) {

    var categoryTupleList = getCategoryTupleList(matchValuesList);
    var categoryValues =
        categoryDataAccess.batchGetMatchCategoryValues(categoryTupleList);

    return BatchGetMatchCategoryValuesResponse.newBuilder()
        .addAllCategoryValues(categoryValues)
        .build();
  }

  private List<MatchCategoryRequest> getCategoryTupleList(List<String> matchValuesList) {

    List<MatchCategoryRequest> matchCategoryRequestList = new ArrayList<>();
    for (String matchValue : matchValuesList) {

      ResourceName resourceName = ResourceName.create(matchValue);
      var match = resourceName.get("matches");
      var category = resourceName.get("categories");

      matchCategoryRequestList.add(new MatchCategoryRequest(match, category));
    }
    return matchCategoryRequestList;
  }
}
