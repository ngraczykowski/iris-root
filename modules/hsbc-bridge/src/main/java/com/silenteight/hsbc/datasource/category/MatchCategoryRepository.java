package com.silenteight.hsbc.datasource.category;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.stream.Stream;

@org.springframework.stereotype.Repository
interface MatchCategoryRepository extends Repository<MatchCategoryEntity, Long> {

  Stream<MatchCategoryEntity> findByNameIn(List<String> matchValues);

  void save(MatchCategoryEntity matchCategory);
}
