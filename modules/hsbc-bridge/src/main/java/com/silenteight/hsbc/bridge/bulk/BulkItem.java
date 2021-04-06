package com.silenteight.hsbc.bridge.bulk;

import lombok.*;

import com.silenteight.hsbc.bridge.common.entity.BaseEntity;

import javax.persistence.*;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PROTECTED;

@Data
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Entity
@Table(name = "hsbc_bridge_bulk_item")
class BulkItem extends BaseEntity {

  @Id
  @Column(name = "id", nullable = false, insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Enumerated(EnumType.STRING)
  private BulkStatus status = BulkStatus.STORED;

  @Setter
  @Column(name = "bulk_id")
  String bulkId;

  private int alertExternalId;
  private byte[] payload;

  public BulkItem(int alertId, byte[] payload) {
    this.alertExternalId = alertId;
    this.payload = payload;
  }
}
