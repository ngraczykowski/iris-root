package com.silenteight.hsbc.datasource.category;

import lombok.*;

import java.util.List;
import javax.persistence.*;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PROTECTED;

@Data
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
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
  public String getCategoryName() {
    return category.getName();
  }

  MatchCategoryEntity(long matchId, CategoryEntity category, List<String> values) {
    this.matchId = matchId;
    this.category = category;
    this.values = values;
  }
}
