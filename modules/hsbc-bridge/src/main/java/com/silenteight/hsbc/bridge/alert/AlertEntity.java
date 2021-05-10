package com.silenteight.hsbc.bridge.alert;

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
@Table(name = "hsbc_bridge_alert")
public class AlertEntity extends BaseEntity {

  @Id
  @Column(name = "id", nullable = false, insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String externalId;

  @Setter
  private String name;

  @Column(name = "bulk_id")
  private String bulkId;

  @Setter
  @Enumerated(EnumType.STRING)
  private AlertStatus status = AlertStatus.STORED;

  public AlertEntity(String externalId, String bulkId) {
    this.externalId = externalId;
    this.bulkId = bulkId;
  }
}
