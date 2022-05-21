package com.silenteight.hsbc.bridge.match;

import lombok.*;

import com.silenteight.hsbc.bridge.common.entity.BaseEntity;

import javax.persistence.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.NONE)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Entity
@Table(name = "hsbc_bridge_match")
class MatchEntity extends BaseEntity {

  @Id
  @Column(name = "id", nullable = false, insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String externalId;

  @Column(name = "alert_id")
  private long alertId;

  @Setter
  private String name;

  @Setter
  private String matchName;

  @Setter
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "match_payload_id")
  private MatchPayloadEntity payload;

  MatchEntity(String externalId, long alertId) {
    this.externalId = externalId;
    this.alertId = alertId;
  }
}
