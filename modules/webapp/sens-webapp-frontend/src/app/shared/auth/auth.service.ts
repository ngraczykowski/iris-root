import { Injectable } from '@angular/core';
import * as fromRoot from '@app/reducers';
import { Principal } from '@app/shared/auth/principal.model';
import { Store } from '@ngrx/store';
import { KeycloakEvent, KeycloakEventType, KeycloakService } from 'keycloak-angular';
import { Keycloak } from 'keycloak-angular/lib/core/services/keycloak.service';

import { from, Observable } from 'rxjs';
import { filter, map, mergeMap, tap } from 'rxjs/operators';
import { loginSuccess } from './store/auth.actions';

@Injectable()
export class AuthService {

  constructor(
      private keycloak: KeycloakService,
      private store: Store<fromRoot.State>
  ) {
    // TODO(bgulowaty): destroy this subscription
    keycloak.keycloakEvents$.pipe(
        map((event: KeycloakEvent) => event.type),
        filter((eventType: KeycloakEventType) =>
            eventType === KeycloakEventType.OnAuthSuccess),
        tap(() => this.store.dispatch(loginSuccess({principal: null}))),
        mergeMap(() => this.fetchPrincipal()),
    ).subscribe({
      next: (principal: Principal) =>
          this.store.dispatch(loginSuccess({principal: principal}))
    });
  }

  private static mapToPrincipal(keycloakProfile: Keycloak.KeycloakProfile, userRoles: string[]):
      Principal {
    return new Principal(
        keycloakProfile.username,
        `${keycloakProfile.firstName} ${keycloakProfile.lastName}`,
        userRoles,
        false
    );
  }

  private fetchPrincipal(): Observable<Principal> {
    const userRoles = this.keycloak.getUserRoles(true);

    return from(this.keycloak.loadUserProfile()).pipe(
        map((keycloakProfile: Keycloak.KeycloakProfile) =>
            AuthService.mapToPrincipal(keycloakProfile, userRoles))
    );
  }

  public logout(): Observable<void> {
    return from(this.keycloak.logout());
  }
}
