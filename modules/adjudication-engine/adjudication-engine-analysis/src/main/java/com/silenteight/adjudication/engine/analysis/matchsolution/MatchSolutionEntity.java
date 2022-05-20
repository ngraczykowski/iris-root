package com.silenteight.adjudication.engine.analysis.matchsolution;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "MatchSolution")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Builder(access = PUBLIC)
class MatchSolutionEntity extends BaseEntity implements IdentifiableEntity {

  @Id
  @Column(name = "match_solution_id", insertable = false, updatable = false, nullable = false)
  @GeneratedValue(strategy = IDENTITY)
  @Setter(PUBLIC)
  @Include
  private Long id;

  @Column(nullable = false, updatable = false)
  @NonNull
  private Long analysisId;

  @Column(nullable = false, updatable = false)
  @NonNull
  private Long matchId;

  @Column(nullable = false)
  @NonNull
  private String solution;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private ObjectNode reason;
}
