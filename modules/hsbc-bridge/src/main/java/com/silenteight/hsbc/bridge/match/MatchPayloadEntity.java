package com.silenteight.hsbc.bridge.match;

import lombok.*;

import com.silenteight.hsbc.bridge.common.entity.BaseEntity;

import javax.persistence.*;

import static java.util.Objects.isNull;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hsbc_bridge_match_payload")
class MatchPayloadEntity  extends BaseEntity {

  @Id
  @Column(name = "id", nullable = false, insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private byte[] payload;

  MatchPayloadEntity(byte[] payload) {
    this.payload = payload;
  }

  @Transient
  boolean isArchived() {
    return isNull(payload);
  }
}
