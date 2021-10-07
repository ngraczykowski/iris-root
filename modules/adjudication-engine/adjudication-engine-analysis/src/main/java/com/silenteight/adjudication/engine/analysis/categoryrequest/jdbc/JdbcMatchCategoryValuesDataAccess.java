package com.silenteight.adjudication.engine.analysis.categoryrequest.jdbc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.categoryrequest.MatchCategoryValuesDataAccess;
import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.CategoryMap;
import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.GetCategoryValueResponse;
import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.MissingCategoryResult;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Repository
@Slf4j
class JdbcMatchCategoryValuesDataAccess implements MatchCategoryValuesDataAccess {

  private final SelectMissingMatchCategoryValuesQuery selectMissingMatchCategoryValuesQuery;
  private final CreateMatchCategoryValue createMatchCategoryValue;

  @Override
  @Transactional(readOnly = true)
  public MissingCategoryResult getMissingCategoryValues(long analysisId) {
    return selectMissingMatchCategoryValuesQuery.execute(analysisId);
  }

  @Override
  @Transactional
  public void createMatchCategoryValues(
      @NonNull CategoryMap categoryMap, @NonNull List<GetCategoryValueResponse> categoryValues) {

    createMatchCategoryValue.execute(categoryMap, categoryValues);
  }
}
