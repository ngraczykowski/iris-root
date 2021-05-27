package com.silenteight.adjudication.engine.features.matchfeaturevalue;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Builder(access = PACKAGE)
class MatchFeatureValue extends BaseEntity {

  @EmbeddedId
  @NonNull
  private MatchFeatureValueKey id;

  @NonNull
  String value;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  ObjectNode reason;
}
