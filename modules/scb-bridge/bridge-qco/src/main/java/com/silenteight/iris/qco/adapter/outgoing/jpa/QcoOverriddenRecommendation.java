/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.adapter.outgoing.jpa;

import lombok.*;

import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Entity
@Table(name = "qco_overridden_recommendation")
public class QcoOverriddenRecommendation {

  @Id
  @Column(insertable = false, updatable = false, nullable = false)
  @GeneratedValue(strategy = IDENTITY)
  private Long id;
  @NonNull
  private String batchId;
  @NonNull
  private String alertId;
  @NonNull
  private String alertName;
  @NonNull
  private String policyId;
  @NonNull
  private String matchName;
  @NonNull
  private String stepId;
  private String comment;
  @NonNull
  private String sourceSolution;
  @NonNull
  private String targetSolution;
  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  @Builder.Default
  private OffsetDateTime createdAt = OffsetDateTime.now();
}
