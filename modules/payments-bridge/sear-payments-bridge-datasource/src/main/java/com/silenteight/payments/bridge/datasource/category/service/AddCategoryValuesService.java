package com.silenteight.payments.bridge.datasource.category.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.datasource.category.model.MatchCategoryValue;
import com.silenteight.payments.bridge.datasource.category.port.incoming.AddCategoryValuesUseCase;
import com.silenteight.payments.bridge.datasource.category.port.outgoing.CategoryDataAccess;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddCategoryValuesService implements AddCategoryValuesUseCase {

  private final CategoryDataAccess categoryDataAccess;

  @Override
  public void addCategoryValues(
      List<MatchCategoryValue> categoryValues) {
    categoryDataAccess.saveAll(categoryValues);
  }
}
