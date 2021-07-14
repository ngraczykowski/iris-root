package com.silenteight.searpayments.scb.domain;

import lombok.*;

import javax.persistence.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Setter(AccessLevel.NONE)
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "nextStatus")
public class NextStatus {

  @Id
  @Column(name = "nextStatusId", insertable = false, updatable = false, nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.PROTECTED)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "alertId", nullable = false)
  @Setter(AccessLevel.PUBLIC)
  private Alert alert;

  @NonNull
  private String idOfNextStatus;

  @NonNull
  private String name;

  @NonNull
  private String routingCode;

  @NonNull
  private String checksum;
}
