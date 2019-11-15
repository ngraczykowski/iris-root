import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';

import { AuthService } from '@app/shared/auth/auth.service';
import { StartPageGuard } from '@app/shared/auth/start-page-guard';

import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(
      private authService: AuthService,
      private router: Router,
      private startPageGuard: StartPageGuard) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | boolean {
    const login = this.authService.login();

    if (login instanceof Observable) {
      return login.pipe(
          tap(loggedIn => {
            // NOTE(ahaczewski): We store requested URL if and only if we were unable to login with
            // simple request to backend. After login observable is completed, user will be
            // redirected to external authentication server and upon return it would be nice that
            // StartPageGuard navigates the user to originally requested page, therefore we store it
            // here.
            //
            // On the other hand loggedIn will be true when login request to backend was
            // successful, i.e. confirmed that user holds valid session cookie and returned user
            // authorities. In such a case user is not redirected out of our Angular app and
            // navigation can commence as usual.
            if (!loggedIn) {
              this.startPageGuard.forceStartPageUrl(state.url);
            }
          })
      );
    } else {
      return login;
    }
  }
}
