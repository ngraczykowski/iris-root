import { NgModule } from '@angular/core';
import { PageableDynamicTableModule } from '../../components/pageable-dynamic-table/pageable-dynamic-table.module';
import { SharedModule } from '../../shared/shared.module';
import { InboxTabComponent } from './inbox-tab/inbox-tab.component';
import { InboxViewModule } from './inbox-view.module';
import { InboxComponent } from './inbox.component';
import { InboxService } from './inbox.service';
import { SolvedMessageTableDataProvider } from './solved-message-table-data-provider';
import { UnsolvedMessageTableDataProvider } from './unsolved-message-table-data-provider';

@NgModule({
  imports: [
    SharedModule,
    InboxViewModule,
    PageableDynamicTableModule
  ],
  providers: [
    InboxService,
    UnsolvedMessageTableDataProvider,
    SolvedMessageTableDataProvider
  ],
  declarations: [
    InboxComponent,
    InboxTabComponent
  ],
  exports: [
    InboxComponent,
    InboxTabComponent
  ]
})
export class InboxModule {}
