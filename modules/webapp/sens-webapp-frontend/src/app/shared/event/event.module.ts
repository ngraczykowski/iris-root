import { NgModule } from '@angular/core';
import { BroadcastChannelWrapper } from './broadcast-channel-wrapper';
import { InterAppEventService } from './inter-app-event.service';
import { LocalEventService } from './local-event.service';

@NgModule({
  providers: [
    LocalEventService,
    BroadcastChannelWrapper,
    InterAppEventService
  ]
})
export class EventModule {}
