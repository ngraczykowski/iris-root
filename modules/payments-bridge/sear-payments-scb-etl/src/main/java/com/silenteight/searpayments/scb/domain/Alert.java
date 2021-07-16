package com.silenteight.searpayments.scb.domain;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Setter(AccessLevel.NONE)
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "scb_alert_big")
@Builder
public class Alert {

  @Id
  @Column(name = "alertId", insertable = false, updatable = false, nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.PROTECTED)
  private Long id;

  @NonNull
  private String systemId;

  @NonNull
  private String messageId;

  @NonNull
  private String messageType;

  @NonNull
  private String applicationCode;

  @NonNull
  @Enumerated(EnumType.STRING)
  private AlertMessageFormat messageFormat;

  @NonNull
  private String messageData;

  @NonNull
  private String businessUnitId;

  @NonNull
  private String unit;

  @Setter(AccessLevel.PUBLIC)
  private String currentStatusName;

  @Setter(AccessLevel.PUBLIC)
  private String currentStatusId;

  @Setter(AccessLevel.PUBLIC)
  private String currentStatusRoutingCode;

  @Setter(AccessLevel.PUBLIC)
  private String currentStatusChecksum;

  @Setter(AccessLevel.PUBLIC)
  private String outputStatusName;

  @NonNull
  @Enumerated(EnumType.STRING)
  private AlertStatus status;

  @NonNull
  private String dataCenter;

  @Builder.Default
  @NonNull
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @Setter(AccessLevel.PUBLIC)
  private OffsetDateTime recommendationSentAt;

  @Builder.Default
  @OneToMany(mappedBy = "alert", cascade = ALL, orphanRemoval = true, fetch = EAGER)
  @Fetch(FetchMode.SUBSELECT)
  @ToString.Exclude
  private Collection<Hit> hits = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "alert", cascade = ALL, orphanRemoval = true, fetch = EAGER)
  @Fetch(FetchMode.SUBSELECT)
  @ToString.Exclude
  private Collection<NextStatus> nextStatuses = new ArrayList<>();

  @NonNull
  private String transactionReference;
  @NonNull
  private String direction;

  @Setter(AccessLevel.PUBLIC)
  private Integer hitCount;

  @Setter(AccessLevel.PUBLIC)
  private String gitCommitId;

  private DamageReason damageReason;

  @Setter(AccessLevel.PUBLIC)
  private String countryCode;

  public void addHit(Hit hit) {
    hit.setAlert(this);
    this.hits.add(hit);
    hitCount = this.hits.size();
  }

  public void clearHits() {
    this.hits.clear();
  }

  public void addNextStatus(NextStatus nextStatus) {
    nextStatus.setAlert(this);
    this.nextStatuses.add(nextStatus);
  }

  public void markAsProcessing() {
    this.status = AlertStatus.STATE_PROCESSING;
  }

  public void markAsProcessed() {
    this.status = AlertStatus.STATE_PROCESSED;
  }

  public void markAsDamaged(@NonNull DamageReason reason) {
    this.status = AlertStatus.STATE_DAMAGED;
    this.damageReason = reason;
  }

  public void markAsObsolete() {
    this.status = AlertStatus.STATE_OBSOLETE;
  }

  public void markAsRecommended() {
    this.status = AlertStatus.STATE_RECOMMENDED;
  }

  public void markAsSent() {
    this.status = AlertStatus.STATE_RECOMMENDATION_SENT;
  }

  public void markNonDamagedAsRecommended() {
    if (this.status != AlertStatus.STATE_DAMAGED)
      this.status = AlertStatus.STATE_RECOMMENDED;
  }

  public void markNonDamagedAsSent() {
    if (this.status != AlertStatus.STATE_DAMAGED)
      this.status = AlertStatus.STATE_RECOMMENDATION_SENT;
  }

  public enum AlertStatus {
    STATE_STORED, STATE_PROCESSING, STATE_PROCESSED, STATE_DAMAGED, STATE_OBSOLETE,
    STATE_RECOMMENDED, STATE_RECOMMENDATION_SENT
  }

  public enum AlertMessageFormat {
    SWIFT, OTHER;

    public static AlertMessageFormat of(String code) {
      if ("SWIFT".equalsIgnoreCase(code))
        return SWIFT;
      return OTHER;
    }
  }

  @Embeddable
  @Data
  @NoArgsConstructor
  public static class DamageReason {

    public static final int MAX_DESCRIPTION_LENGTH = 256;

    @NonNull
    @Column(name = "damage_reason_code")
    String code;

    @Column(name = "damage_reason_description")
    String description;

    public DamageReason(@NonNull String code, String description) {
      this.code = code;
      this.description = description != null ?
                         description.substring(
                             0, Math.min(description.length(), MAX_DESCRIPTION_LENGTH)) : null;
    }
  }
}
