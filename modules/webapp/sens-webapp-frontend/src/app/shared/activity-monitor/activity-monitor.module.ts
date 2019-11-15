import { NgModule } from '@angular/core';
import { BackgroundServiceToken } from '../background-services-manager';
import { LastUserActivityLocalStorage } from './last-user-activity-storage/last-user-activity-local-storage';
import { LastUserActivityStorage } from './last-user-activity-storage/last-user-activity-storage.model';
import { UserActivityMonitorService } from './user-activity-monitor/user-activity-monitor.service';

@NgModule({
  providers: [
    {
      provide: BackgroundServiceToken,
      useClass: UserActivityMonitorService,
      multi: true
    },
    {
      provide: LastUserActivityStorage,
      useClass: LastUserActivityLocalStorage
    }
  ]
})

export class ActivityMonitorModule {}
