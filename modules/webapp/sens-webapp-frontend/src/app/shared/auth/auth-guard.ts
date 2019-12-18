import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  CanActivateChild,
  Router,
  RouterStateSnapshot,
  UrlTree
} from '@angular/router';
import { AuthenticatedUserFacade } from '@app/shared/auth/authenticated-user-facade.service';

import { combineLatest, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable()
export class AuthGuard implements CanActivate, CanActivateChild {

  constructor(
      private readonly router: Router,
      private readonly authenticatedUserFacade: AuthenticatedUserFacade) {
  }

  // TODO(bgulowaty): think about it
  canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot):
      boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
    return this._canActivate(childRoute);
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):
      Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this._canActivate(route);
  }

  private _canActivate(route: ActivatedRouteSnapshot) {
    const isLoggedIn$ = this.authenticatedUserFacade.isLoggedIn();

    const requiredAuthorities: string[] = route.data.authorities;

    if (requiredAuthorities == null || requiredAuthorities.length === 0) {
      return isLoggedIn$.pipe(
          map(canHaveAccess => canHaveAccess ? canHaveAccess : this.router.parseUrl('/403')),
      );
    }

    const hasRequiredAuthorities$ = this.authenticatedUserFacade.hasAuthorities(requiredAuthorities);

    return combineLatest([isLoggedIn$, hasRequiredAuthorities$]).pipe(
        map(([isLoggedIn, hasRequiredAuthorities]) => isLoggedIn && hasRequiredAuthorities),
        map(canHaveAccess => canHaveAccess ? canHaveAccess : this.router.parseUrl('/403')),
    );
  }
}
