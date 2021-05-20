package com.silenteight.hsbc.bridge.alert;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hsbc_bridge_match")
@NoArgsConstructor(access = AccessLevel.NONE)
@Getter
class AlertMatchEntity {

  @Id
  private Long id;

  private String name;

  private String externalId;
}
