package com.silenteight.hsbc.bridge.bulk;

import lombok.*;

import com.silenteight.hsbc.bridge.alert.Alert;
import com.silenteight.hsbc.bridge.bulk.BulkStatus;
import com.silenteight.hsbc.bridge.common.entity.BaseEntity;

import java.util.UUID;
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

  @Enumerated(EnumType.STRING)
  private BulkStatus status = BulkStatus.STORED;

  @Setter
  @Column(name = "bulk_id")
  UUID bulkId;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  private Alert alert;

  public BulkItem(Alert alert) {
    this.alert = alert;
  }

  @Transient
  public int getAlertCaseId() {
    return this.alert.getCaseId();
  }
}
