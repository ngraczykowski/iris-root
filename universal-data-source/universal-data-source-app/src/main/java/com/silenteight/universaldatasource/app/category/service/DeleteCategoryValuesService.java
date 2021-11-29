package com.silenteight.universaldatasource.app.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.universaldatasource.app.category.port.incoming.DeleteCategoryValuesUseCase;
import com.silenteight.universaldatasource.app.category.port.outgoing.CategoryValueDataAccess;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
class DeleteCategoryValuesService implements DeleteCategoryValuesUseCase {

  private final CategoryValueDataAccess categoryValueDataAccess;

  @Timed(value = "uds.category.use_cases", extraTags = { "action", "deleteCategoryValues" })
  @Override
  public void delete(List<String> alerts) {

    log.info("Deleting category values: alertCount={}, alerts={}", alerts.size(), alerts);

    int deletedCount = categoryValueDataAccess.delete(alerts);

    log.info("Category values removed, count={}", deletedCount);
  }
}
