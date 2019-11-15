import { Component, OnDestroy, OnInit } from '@angular/core';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { environment } from '@env/environment';
import { Subscription } from 'rxjs/Subscription';

export class BriefMessage {
  constructor(public message: String, public params: any) { }
}

@Component({
  selector: 'app-brief-message',
  templateUrl: './brief-message.component.html',
  styleUrls: ['./brief-message.component.scss']
})
export class BriefMessageComponent implements OnInit, OnDestroy {

  briefMessages: Array<BriefMessage> = [];

  private subscription: Subscription;

  constructor(private eventService: LocalEventService) { }

  ngOnInit() {
    this.subscription = this.eventService.subscribe(event => {
      const message = new BriefMessage(event.data.message, event.data.params);
      this.briefMessages = [];
      this.addBriefMessage(message);
      setTimeout(() => {
        this.removeBriefMessage(message);
      }, event.data.timeout ? event.data.timeout : environment.notifications.defaultTimeoutInSec * 1000);
    }, [EventKey.NOTIFICATION]);
  }

  private addBriefMessage(message: BriefMessage) {
    this.briefMessages.push(message);
  }

  private removeBriefMessage(message: BriefMessage) {
    const index = this.briefMessages.indexOf(message, 0);
    if (index > -1) {
      this.briefMessages.splice(index, 1);
    }
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

}
