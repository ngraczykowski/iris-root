package com.silenteight.hsbc.bridge.bulk;

import lombok.*;

import com.silenteight.hsbc.bridge.common.entity.BaseEntity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.*;

import static com.silenteight.hsbc.bridge.bulk.BulkStatus.DELIVERED;
import static com.silenteight.hsbc.bridge.bulk.BulkStatus.ERROR;
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
  private Long analysisId;
  private String errorMessage;
  private OffsetDateTime errorTimestamp;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "bulk_payload_id")
  private BulkPayloadEntity payload;

  @Setter(NONE)
  @OneToMany
  @JoinColumn(name = "bulk_id")
  private Collection<BulkAlertEntity> alerts = new ArrayList<>();

  Bulk(String id) {
    this.id = id;
  }

  @Transient
  boolean hasAnalysisId() {
    return nonNull(analysisId);
  }

  @Transient
  void delivered() {
    this.status = DELIVERED;
  }

  @Transient
  void error(String errorMessage) {
    this.status = ERROR;
    this.errorMessage = errorMessage;
    this.errorTimestamp = OffsetDateTime.now();
  }
}
