package com.silenteight.hsbc.bridge.match;

import lombok.*;

import com.silenteight.hsbc.bridge.common.entity.BaseEntity;

import java.util.Objects;
import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hsbc_bridge_match_payload")
class MatchPayloadEntity extends BaseEntity {

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
    return Objects.isNull(payload);
  }
}
