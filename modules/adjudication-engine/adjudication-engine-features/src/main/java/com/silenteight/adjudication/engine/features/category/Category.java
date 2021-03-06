package com.silenteight.adjudication.engine.features.category;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import com.silenteight.adjudication.engine.features.category.dto.CategoryDto;
import com.silenteight.sep.base.common.entity.BaseEntity;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;
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
class Category extends BaseEntity {

  @Id
  @Column(name = "category_id", insertable = false, updatable = false, nullable = false)
  @GeneratedValue(strategy = IDENTITY)
  @Setter(PACKAGE)
  @Include
  private Long id;

  // NOTE(ahaczewski): Sonar does not like a field named "category" in class "Category", PostgreSQL
  //  doesn't like column named "name". So: the column is named "category", the field "name".
  @Column(name = "category", updatable = false, nullable = false)
  @NonNull
  private String name;

  CategoryDto toDto() {
    return new CategoryDto(getId(), getName());
  }
}
