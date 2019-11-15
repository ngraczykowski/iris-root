import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivateChild,
  Router,
  RouterStateSnapshot
} from '@angular/router';
import { AuthGuard } from '@app/shared/auth/auth-guard';
import { AuthService } from '@app/shared/auth/auth.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable()
export class AccessGuard implements CanActivateChild {

  constructor(
      private authService: AuthService,
      private router: Router,
      private authGuard: AuthGuard) {
  }

  canActivateChild(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | boolean {
    const authGuardCanActivate = this.authGuard.canActivate(route, state);
    if (authGuardCanActivate instanceof Observable) {
      return authGuardCanActivate.pipe(map(c => this._canActivateChild(c, route)));
    } else {
      return this._canActivateChild(authGuardCanActivate, route);
    }
  }

  private _canActivateChild(authGuardCanActivate: boolean, route: ActivatedRouteSnapshot): boolean {
    if (!authGuardCanActivate) {
      return false;
    } else if (!this.hasAuthority(route)) {
      this.router.navigate(['/403']);
      return false;
    }
    return true;
  }

  private hasAuthority(route: ActivatedRouteSnapshot) {
    const required = route.data.authority;
    return this.authService.hasAuthority(required);
  }
}
