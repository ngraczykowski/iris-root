package com.silenteight.sens.webapp.user;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.domain.user.User;
import com.silenteight.sens.webapp.domain.user.UserToken;
import com.silenteight.sens.webapp.user.exception.UserNotFoundException;
import com.silenteight.sens.webapp.user.exception.UserTokenAlreadyExistsException;
import com.silenteight.sens.webapp.user.exception.UserTokenNotFoundException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
public class UserTokenService {

  private static final int TOKEN_LENGTH = 32;

  @NonNull
  private final UserService userService;

  @NonNull
  private final UserTokenRepository userTokenRepository;

  @NonNull
  private final TokenEncoder tokenEncoder;

  @Transactional
  public String generate(String userName, String alias) {
    User user = getUser(userName);
    validateTokenExistance(userName, alias);

    String token = generateToken();
    UserToken userToken = new UserToken(user, getHashedToken(token), alias);
    userTokenRepository.save(userToken);
    return token;
  }

  private User getUser(String userName) {
    return userService
        .getUserByName(userName)
        .orElseThrow(() -> new UserNotFoundException(userName));
  }

  private void validateTokenExistance(String userName, String alias) {
    userTokenRepository
        .find(userName, alias)
        .ifPresent(ur -> {
          throw new UserTokenAlreadyExistsException(userName, alias);
        });
  }

  private static String generateToken() {
    return RandomStringUtils.random(TOKEN_LENGTH, true, true);
  }

  private String getHashedToken(String token) {
    return tokenEncoder.encode(token);
  }

  @Transactional
  public void delete(String userName, String alias) {
    UserToken userToken = userTokenRepository
        .find(userName, alias)
        .orElseThrow(() -> new UserTokenNotFoundException(userName, alias));

    userTokenRepository.delete(userToken);
  }

  public Optional<UserToken> find(String inputToken) {
    String hashedToken = tokenEncoder.encode(inputToken);
    return userTokenRepository.findByHashedToken(hashedToken);
  }
}
