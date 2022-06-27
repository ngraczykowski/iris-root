package com.silenteight.simulator.processing.alert.index.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseModifiableEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.simulator.processing.alert.index.dto.IndexedAlertDto;

import java.io.Serializable;
import javax.persistence.*;

import static com.silenteight.simulator.processing.alert.index.domain.State.ACKED;

@Entity
@Data
@Setter(AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class IndexedAlertEntity extends BaseModifiableEntity implements IdentifiableEntity, Serializable {

  private static final long serialVersionUID = -8832059739436178153L;

  @Id
  @Setter(AccessLevel.PUBLIC)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  @Column(updatable = false)
  private Long id;

  @ToString.Include
  @Column(name = "request_id", nullable = false)
  private String requestId;

  @ToString.Include
  @Column(name = "analysis_name", nullable = false)
  private String analysisName;

  @ToString.Include
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private State state;

  @ToString.Include
  @Column(name = "alert_count", nullable = false)
  private long alertCount;

  void ack() {
    this.state = ACKED;
  }

  IndexedAlertDto toDto() {
    return IndexedAlertDto.builder()
        .analysisName(getAnalysisName())
        .alertCount(getAlertCount())
        .state(getState())
        .requestId(getRequestId())
        .build();
  }
}
