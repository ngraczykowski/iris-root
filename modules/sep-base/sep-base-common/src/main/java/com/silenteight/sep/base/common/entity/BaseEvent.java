package com.silenteight.sep.base.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
public abstract class BaseEvent implements Serializable {

  private static final long serialVersionUID = 726084917522462382L;

  private OffsetDateTime date;

  public BaseEvent() {
    this.date = OffsetDateTime.now();
  }
}
