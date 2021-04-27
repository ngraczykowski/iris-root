package com.silenteight.hsbc.bridge.bulk;

import lombok.*;

import com.silenteight.hsbc.bridge.common.entity.BaseEntity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.*;

import static com.silenteight.hsbc.bridge.bulk.BulkStatus.*;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PRIVATE;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Entity
@NoArgsConstructor(access = PRIVATE)
@Table(name = "hsbc_bridge_bulk")
public class Bulk extends BaseEntity {

  @Id
  @Setter(NONE)
  private String id;

  @Enumerated(value = EnumType.STRING)
  private BulkStatus status = BulkStatus.STORED;

  @Setter(NONE)
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @JoinColumn(name = "bulk_id")
  private Collection<BulkItem> items = new ArrayList<>();

  private Long analysisId;

  private String errorMessage;
  private OffsetDateTime errorTimestamp;

  Bulk(String id) {
    this.id = id;
  }

  public void addItem(BulkItem bulkItem) {
    bulkItem.setBulkId(this.getId());
    items.add(bulkItem);
  }

  @Transient
  boolean hasAnalysisId() {
    return nonNull(analysisId);
  }

  @Transient
  boolean hasNonFinalStatus() {
    return status == STORED || status == PROCESSING;
  }

  @Transient
  void markError(String errorMessage) {
    this.status = ERROR;
    this.errorMessage = errorMessage;
    this.errorTimestamp = OffsetDateTime.now();
  }
}
