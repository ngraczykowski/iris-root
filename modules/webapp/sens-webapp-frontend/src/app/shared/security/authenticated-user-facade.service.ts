import { Injectable } from '@angular/core';
import * as fromRoot from '@app/reducers';
import { AuthRole } from '@app/shared/security/model/auth-role.enum';
import { Logout, TryLogin } from '@app/shared/security/store/security.actions';
import { select, Store } from '@ngrx/store';
import { Observable, of, from } from 'rxjs';
import { getLoggedInPrincipal, hasAllAuthorities, isLoggedIn } from './store/security.selectors';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';

@Injectable()
export class AuthenticatedUserFacade {

  constructor(
      private readonly store: Store<fromRoot.State>,
      private readonly keycloak: KeycloakService,
  ) { }

  isLoggedIn(): Observable<boolean> {
    return from(this.keycloak.isLoggedIn());
    // @TODO WA-375
    // return this.store.pipe(
    //   select(isLoggedIn),
    // );
  }

  getUserRoles() {
    return this.keycloak.getUserRoles(true);
  }

  hasAuthorities(requiredAuthorities: string[]): Observable<boolean> {
    return this.store.pipe(
        select(hasAllAuthorities(requiredAuthorities))
    );
  }

  hasRole(authRole: AuthRole): boolean {
    const roles: string[] = this.getUserRoles();
    return roles && roles.length ? roles.includes(authRole) : false;
  }

  hasAuthority(authority: string): Observable<boolean> {
    return this.hasAuthorities([authority]);
  }

  hasAccessToUrl(url: string): Observable<boolean> {
    return of(this.checkUrlAccess(url));
  }

  logout() {
    return this.store.dispatch(new Logout());
  }

  getPrincipal(): Observable<KeycloakProfile> {
    return from(this.keycloak.loadUserProfile());
    // @TODO WA-375
    // return this.store.pipe(
    //   select(getLoggedInPrincipal)
    // );
  }

  checkUrlAccess(url: string): boolean {
    const roles = this.getUserRoles();

    if (roles.includes(AuthRole.ADMIN)) {
      return true;
    }

    if (url.includes('reasoning-branch')) {
      return roles.includes(AuthRole.BUSINESS_OPERATOR);
    } // TODO Remove with Reasoning-branch

    if (url.includes('reasoning-branch')) {
      return roles.includes(AuthRole.BUSINESS_OPERATOR);
    }

    if (url.includes('reports')) {
      return roles.includes(AuthRole.AUDITOR);
    }

    return false;
  }
}
