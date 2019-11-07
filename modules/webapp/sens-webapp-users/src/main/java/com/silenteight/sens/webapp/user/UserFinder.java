package com.silenteight.sens.webapp.user;

import com.silenteight.sens.webapp.kernel.security.authority.Role;
import com.silenteight.sens.webapp.user.dto.*;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserFinder {

  UserResponseView findUsers(Pageable pageable);

  List<UserView> findAll();

  List<UserAuditDto> findAudited();

  UserDetailedView findUser(long userId);

  List<UserNameView> findUsersWithoutRole(List<Long> userIds, Role role);
}
