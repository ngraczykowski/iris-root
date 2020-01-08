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

    const hasRequiredAuthorities$ = this.authenticatedUserFacade.hasAuthorities(requiredAuthorities);

    return hasRequiredAuthorities$.pipe(
        map(canHaveAccess => canHaveAccess ? canHaveAccess : this.router.parseUrl('/403')),
    );
  }
}
