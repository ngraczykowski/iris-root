import { Injectable } from '@angular/core';
import { AuthService } from '@app/shared/auth/auth.service';
import { BackgroundService } from '@app/shared/background-services-manager';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { Subscription } from 'rxjs';

@Injectable()
export class AutoLogoutService implements BackgroundService {

  private subscription: Subscription;

  constructor(
      private eventService: LocalEventService,
      private authService: AuthService) {
  }

  startService(): void {
    this.subscription = this.eventService.subscribe(event => {
      this.authService.logout();
    }, [EventKey.AUTO_LOGOUT]);
  }

  stopService(): void {
    this.subscription.unsubscribe();
  }
}
