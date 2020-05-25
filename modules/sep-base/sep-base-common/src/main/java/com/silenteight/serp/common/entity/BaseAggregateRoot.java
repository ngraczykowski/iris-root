package com.silenteight.serp.common.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import org.hibernate.envers.Audited;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import static java.util.Collections.unmodifiableList;

@EqualsAndHashCode(exclude = { "domainEvents" }, callSuper = true)
@MappedSuperclass
public abstract class BaseAggregateRoot extends BaseModifiableEntity {

  @Getter(AccessLevel.NONE)
  @Version
  @Column(nullable = false)
  @Access(AccessType.FIELD)
  @Audited
  private Integer version;

  @Transient
  private final List<BaseEvent> domainEvents = new ArrayList<>();

  protected final void recordEvent(@NonNull BaseEvent event) {
    domainEvents.add(event);
  }

  @AfterDomainEventPublication
  public void clearEvents() {
    domainEvents.clear();
  }

  @DomainEvents
  public List<BaseEvent> getDomainEvents() {
    return unmodifiableList(domainEvents);
  }
}
