package com.silenteight.hsbc.bridge.bulk;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.silenteight.hsbc.bridge.alert.AlertStatus;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Entity
@Immutable
@Table(name = "hsbc_bridge_alert")
@NoArgsConstructor(access = AccessLevel.NONE)
@Getter
public class BulkAlertEntity {

  @Id
  private Long id;

  private String externalId;

  @Enumerated(EnumType.STRING)
  private AlertStatus status = AlertStatus.STORED;
}
