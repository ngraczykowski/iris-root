package com.silenteight.payments.bridge.datasource.category.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;

import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
class SelectCategoryQuery {

  BatchGetMatchCategoryValuesResponse execute(List<String> matchValuesList) {
    return BatchGetMatchCategoryValuesResponse.getDefaultInstance();
  }
}
