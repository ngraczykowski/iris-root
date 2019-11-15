import { Injectable, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { EventKey } from '../../event/event.service.model';
import { LocalEventService } from '../../event/local-event.service';
import { LastUserActivityStorage } from './last-user-activity-storage.model';
import { SessionStorageKey } from './session-storage-key';

@Injectable()
export class LastUserActivityLocalStorage extends LastUserActivityStorage implements OnDestroy {

  private eventServiceSubscription: Subscription;

  constructor(eventService: LocalEventService) {
    super();
    LastUserActivityLocalStorage.saveCurrentTimeToSessionStorage();
    this.eventServiceSubscription = eventService.subscribe(
        () => LastUserActivityLocalStorage.saveCurrentTimeToSessionStorage(),
        [EventKey.ACTIVITY]);
  }

  private static fetchLastActivityIsoString() {
    return localStorage.getItem(SessionStorageKey.LAST_ACTIVITY_TIME);
  }

  private static saveCurrentTimeToSessionStorage() {
    localStorage.setItem(SessionStorageKey.LAST_ACTIVITY_TIME, new Date().toISOString());
  }

  getLastUserActivityDate(): Date {
    const lastActivityDateIsoString = LastUserActivityLocalStorage.fetchLastActivityIsoString();
    return new Date(lastActivityDateIsoString);
  }

  getLastUserActivityTime(): number {
    return new Date(LastUserActivityLocalStorage.fetchLastActivityIsoString()).getTime();
  }

  ngOnDestroy(): void {
    this.eventServiceSubscription.unsubscribe();
  }
}
