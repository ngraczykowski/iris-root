import { Inject, Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  CanActivateChild,
  Router,
  RouterStateSnapshot,
  UrlTree
} from '@angular/router';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';
import {
  RoleDefaultPageMappings,
  ROLES_REDIRECT_CONFIG
} from '@app/shared/security/role-default-page-mappings';

import { Observable } from 'rxjs';

@Injectable()
export class AuthorityGuard implements CanActivate, CanActivateChild {

  constructor(
      @Inject(ROLES_REDIRECT_CONFIG) private readonly roleDefaultPageMappings: RoleDefaultPageMappings,
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

    const userRolesContainsRequiredRole = userRoles.some(userRole => requiredAuthorities.includes(userRole));

    if (userRolesContainsRequiredRole) {
      return true;
    } else {
      const defaultPathForRole = this.roleDefaultPageMappings.getForRoles(userRoles);
      return defaultPathForRole !== undefined
          ? this.goTo(defaultPathForRole)
          : this.goTo('/403');
    }

    // @TODO WA-375
    // return hasRequiredAuthorities$.pipe(
    //     map(canHaveAccess => true ? true : this.router.parseUrl('/403')),
    // );
  }

  private goTo(url) {
    return this.router.parseUrl(url);
  }
}
