package com.silenteight.hsbc.bridge.alert;

import lombok.*;

import javax.persistence.*;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PROTECTED;

@Data
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Entity
@Table(name = "hsbc_bridge_raw_alert")
public class RawAlert {

  @Id
  @Column(name = "id", nullable = false, insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private int caseId;
  private byte[] payload;

  public RawAlert(int caseId, byte[] payload) {
    this.caseId = caseId;
    this.payload = payload;
  }
}
