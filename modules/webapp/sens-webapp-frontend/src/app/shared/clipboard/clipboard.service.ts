import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { EventKey } from '../event/event.service.model';
import { LocalEventService } from '../event/local-event.service';

// TODO (iwnek) refactored from very ugly code, this service should be tested
@Injectable()
export class ClipboardService {

  constructor(private eventService: LocalEventService) { }

  copy(text: string) {
    this.copyToClipboard(text)
        .subscribe(
            () => this.sendSuccessNotificationEvent(),
            () => this.sendErrorNotificationEvent()
        );
  }

  private copyToClipboard(text: string): Observable<any> {
    return new Observable(observer => {
      try {
        this.tryCopyToClipboard(text);
        observer.next();
        observer.complete();
      } catch (err) {
        observer.error(err);
      }
    });
  }

  private tryCopyToClipboard(text: string) {
    document.addEventListener('copy', (e: ClipboardEvent) => {
      e.clipboardData.setData('text/plain', text);
      e.preventDefault();
      document.removeEventListener('copy', null);
    });
    const result = document.execCommand('copy');
    if (!result) {
      throw new Error('Copy task failed. Result: ' + result);
    }
  }

  private sendErrorNotificationEvent() {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        type: 'error',
        message: 'copyToClipboard.error'
      }
    });
  }

  private sendSuccessNotificationEvent() {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        type: 'success',
        message: 'copyToClipboard.success'
      }
    });
  }
}
