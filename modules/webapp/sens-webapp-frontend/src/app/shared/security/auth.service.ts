import { Injectable, OnDestroy } from '@angular/core';
import * as fromRoot from '@app/reducers';
import { Principal } from '@app/shared/security/principal.model';
import { environment } from '@env/environment.prod';
import { Store } from '@ngrx/store';
import { KeycloakEvent, KeycloakEventType, KeycloakService } from 'keycloak-angular';
import { Keycloak } from 'keycloak-angular/lib/core/services/keycloak.service';

import { from, Observable, Subscription } from 'rxjs';
import { filter, map, mergeMap, tap } from 'rxjs/operators';
import { loginSuccess, setPrincipal } from './store/security.actions';

@Injectable()
export class AuthService implements OnDestroy {

  subscriptions: Subscription[] = [];

  constructor(
      private readonly keycloak: KeycloakService,
      private readonly store: Store<fromRoot.State>
  ) {
    this.subscriptions.push(
        this.authenticationSuccessfulEvent(keycloak).pipe(
            tap(() => this.store.dispatch(loginSuccess())),
            mergeMap(() => this.fetchPrincipal()),
        ).subscribe({
          next: (principal: Principal) =>
              this.store.dispatch(setPrincipal({principal: principal}))
        })
    );
  }

  private static mapToPrincipal(keycloakProfile: Keycloak.KeycloakProfile, userRoles: string[]):
      Principal {
    return new Principal(
        keycloakProfile.username,
        `${keycloakProfile.firstName} ${keycloakProfile.lastName}`,
        userRoles,
        userRoles.includes(environment.auth.keycloak.adminRoleName.toLowerCase())
    );
  }

  private authenticationSuccessfulEvent(keycloak: KeycloakService): Observable<any> {
    return keycloak.keycloakEvents$.pipe(
        map((event: KeycloakEvent) => event.type),
        filter((eventType: KeycloakEventType) =>
            eventType === KeycloakEventType.OnAuthSuccess)
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }

  private fetchPrincipal(): Observable<Principal> {
    const userRoles = this.keycloak.getUserRoles(false);

    return from(this.keycloak.loadUserProfile()).pipe(
        map((keycloakProfile: Keycloak.KeycloakProfile) =>
            AuthService.mapToPrincipal(keycloakProfile, userRoles))
    );
  }

  public login(redirectUri?: string): Observable<void> {
    return from(this.keycloak.login({redirectUri: redirectUri}));
  }
}
