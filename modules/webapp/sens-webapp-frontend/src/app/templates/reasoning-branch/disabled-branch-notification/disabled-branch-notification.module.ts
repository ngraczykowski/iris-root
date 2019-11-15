import { NgModule } from '@angular/core';
import { SharedModule } from '../../../shared/shared.module';
import { InboxModule } from '../../inbox/inbox.module';
import { DisabledBranchNotificationComponent } from './disabled-branch-notification.component';

@NgModule({
  imports: [
    SharedModule,
    InboxModule
  ],
  declarations: [
    DisabledBranchNotificationComponent,
  ],
  exports: [
    DisabledBranchNotificationComponent
  ]
})
export class DisabledBranchNotificationModule {}
