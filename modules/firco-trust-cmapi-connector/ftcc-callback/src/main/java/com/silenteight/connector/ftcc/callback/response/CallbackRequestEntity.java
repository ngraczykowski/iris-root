package com.silenteight.connector.ftcc.callback.response;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;

@EqualsAndHashCode(callSuper = false)
@Data
@Setter(AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
class CallbackRequestEntity extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -2827408816130009807L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NonNull
  @Column(name = "batch_id", nullable = false, updatable = false)
  private UUID batchId;

  private String endpoint;
  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private JsonNode payload;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private JsonNode response;

  @Column(name = "code")
  private Integer code;
}
