import { HttpErrorResponse } from '@angular/common/http';
import { ErrorHandler, Injectable, Injector } from '@angular/core';
import { Router } from '@angular/router';

@Injectable()
export class DefaultErrorHandler implements ErrorHandler {

  constructor(
      private injector: Injector) {
  }

  handleError(error: any): void {
    const router = this.injector.get(Router);

    if (error instanceof HttpErrorResponse) {
      this.handleNetworkError(error);
    } else {
      this.handleClientError(router, error);
    }

    console.error(error);
  }

  private handleNetworkError(error: any) {
    if (!navigator.onLine) {
      this.handleOffLine();
    } else {
      this.handleResponseError(error);
    }
  }

  private handleOffLine() {
    // TODO(ahaczewski): Show notification about being off-line.
    console.error('Application is offline');
  }

  private handleResponseError(error: HttpErrorResponse) {
    // TODO(ahaczewski): Handle uncaught HTTP errors with notification.
    console.error('Caught error response');
  }

  private handleClientError(router: Router, error: any) {
    // TODO(ahaczewski): Handle client error by navigating to error page or something.
    console.error('Caught client error');
  }
}
