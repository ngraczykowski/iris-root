package com.silenteight.connector.ftcc.request.domain;

import lombok.*;

import com.silenteight.connector.ftcc.common.resource.BatchResource;
import com.silenteight.connector.ftcc.common.resource.MessageResource;
import com.silenteight.connector.ftcc.request.get.dto.MessageDto;
import com.silenteight.sep.base.common.entity.BaseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

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
class MessageEntity extends BaseEntity {

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

  MessageDto toDto() {
    return MessageDto.builder()
        .batchName(BatchResource.toResourceName(getBatchId()))
        .messageName(MessageResource.toResourceName(getMessageId()))
        .payload(getPayload().toString())
        .build();
  }
}
