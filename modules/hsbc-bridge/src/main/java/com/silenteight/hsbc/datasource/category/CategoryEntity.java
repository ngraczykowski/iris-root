package com.silenteight.hsbc.datasource.category;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.silenteight.hsbc.bridge.common.entity.BaseEntity;

import java.util.List;
import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Data
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Entity
@Table(name = "hsbc_bridge_category")
class CategoryEntity extends BaseEntity {

  @Id
  @Column(name = "id", nullable = false, insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String displayName;
  private boolean multiValue;
  @Enumerated(EnumType.STRING)
  private CategoryType type;

  @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
  @CollectionTable(
      name = "hsbc_bridge_category_allowed_values",
      joinColumns = @JoinColumn(name = "category_id"))
  @Column(name = "value")
  private List<String> allowedValues;

  CategoryEntity(CategoryModel categoryModel) {
    this.name = categoryModel.getName();
    this.displayName = categoryModel.getDisplayName();
    this.multiValue = categoryModel.isMultiValue();
    this.type = categoryModel.getType();
    this.allowedValues = categoryModel.getAllowedValues();
  }
}
