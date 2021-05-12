package com.silenteight.hsbc.datasource.category;

import lombok.*;

import org.hibernate.annotations.Immutable;

import java.util.List;
import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Data
@NoArgsConstructor(access = PROTECTED)
@Immutable
@Entity
@Table(name = "hsbc_bridge_match_category_view")
class MatchCategoryView {

  @Id
  private Long id;

  String name;

  boolean multiValue;

  @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
  @CollectionTable(
      name = "hsbc_bridge_match_category_values",
      joinColumns = @JoinColumn(name = "match_category_id"))
  @Column(name = "value")
  private List<String> values;
}
