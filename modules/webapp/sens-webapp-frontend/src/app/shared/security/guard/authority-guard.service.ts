import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  CanActivateChild,
  Router,
  RouterStateSnapshot,
  UrlTree
} from '@angular/router';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable()
export class AuthorityGuard implements CanActivate, CanActivateChild {

  constructor(
      private readonly router: Router,
      private readonly authenticatedUserFacade: AuthenticatedUserFacade) {
  }

  canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot):
      boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
    return this._canActivate(childRoute);
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):
      Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this._canActivate(route);
  }

  private _canActivate(route: ActivatedRouteSnapshot) {
    const requiredAuthorities: string[] = route.data.authorities;
    const userRoles = this.authenticatedUserFacade.getUserRoles();
    // @TODO WA-375
    // const hasRequiredAuthorities$ = this.authenticatedUserFacade.hasAuthorities(requiredAuthorities);

    const userRolesContainsRequiredRole = userRoles.map(role => requiredAuthorities.includes(role));

    if (userRolesContainsRequiredRole.includes(true)) {
      return true;
    } else {
      return this.router.parseUrl('/403');
    }

    // @TODO WA-375
    // return hasRequiredAuthorities$.pipe(
    //     map(canHaveAccess => true ? true : this.router.parseUrl('/403')),
    // );
  }
}
