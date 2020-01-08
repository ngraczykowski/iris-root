import { Injectable } from '@angular/core';
import { AuthService } from '@app/shared/security/auth.service';
import {
  loginFailed,
  loginSuccess,
  logout,
  logoutFailed,
  logoutSuccess,
  tryLogin
} from '@app/shared/security/store/security.actions';
import { Actions, Effect, ofType } from '@ngrx/effects';
import { KeycloakService } from 'keycloak-angular';
import { of } from 'rxjs';
import { catchError, exhaustMap, map, tap } from 'rxjs/operators';

@Injectable()
export class SecurityEffects {
  constructor(
      private readonly actions$: Actions,
      private readonly authService: AuthService,
      private readonly keycloakService: KeycloakService
  ) {}

  @Effect()
  logout$ = this.actions$.pipe(
      ofType(logout.type),
      exhaustMap(() => this.keycloakService.logout()),
      map(() => logoutSuccess()),
      catchError(err => of(logoutFailed(err)))
  );

  @Effect()
  login$ = this.actions$.pipe(
      ofType(tryLogin.type),
      exhaustMap(() => this.keycloakService.login()),
      catchError(err => of(loginFailed(err)))
  );
}


