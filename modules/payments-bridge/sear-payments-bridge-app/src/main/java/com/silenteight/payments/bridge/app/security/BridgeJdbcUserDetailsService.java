package com.silenteight.payments.bridge.app.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class BridgeJdbcUserDetailsService implements UserDetailsService {

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // TODO(ahaczewski): Implement BridgeJdbcUserDetailsService#loadUserByUsername().
    return null;
  }
}
