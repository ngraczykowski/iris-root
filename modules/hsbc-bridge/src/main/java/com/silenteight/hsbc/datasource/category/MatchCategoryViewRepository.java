package com.silenteight.hsbc.datasource.category;

import org.springframework.data.repository.Repository;

import java.util.List;

interface MatchCategoryViewRepository extends Repository<MatchCategoryView, Long> {

  List<MatchCategoryView> findByNameIn(List<String> matchValues);
}
