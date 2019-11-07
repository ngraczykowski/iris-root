package com.silenteight.sens.webapp.domain.user;

import lombok.*;

import com.silenteight.sens.webapp.common.entity.BaseModifiableEntity;
import com.silenteight.sens.webapp.kernel.security.authority.Role;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Size;

import static com.silenteight.sens.webapp.domain.user.RoleToAuthoritiesMapper.mapRolesToAuthorities;
import static java.util.stream.Collectors.toList;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Setter(AccessLevel.NONE)
@Entity
@Audited
public class User extends BaseModifiableEntity {

  @Id
  @Column(name = "userId", updatable = false, nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.PROTECTED)
  private Long id;

  @Column(insertable = false)
  @Setter(AccessLevel.PACKAGE)
  @Audited
  private Instant deletedAt;

  @NonNull
  @Size(min = 1, max = 64)
  @Column(length = 64, unique = true, nullable = false)
  private String userName;

  @Column(length = 64)
  @Audited(withModifiedFlag = true)
  private String hashedPassword;

  private boolean superUser;

  private boolean active = true;

  @NonNull
  @Column(nullable = false)
  @Convert(converter = ListOfRolesConverter.class)
  private List<Role> roles = new ArrayList<>();

  @NotAudited
  private Instant lastLoginAt;

  @Size(max = 64)
  @Column(length = 64)
  @Setter(AccessLevel.PUBLIC)
  private String displayName;

  public User(String userName) {
    this(userName, false);
  }

  public User(String userName, boolean isSuperUser) {
    this.userName = userName;
    this.superUser = isSuperUser;
  }

  public List<Role> getActiveRoles() {
    if (isSuperUser())
      return Arrays.stream(Role.values()).collect(toList());
    else
      return getRoles();
  }

  public boolean hasOnlyRole(Role role) {
    return this.roles.size() == 1 && hasRole(role);
  }

  public boolean hasRole(Role role) {
    return this.roles.contains(role);
  }

  public Set<GrantedAuthority> getAuthorities() {
    return mapRolesToAuthorities(getActiveRoles());
  }

  public boolean isExternalUser() {
    return StringUtils.isEmpty(hashedPassword);
  }

  public boolean isLocalUser() {
    return !StringUtils.isEmpty(hashedPassword);
  }

  public boolean isInactive() {
    return !active;
  }

  public void changePassword(String hashedPassword) {
    this.hashedPassword = hashedPassword;
  }

  public void changeRoles(List<Role> roles) {
    this.roles.clear();
    if (roles != null)
      this.roles.addAll(roles);
  }

  public void addRole(@NonNull Role role) {
    this.roles.add(role);
  }

  public void activate() {
    this.active = true;
  }

  public void deactivate() {
    this.active = false;
  }

  public void promoteToSuperUser() {
    superUser = true;
  }

  public void relegateToUser() {
    superUser = false;
  }

  public void logIn() {
    this.lastLoginAt = Instant.now();
  }

  public String getDisplayableName() {
    return StringUtils.isEmpty(getDisplayName()) ? getUserName() : getDisplayName();
  }

  public boolean isDeleted() {
    return deletedAt != null;
  }
}
