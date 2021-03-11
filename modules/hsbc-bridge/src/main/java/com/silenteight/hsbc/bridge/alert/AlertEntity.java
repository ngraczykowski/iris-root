package com.silenteight.hsbc.bridge.alert;

import lombok.*;

import com.silenteight.hsbc.bridge.common.entity.BaseEntity;
import com.silenteight.hsbc.bridge.match.MatchEntity;

import java.util.List;
import javax.persistence.*;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PROTECTED;

@Data
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Entity
@Table(name = "hsbc_bridge_alert")
public class AlertEntity extends BaseEntity {

  @Id
  @Column(name = "id", nullable = false, insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private int caseId;
  private byte[] payload;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "alert_id")
  private List<MatchEntity> matches;

  public AlertEntity(int caseId) {
    this.caseId = caseId;
  }
}
