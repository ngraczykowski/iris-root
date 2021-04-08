package com.silenteight.hsbc.bridge.bulk;

import lombok.*;

import com.silenteight.hsbc.bridge.common.entity.BaseEntity;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.*;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PRIVATE;

@Data
@Setter(NONE)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Entity
@NoArgsConstructor(access = PRIVATE)
@Table(name = "hsbc_bridge_bulk")
public class Bulk extends BaseEntity {

  @Id
  private String id;

  @Setter
  @Enumerated(value = EnumType.STRING)
  private BulkStatus status = BulkStatus.STORED;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @JoinColumn(name = "bulk_id")
  private Collection<BulkItem> items = new ArrayList<>();

  Bulk(String id) {
    this.id = id;
  }

  public void addItem(BulkItem bulkItem) {
    bulkItem.setBulkId(this.getId());
    items.add(bulkItem);
  }
}
