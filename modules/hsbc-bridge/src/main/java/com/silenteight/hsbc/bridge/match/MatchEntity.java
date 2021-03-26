package com.silenteight.hsbc.bridge.match;

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
@Table(name = "hsbc_bridge_match")
class MatchEntity extends BaseEntity {

  @Id
  @Column(name = "id", nullable = false, insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private byte[] payload;

  private long alertId;

  private String name;

  MatchEntity(long alertId, String name, byte[] payload) {
    this.alertId = alertId;
    this.name = name;
    this.payload = payload;
  }
}
