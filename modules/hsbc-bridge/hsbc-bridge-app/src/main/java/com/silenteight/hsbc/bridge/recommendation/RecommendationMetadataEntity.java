package com.silenteight.hsbc.bridge.recommendation;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.common.entity.BaseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Table(name = "hsbc_bridge_recommendation_metadata")
@Slf4j
class RecommendationMetadataEntity extends BaseEntity {

  @Id
  @Column(name = "id", nullable = false, insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private ObjectNode payload;

  public RecommendationMetadataEntity(JsonNode json) {
    if (json.isObject()) {
      this.payload = (ObjectNode) json;
    } else {
      log.error("Invalid json format");
    }
  }
}
