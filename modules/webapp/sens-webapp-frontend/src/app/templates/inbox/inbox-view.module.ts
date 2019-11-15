import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { ActionViewComponent } from './message-table/views/action-view/action-view.component';
import { DisabledBranchDescriptionViewComponent } from './message-table/views/disabled-branch-description-view/disabled-branch-description-view.component';
import { SimpleViewComponent } from './message-table/views/simple-view/simple-view.component';

@NgModule({
  imports: [
    SharedModule,
  ],
  declarations: [
    SimpleViewComponent,
    DisabledBranchDescriptionViewComponent,
    ActionViewComponent,
  ],
  entryComponents: [
    SimpleViewComponent,
    DisabledBranchDescriptionViewComponent,
    ActionViewComponent
  ]
})
export class InboxViewModule {}
