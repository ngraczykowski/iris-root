/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data.jdbc;

import com.silenteight.adjudication.engine.solving.data.CategoryAggregate;

import java.io.Serializable;

record MatchCategoryDao(Long matchId,String category,String categoryValue) implements Serializable {

  public CategoryAggregate toCategoryAggeregate(){
    return new CategoryAggregate(this.category,this.categoryValue);
  }
}
