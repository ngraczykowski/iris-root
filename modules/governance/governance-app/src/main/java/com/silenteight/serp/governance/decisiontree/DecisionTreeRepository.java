package com.silenteight.serp.governance.decisiontree;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Optional;

interface DecisionTreeRepository extends Repository<DecisionTree, Long> {

  DecisionTree save(DecisionTree tree);

  Optional<DecisionTree> findByName(String treeName);

  // TOOD(bgulowaty): used only to tell if repo is empty.
  Collection<DecisionTree> findAll();

  @Modifying
  @Query("UPDATE DecisionTree SET defaultForNewGroups = false")
  int setAllDefaultForNewGroupsToFalse();

  @Query("SELECT new com.silenteight.serp.governance.decisiontree.DecisionTreeId(id)"
      + " FROM DecisionTree WHERE defaultForNewGroups = true")
  Optional<DecisionTreeId> findByDefaultForNewGroups();

  boolean existsById(long id);
}
