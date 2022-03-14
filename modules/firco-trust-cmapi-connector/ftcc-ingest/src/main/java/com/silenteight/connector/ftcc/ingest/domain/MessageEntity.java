package com.silenteight.connector.ftcc.ingest.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Setter(AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
class MessageEntity extends BaseEntity implements Serializable {

  @Id
  @Column(name = "message_id", updatable = false, nullable = false)
  @ToString.Include
  @EqualsAndHashCode.Include
  private UUID messageId;

  @NonNull
  @ToString.Include
  @Column(name = "batch_id", nullable = false)
  private UUID batchId;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private JsonNode payload;
}
