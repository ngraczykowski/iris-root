import { Injectable } from '@angular/core';
import {
  LoginFailed,
  LogoutFailed,
  LogoutSuccess,
  SecurityActionTypes
} from '@app/shared/security/store/security.actions';
import { Actions, Effect, ofType } from '@ngrx/effects';
import { KeycloakService } from 'keycloak-angular';
import { of } from 'rxjs';
import { catchError, exhaustMap, map } from 'rxjs/operators';

@Injectable()
export class SecurityEffects {
  constructor(
    private readonly actions$: Actions,
    private readonly keycloakService: KeycloakService
  ) {}

  @Effect()
  logout$ = this.actions$.pipe(
    ofType(SecurityActionTypes.logout),
    exhaustMap(() => this.keycloakService.logout('/')),
    map(() => new LogoutSuccess()),
    catchError(err => of(new LogoutFailed(err)))
  );

  @Effect()
  login$ = this.actions$.pipe(
    ofType(SecurityActionTypes.tryLogin),
    exhaustMap(() => this.keycloakService.login()),
    catchError(err => of(new LoginFailed(err)))
  );
}


