import { HTTP_INTERCEPTORS } from '@angular/common/http';

import { AuthErrorInterceptor } from '@app/shared/auth/interceptors/auth-error.interceptor';
import { AccessDeniedErrorRedirectingInterceptor } from '@app/shared/http/access-denied-error-redirecting-interceptor';
import { HttpTimeoutErrorInterceptor } from '@app/shared/http/http-timeout-error-interceptor';
import { InternalServerErrorRedirectingInterceptor } from '@app/shared/http/internal-server-error-redirecting-interceptor';
import { NotFoundErrorRedirectingInterceptor } from '@app/shared/http/not-found-error-redirecting-interceptor';

/** Http interceptor providers in outside-in order */
export const httpInterceptorProviders = [
  {provide: HTTP_INTERCEPTORS, useClass: AuthErrorInterceptor, multi: true},
  {provide: HTTP_INTERCEPTORS, useClass: HttpTimeoutErrorInterceptor, multi: true},
  {provide: HTTP_INTERCEPTORS, useClass: InternalServerErrorRedirectingInterceptor, multi: true},
  {provide: HTTP_INTERCEPTORS, useClass: NotFoundErrorRedirectingInterceptor, multi: true},
  {provide: HTTP_INTERCEPTORS, useClass: AccessDeniedErrorRedirectingInterceptor, multi: true}
];
