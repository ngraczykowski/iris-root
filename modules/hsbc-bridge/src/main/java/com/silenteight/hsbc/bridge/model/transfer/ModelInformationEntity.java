package com.silenteight.hsbc.bridge.model.transfer;

import lombok.*;

import com.silenteight.hsbc.bridge.common.entity.BaseEntity;

import javax.persistence.*;

import static lombok.AccessLevel.NONE;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = NONE)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Getter
@Entity
@Table(name = "hsbc_bridge_model")
public class ModelInformationEntity extends BaseEntity {

  @Id
  @Column(name = "id", nullable = false, insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Column(name = "minio_url")
  private String minIoUrl;

  @Enumerated(EnumType.STRING)
  private ModelType type;
}
