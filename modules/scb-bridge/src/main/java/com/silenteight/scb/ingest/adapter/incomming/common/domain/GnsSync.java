package com.silenteight.scb.ingest.adapter.incomming.common.domain;

import lombok.*;

import java.time.LocalDateTime;
import javax.persistence.*;

@Data
@RequiredArgsConstructor
@Setter(AccessLevel.NONE)
@EqualsAndHashCode
@Entity
@Table(name = "ScbGnsSync")
class GnsSync {

  @Id
  @Column(name = "gnsSyncId", insertable = false, updatable = false, nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.PROTECTED)
  private Long id;

  @NonNull
  @Column(nullable = false)
  @Setter(AccessLevel.PROTECTED)
  private LocalDateTime startedAt;

  @Setter
  private LocalDateTime finishedAt;

  private Boolean partiallyLoaded = Boolean.FALSE;

  private String errorMessage;

  @Setter
  private String syncMode;

  GnsSync() {
    startedAt = LocalDateTime.now();
    partiallyLoaded = Boolean.FALSE;
  }

  void finishSync() {
    finishedAt = LocalDateTime.now();
    errorMessage = null;
  }

  void finishSyncWithError(String errorMessage) {
    partiallyLoaded = Boolean.TRUE;
    finishedAt = LocalDateTime.now();
    this.errorMessage = errorMessage;
  }
}
