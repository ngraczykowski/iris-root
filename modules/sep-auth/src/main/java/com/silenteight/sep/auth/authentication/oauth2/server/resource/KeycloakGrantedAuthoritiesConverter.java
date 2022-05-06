package com.silenteight.sep.auth.authentication.oauth2.server.resource;

import lombok.AllArgsConstructor;

import com.silenteight.sep.auth.authentication.AuthorityNameNormalizer;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@Deprecated
@AllArgsConstructor
public class KeycloakGrantedAuthoritiesConverter
    implements Converter<Jwt, Collection<GrantedAuthority>> {

  private static final String AUTHORITY_CLAIM = "roles";
  private AuthorityNameNormalizer defaultNormalizer;

  @Override
  public Collection<GrantedAuthority> convert(Jwt source) {
    List<String> roleNames =
        ofNullable(source.getClaimAsStringList(AUTHORITY_CLAIM)).orElse(emptyList());
    Stream<GrantedAuthority> roles = roleNames.stream().map(SimpleGrantedAuthority::new);
    Stream<GrantedAuthority> normalizedRoles =
        roleNames.stream().map(r -> new SimpleGrantedAuthority(defaultNormalizer.apply(r)));
    return Stream.concat(roles, normalizedRoles).collect(Collectors.toSet());
  }
}
