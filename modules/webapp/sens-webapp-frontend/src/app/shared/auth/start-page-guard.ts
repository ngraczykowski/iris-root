import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '@app/shared/auth/auth.service';

import { environment } from '@env/environment';
import { SessionStorageService } from 'ngx-webstorage';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable()
export class StartPageGuard implements CanActivate {

  private static readonly REDIRECT_URL_KEY = 'redirectUrl';

  constructor(
      private authService: AuthService,
      private router: Router,
      private sessionStorageService: SessionStorageService) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    const login = this.authService.login();

    if (login instanceof Observable) {
      return login.pipe(
          map(loggedIn => {
            if (loggedIn) {
              return this.resolveStartPage();
            } else {
              return false;
            }
          })
      );
    } else {
      return this.resolveStartPage();
    }
  }

  resolveStartPage(): boolean {
    const forcedStartPageUrl = this.releaseForcedStartPageUrl();

    if (forcedStartPageUrl !== null) {
      this.router.navigateByUrl(forcedStartPageUrl);
      return false;
    }

    const startUrl = environment.auth.authorityStartPageUrlPriority[0];
    console.log('Starting to navigate to', startUrl);
    let navigation: Promise<boolean> = this.performNavigation(startUrl);

    for (let i = 1; i < environment.auth.authorityStartPageUrlPriority.length; i++) {
      const url = environment.auth.authorityStartPageUrlPriority[i];

      navigation = navigation
          .then(activated => this.handleNavigationResult(url, activated))
          .then(nextUrl => this.performNavigation(nextUrl));
    }

    return false;
  }

  private performNavigation(url: string | null): Promise<boolean> {
    if (url !== null) {
      return this.router.navigateByUrl(url);
    } else {
      console.log('Stopping navigation');
      return Promise.resolve(true);
    }
  }

  private handleNavigationResult(url: string, activated: boolean): Promise<string | null> {
    if (activated === false) {
      console.log('Then trying to navigate to', url);
      return Promise.resolve(url);
    }

    return Promise.resolve(null);
  }

  forceStartPageUrl(redirectUrl: string): void {
    console.log('Storing forced start page URL: ' + redirectUrl);
    this.sessionStorageService.store(StartPageGuard.REDIRECT_URL_KEY, redirectUrl);
  }

  private releaseForcedStartPageUrl(): string {
    const redirectUrl = this.sessionStorageService.retrieve(StartPageGuard.REDIRECT_URL_KEY);
    if (redirectUrl !== null) {
      console.log('Loaded forced start page URL: ' + redirectUrl);
    }
    this.sessionStorageService.clear(StartPageGuard.REDIRECT_URL_KEY);
    return redirectUrl;
  }
}
