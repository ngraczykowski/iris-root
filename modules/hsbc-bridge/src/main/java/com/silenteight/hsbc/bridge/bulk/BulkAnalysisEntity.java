package com.silenteight.hsbc.bridge.bulk;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Immutable
@Subselect("SELECT a.id, a.name, a.policy "
    + "FROM hsbc_bridge_analysis a, hsbc_bridge_bulk b "
    + "WHERE b.analysis_id = a.id")
@NoArgsConstructor(access = AccessLevel.NONE)
@Getter
class BulkAnalysisEntity {

  @Id
  private Long id;
  private String name;
  private String policy;
}
