package com.silenteight.warehouse.statistics.model;

import lombok.*;

import java.time.LocalDate;
import javax.persistence.*;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@Builder(access = PACKAGE)
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Table(name = "warehouse_daily_policy_statistics")
final class DailyPolicyStatistics {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private LocalDate day;

  @Column
  private int alertsCount;

  @Column
  private int falsePositivesCount;

  @Column
  private int potentialTruePositivesCount;

  @Column
  private int manualInvestigationsCount;

  @Column
  private double efficiencyPercent;

  @Column
  private double effectivenessPercent;

}
