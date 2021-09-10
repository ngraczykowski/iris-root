package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import com.silenteight.sep.base.common.entity.BaseEntity;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

@Data
@AllArgsConstructor(access = PACKAGE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Builder(access = PACKAGE)
class AlertMessagePayload extends BaseEntity {

  @Id
  @Column(insertable = false, updatable = false, nullable = false)
  @Setter(PUBLIC)
  @Include
  private UUID alertMessageId;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb", updatable = false, nullable = false)
  private ObjectNode originalMessage;
}
