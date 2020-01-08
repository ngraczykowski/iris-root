import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  CanActivateChild, Router,
  RouterStateSnapshot, UrlTree
} from '@angular/router';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable()
export class AuthenticationGuard implements CanActivate, CanActivateChild {

  constructor(
      private readonly authenticatedUser: AuthenticatedUserFacade,
      private readonly router: Router
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):
      Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this._canActivate(route);
  }

  canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot):
      Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this._canActivate(childRoute);
  }

  private _canActivate(route: ActivatedRouteSnapshot): Observable<boolean | UrlTree> {
    const isLoggedIn$ = this.authenticatedUser.isLoggedIn();

    return isLoggedIn$.pipe(
        map(isLoggedIn => isLoggedIn ? isLoggedIn : this.router.parseUrl('/401'))
    );
  }

}
