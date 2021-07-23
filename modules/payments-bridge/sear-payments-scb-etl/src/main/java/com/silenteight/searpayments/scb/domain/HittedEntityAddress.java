package com.silenteight.searpayments.scb.domain;

import lombok.*;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@AllArgsConstructor
@Builder
@Data
@RequiredArgsConstructor
@Setter(AccessLevel.PUBLIC)
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "scb_hitted_entity_address")
public class HittedEntityAddress {

  @Id
  @Column(name = "addressId", insertable = false, updatable = false, nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.PROTECTED)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "hitId", nullable = false)
  private Hit hit;

  private boolean isMain;

  private String postalAddress;

  private String city;

  private String state;

  private String country;

  public boolean hasNonEmptyData() {
    return StringUtils.isNotEmpty(city)
        || StringUtils.isNotEmpty(state)
        || StringUtils.isNotEmpty(country);
  }
}
