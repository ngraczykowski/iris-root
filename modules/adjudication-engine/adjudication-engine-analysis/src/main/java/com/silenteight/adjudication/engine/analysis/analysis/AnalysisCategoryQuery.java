package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Immutable
@Entity
@Builder(access = PACKAGE)
class AnalysisCategoryQuery {

  @Id
  @Column(name = "analysis_category_id")
  @Include
  private Long id;

  private Long categoryId;

  // NOTE(ahaczewsk): For the reasoning why the column is called "category" and the field "name",
  //  see comment in the `com.silenteight.adjudication.engine.solve.category.Category` class.
  @Column(name = "category")
  private String name;
}
