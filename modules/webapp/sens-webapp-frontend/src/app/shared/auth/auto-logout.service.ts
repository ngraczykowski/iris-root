import { Injectable } from '@angular/core';
import { AuthenticatedUserFacade } from '@app/shared/auth/authenticated-user-facade.service';
import { BackgroundService } from '@app/shared/background-services-manager';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { Subscription } from 'rxjs';

@Injectable()
export class AutoLogoutService implements BackgroundService {

  private subscription: Subscription;

  constructor(
      private eventService: LocalEventService,
      private authenticatedUser: AuthenticatedUserFacade) {
  }

  startService(): void {
    this.subscription = this.eventService.subscribe(event => {
      this.authenticatedUser.logout();
    }, [EventKey.AUTO_LOGOUT]);
  }

  stopService(): void {
    this.subscription.unsubscribe();
  }
}
