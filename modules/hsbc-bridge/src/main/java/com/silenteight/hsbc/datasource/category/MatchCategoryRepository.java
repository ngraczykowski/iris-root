package com.silenteight.hsbc.datasource.category;

import org.springframework.data.repository.Repository;

import java.util.Collection;

@org.springframework.stereotype.Repository
interface MatchCategoryRepository extends Repository<MatchCategoryEntity, Long> {

  Collection<MatchCategoryEntity> findByMatchValueIn(Collection<String> matchValues);
}
