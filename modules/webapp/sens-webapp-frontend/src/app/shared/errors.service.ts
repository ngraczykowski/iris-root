import { Injectable } from '@angular/core';
import { NavigationError, Router } from '@angular/router';

import { Observable, of } from 'rxjs';

@Injectable()
export class ErrorsService {

  constructor(private router: Router) {
    this.router.events.subscribe(this.handleRouterEvent);
  }

  private handleRouterEvent(event) {
    if (event instanceof NavigationError) {
      if (navigator.onLine) {
        this.log(event.error).subscribe((error) => this.router.navigate(['/404']));
      } else {
        this.router.navigate(['/404']);
      }
    }
  }

  log(error: any): Observable<any> {
    // TODO(ahaczewski): Log or send an error to backend.
    return of(error);
  }
}
