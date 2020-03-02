import { Injectable } from '@angular/core';
import * as fromRoot from '@app/reducers';
import { Principal } from '@app/shared/security/principal.model';
import { Logout } from '@app/shared/security/store/security.actions';
import { select, Store } from '@ngrx/store';
import { Observable, of } from 'rxjs';
import { getLoggedInPrincipal, hasAllAuthorities, isLoggedIn } from './store/security.selectors';

@Injectable()
export class AuthenticatedUserFacade {

  constructor(private readonly store: Store<fromRoot.State>) {
  }

  isLoggedIn(): Observable<boolean> {
    return this.store.pipe(
        select(isLoggedIn),
    );
  }

  hasAuthorities(requiredAuthorities: string[]): Observable<boolean> {
    return this.store.pipe(
        select(hasAllAuthorities(requiredAuthorities))
    );
  }

  hasAuthority(authority: string): Observable<boolean> {
    return this.hasAuthorities([authority]);
  }

  hasAccessToUrl(url: string): Observable<boolean> {
    return of(true);
  }

  logout() {
    return this.store.dispatch(new Logout());
  }

  getPrincipal(): Observable<Principal> {
    return this.store.pipe(
        select(getLoggedInPrincipal)
    );
  }

  hasSuperuserPermissions(): Observable<boolean> {
    return of(true);
  }

}
