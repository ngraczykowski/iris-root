package com.silenteight.hsbc.bridge.bulk;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.silenteight.hsbc.bridge.alert.AlertStatus;

import org.hibernate.annotations.Immutable;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.*;

import static java.util.Objects.isNull;
import static lombok.AccessLevel.NONE;

@Entity
@Immutable
@Table(name = "hsbc_bridge_alert")
@NoArgsConstructor(access = AccessLevel.NONE)
@Getter
class BulkAlertEntity {

  @Id
  private Long id;

  private String externalId;

  private String errorMessage;

  private String name;

  @Enumerated(EnumType.STRING)
  private AlertStatus status = AlertStatus.STORED;

  @Setter(NONE)
  @OneToMany
  @JoinColumn(name = "alert_id")
  private Collection<BulkAlertMatchEntity> matches = new ArrayList<>();

  @Transient
  boolean isValid() {
    return isNull(errorMessage) && status != AlertStatus.ERROR;
  }
}
