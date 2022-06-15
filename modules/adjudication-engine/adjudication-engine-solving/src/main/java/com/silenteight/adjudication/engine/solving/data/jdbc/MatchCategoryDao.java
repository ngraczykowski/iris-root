/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data.jdbc;

import com.silenteight.adjudication.engine.solving.data.CategoryAggregate;

record MatchCategoryDao(Long matchId,String category,String categoryValue) {

  public CategoryAggregate toCategoryAggeregate(){
    return new CategoryAggregate(this.category,this.categoryValue);
  }
}
