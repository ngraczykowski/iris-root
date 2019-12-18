import { Injectable } from '@angular/core';
import { AuthService } from '@app/shared/auth/auth.service';
import { logout, logoutFailed, logoutSuccess } from '@app/shared/auth/store/auth.actions';
import { Actions, Effect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { catchError, exhaustMap, map } from 'rxjs/operators';

@Injectable()
export class AuthEffects {
  constructor(
      private readonly actions$: Actions,
      private readonly authService: AuthService
  ) {}


  @Effect()
  logout$ = this.actions$.pipe(
      ofType(logout.type),
      exhaustMap(() => this.authService.logout()),
      map(() => logoutSuccess()),
      catchError((reason) => of(logoutFailed(reason)))
  );
}


