import { Injectable } from '@angular/core';
import { BackgroundService } from '../../background-services-manager';
import { EventKey } from '../../event/event.service.model';
import { InterAppEventService } from '../../event/inter-app-event.service';
import { LocalEventService } from '../../event/local-event.service';

@Injectable()
export class UserActivityMonitorService implements BackgroundService {

  private subscriptions = [];

  constructor(
      private localEventService: LocalEventService,
      private interAppEventService: InterAppEventService) {
  }

  startService(): void {
    this.subscriptions.push(
        this.localEventService.subscribe(
            () => {
              this.localEventService.sendEvent({key: EventKey.ACTIVITY});
              this.interAppEventService.sendEvent({key: EventKey.OTHER_CONTEXT_ACTIVITY});
            },
            [EventKey.KEY_PRESS, EventKey.CLICK])
    );
  }

  stopService(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }
}
