import { Injectable, OnDestroy } from '@angular/core';
import { BroadcastChannelWrapper } from '@app/shared/event/broadcast-channel-wrapper';
import { Event, EventKey, EventService } from '@app/shared/event/event.service.model';
import { SubjectEventHolder } from '@app/shared/event/subject-event-holder';
import { Subscription } from 'rxjs';

@Injectable()
export class InterAppEventService implements EventService, OnDestroy {
  private events = new SubjectEventHolder();

  constructor(private channel: BroadcastChannelWrapper) {
    this.channel.onmessage = (e) => this.events.publish(e.data);
  }

  subscribe(eventHandler: (ev: Event) => void, keyFilters?: Array<EventKey>): Subscription {
    return this.events.observe(keyFilters).subscribe((e) => eventHandler(e));
  }

  sendEvent(message: Event) {
    this.channel.postMessage(message);
  }

  ngOnDestroy(): void {
    this.channel.close();
  }
}
