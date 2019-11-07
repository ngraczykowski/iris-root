package com.silenteight.sens.webapp.domain.user;

import lombok.*;

import com.silenteight.sens.webapp.common.entity.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Setter(AccessLevel.NONE)
@Entity
public class UserToken extends BaseEntity {

  @Id
  @Column(name = "userTokenId", updatable = false, nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "userId", updatable = false, nullable = false)
  private User user;

  @NonNull
  @Size(min = 64, max = 64)
  @Column(length = 64, nullable = false)
  private String hashedToken;

  @NonNull
  @Size(min = 1, max = 16)
  @Column(length = 16, nullable = false)
  private String alias;

  public UserToken(User user, String hashedToken, String alias) {
    this.user = user;
    this.hashedToken = hashedToken;
    this.alias = alias;
  }
}
