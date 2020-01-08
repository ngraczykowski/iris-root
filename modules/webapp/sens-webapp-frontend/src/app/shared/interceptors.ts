import { HTTP_INTERCEPTORS } from '@angular/common/http';

import { AccessDeniedErrorRedirectingInterceptor } from '@app/shared/http/access-denied-error-redirecting-interceptor';
import { HttpTimeoutErrorInterceptor } from '@app/shared/http/http-timeout-error-interceptor';
import { InternalServerErrorRedirectingInterceptor } from '@app/shared/http/internal-server-error-redirecting-interceptor';
import { NotFoundErrorRedirectingInterceptor } from '@app/shared/http/not-found-error-redirecting-interceptor';
import { KeycloakBearerInterceptor } from 'keycloak-angular';

/** Http interceptor providers in outside-in order */
export const httpInterceptorProviders = [
  {provide: HTTP_INTERCEPTORS, useClass: KeycloakBearerInterceptor, multi: true},
  {provide: HTTP_INTERCEPTORS, useClass: HttpTimeoutErrorInterceptor, multi: true},
  {provide: HTTP_INTERCEPTORS, useClass: InternalServerErrorRedirectingInterceptor, multi: true},
  {provide: HTTP_INTERCEPTORS, useClass: NotFoundErrorRedirectingInterceptor, multi: true},
  {provide: HTTP_INTERCEPTORS, useClass: AccessDeniedErrorRedirectingInterceptor, multi: true}
];
