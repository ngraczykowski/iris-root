package com.silenteight.hsbc.bridge.bulk;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.silenteight.hsbc.bridge.alert.AlertStatus;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "hsbc_bridge_alert")
@NoArgsConstructor(access = AccessLevel.NONE)
@Getter
class BulkAlertEntity {

  @Id
  private Long id;

  private String externalId;

  private String errorMessage;

  private String name;

  private OffsetDateTime alertTime;

  private String discriminator;

  @Enumerated(EnumType.STRING)
  private AlertStatus status = AlertStatus.STORED;

  @Setter(AccessLevel.NONE)
  @OneToMany
  @JoinColumn(name = "alert_id")
  private Collection<BulkAlertMatchEntity> matches = new ArrayList<>();

  @Setter(AccessLevel.NONE)
  @ElementCollection
  @CollectionTable(name = "hsbc_bridge_alert_metadata", joinColumns = @JoinColumn(name = "id"))
  private List<BulkAlertMetadata> metadata = new ArrayList<>();

  @Transient
  boolean isValid() {
    return Objects.isNull(errorMessage) && status != AlertStatus.ERROR;
  }

  @Transient
  boolean isCompleted() {
    return status == AlertStatus.COMPLETED;
  }
}
