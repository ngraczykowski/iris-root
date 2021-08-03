package com.silenteight.payments.bridge.datasource.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;

import com.google.protobuf.ProtocolStringList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class GetMatchCategoryValuesUseCase {

  private final CategoryDataAccess categoryDataAccess;

  BatchGetMatchCategoryValuesResponse batchGetMatchCategoryValues(
      List<String> matchValuesList) {

    return categoryDataAccess.batchGetMatchCategoryValues(matchValuesList);
  }
}
