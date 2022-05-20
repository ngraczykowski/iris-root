package com.silenteight.warehouse.statistics.get;

import lombok.*;

import org.hibernate.annotations.SQLInsert;

import java.time.LocalDate;
import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@Table(
    name = "warehouse_daily_recommendation_statistics",
    uniqueConstraints = @UniqueConstraint(columnNames = { "id", "day" })
)
// XXX: be careful the order in @SQLInsert is different from the
// {@code DailyRecommendationStatistics} and
// it's hard to figure out what order is used by jpa. Pay attention to keep in sync order of the
// column names with the column values in this insert query.
@SQLInsert(sql = "INSERT INTO warehouse_daily_recommendation_statistics("
    + "alerts_count, "
    + "analyst_decision_count, "
    + "day, "
    + "effectiveness_percent, "
    + "efficiency_percent, "
    + "false_positives_count, "
    + "manual_investigations_count, "
    + "potential_true_positives_count) "
    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) "
    + "ON CONFLICT(day) DO UPDATE SET "
    + "alerts_count = excluded.alerts_count, "
    + "false_positives_count = excluded.false_positives_count, "
    + "potential_true_positives_count = excluded.potential_true_positives_count, "
    + "analyst_decision_count = excluded.analyst_decision_count, "
    + "manual_investigations_count = excluded.manual_investigations_count, "
    + "effectiveness_percent = excluded.effectiveness_percent, "
    + "efficiency_percent = excluded.efficiency_percent"
)
public class DailyRecommendationStatistics {

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
  private int analystDecisionCount;

  @Column
  private Double efficiencyPercent;

  @Column
  private Double effectivenessPercent;

}
