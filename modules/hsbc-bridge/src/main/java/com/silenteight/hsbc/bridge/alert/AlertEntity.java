package com.silenteight.hsbc.bridge.alert;

import lombok.*;

import com.silenteight.hsbc.bridge.common.entity.BaseEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

  @Setter
  private String externalId;

  @Setter
  private String name;

  @Setter
  private String discriminator;

  private String errorMessage;

  @Column(name = "bulk_id")
  private String bulkId;

  @Setter
  @Enumerated(EnumType.STRING)
  private AlertStatus status = AlertStatus.STORED;

  @Setter(NONE)
  @OneToMany
  @JoinColumn(name = "alert_id")
  private Collection<AlertMatchEntity> matches = new ArrayList<>();

  @Setter
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "alert_payload_id")
  private AlertDataPayloadEntity payload;

  @ElementCollection
  @CollectionTable(name = "hsbc_bridge_alert_metadata", joinColumns = @JoinColumn(name = "id"))
  private List<AlertMetadata> metadata = new ArrayList<>();

  public AlertEntity(String bulkId) {
    this.bulkId = bulkId;
  }

  @Transient
  void error(String errorMessage) {
    this.errorMessage = errorMessage;
    this.status = AlertStatus.ERROR;
  }

  @Transient
  byte[] getPayloadAsBytes() {
    return payload.getPayload();
  }
}
