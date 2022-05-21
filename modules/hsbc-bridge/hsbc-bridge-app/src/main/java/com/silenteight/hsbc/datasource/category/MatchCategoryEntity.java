package com.silenteight.hsbc.datasource.category;

import lombok.*;

import java.util.List;
import javax.persistence.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.NONE)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Entity
@Table(name = "hsbc_bridge_match_category")
class MatchCategoryEntity {

  @Id
  @Column(name = "id", nullable = false, insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private long matchId;

  @Setter
  private String name;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private CategoryEntity category;

  @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
  @CollectionTable(
      name = "hsbc_bridge_match_category_values",
      joinColumns = @JoinColumn(name = "match_category_id"))
  @Column(name = "value")
  private List<String> values;

  @Transient
  public boolean getMultiValue() {
    return category.isMultiValue();
  }

  MatchCategoryEntity(long matchId, String name, CategoryEntity category, List<String> values) {
    this.name = name;
    this.matchId = matchId;
    this.category = category;
    this.values = values;
  }
}
