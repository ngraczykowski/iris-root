import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Principal } from '@app/shared/auth/principal.model';
import { ErrorDto } from '@app/shared/error-dto.model';
import { RedirectorService } from '@app/shared/redirector.service';

import { environment } from '@env/environment';
import { CookieService } from 'ngx-cookie-service';
import { SessionStorageService } from 'ngx-webstorage';

import { Observable, of, throwError } from 'rxjs';
import { catchError, finalize, map, shareReplay, tap } from 'rxjs/operators';

@Injectable()
export class AuthService {

  private static readonly SESSION_COOKIE = 'JSESSIONID';

  private readonly url = environment.serverApiUrl + 'api';

  principal: Principal = null;
  private principalDto$: Observable<PrincipalDto> = null;

  constructor(
      private http: HttpClient,
      private router: Router,
      private sessionStorage: SessionStorageService,
      private cookieService: CookieService,
      private redirector: RedirectorService) {
  }

  isLoginUrl(url: string): boolean {
    return url.endsWith(this.url + '/check-auth');
  }

  login(): Observable<boolean> | boolean {
    if (!this.principalDto$) {
      this.principalDto$ = this.setupLoginRequest();
    }

    return this.principalDto$.pipe(
        map(() => true),
        catchError(error => {
          if (error instanceof HttpErrorResponse) {
            const response = <HttpErrorResponse>error;

            if (response.status === 401) {
              this.removeSessionCookie();
              this.handleUnauthorized(response);
              return of(false);
            }
          }

          return throwError(error);
        })
    );
  }

  private setupLoginRequest(): Observable<PrincipalDto> {
    const principalDto$ = this.http.get<PrincipalDto>(this.url + '/check-auth');

    return principalDto$.pipe(
        tap(dto => this.setPrincipal(dto)),
        finalize(() => this.principalDto$ = null),
        shareReplay()
    );
  }

  private setPrincipal(principal: PrincipalDto): void {
    this.principal = new Principal(principal.userName, principal.displayName, principal.authorities, principal.superUser);
  }

  private handleUnauthorized(response: HttpErrorResponse): void {
    console.log('Unauthorized, need to login');

    const body = <UnauthorizedResponseDto>response.error;

    if (body.loginUrl.length > 0) {
      console.log('Redirecting to login URL:', body.loginUrl);

      if (!this.redirector.redirectToUrl(body.loginUrl)) {
        console.error('Failed to redirect user to login page. Most probably there is no browser' +
            ' (as there is no document) that can be redirected.');
      }
    } else {
      console.error('No login URL received from backend, unable to login');
    }
  }

  isLoggedIn(): boolean {
    return this.hasSessionCookie() && this.principal !== null;
  }

  private hasSessionCookie(): boolean {
    return this.cookieService.check(AuthService.SESSION_COOKIE);
  }

  hasAuthority(authority: string): boolean {
    return this.isLoggedIn() && this.principal.hasAuthority(authority);
  }

  hasAccessToUrl(url: string): boolean {
    // FIXME(ahaczewski): Remove when all users are changed to some other mechanism.
    const authority = environment.auth.authorityPageUrlMapping[url];

    return authority && this.hasAuthority(authority);
  }

  logout(): void {
    const response = this.http.post<LogoutResponseDto>(this.url + '/logout', null);

    response
        .pipe(finalize(() => this.cleanupOnLogout()))
        .subscribe(
            dto => this.handleLogoutResponse(dto),
            error => this.handleLogoutError(error)
        );
  }

  getDisplayName() {
    return this.principal.displayName;
  }

  getUserName() {
    return this.principal.userName;
  }

  hasSuperuserPermissions() {
    return this.principal.superUser === true;
  }

  private handleLogoutResponse(dto: LogoutResponseDto) {
    this.cleanupOnLogout();
    this.redirector.redirectToUrl(dto.redirectToUrl);
  }

  private handleLogoutError(error: any): Observable<any> {
    // TODO(ahaczewski): Something went wrong and we won't be able to redirect to CAS - notify
    // user about that.

    return throwError(error);
  }

  private cleanupOnLogout() {
    this.clearPrincipal();
    this.removeSessionCookie();
  }

  private clearPrincipal(): void {
    this.principal = null;
  }

  private removeSessionCookie(): void {
    this.cookieService.delete(AuthService.SESSION_COOKIE);
  }
}

interface PrincipalDto {
  userName: string;
  displayName: string;
  authorities: string[];
  superUser: boolean;
}

interface LogoutResponseDto {
  redirectToUrl: string;
}

interface UnauthorizedResponseDto {

  loginUrl: string;
  error: ErrorDto;
}
