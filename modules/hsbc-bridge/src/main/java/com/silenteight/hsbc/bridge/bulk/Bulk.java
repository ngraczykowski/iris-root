package com.silenteight.hsbc.bridge.bulk;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import com.silenteight.hsbc.bridge.common.entity.BaseEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import javax.persistence.*;

import static lombok.AccessLevel.NONE;

@Data
@Setter(NONE)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Entity
@Table(name = "hsbc_bridge_bulk")
public class Bulk extends BaseEntity {

  @Id
  private UUID id;

  @Enumerated(value = EnumType.STRING)
  private BulkStatus status = BulkStatus.STORED;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "bulk_id")
  private Collection<BulkItem> items = new ArrayList<>();

  Bulk() {
    this.id = UUID.randomUUID();
  }

  public void addItem(BulkItem bulkItem) {
    bulkItem.setBulkId(this.getId());
    items.add(bulkItem);
  }
}
