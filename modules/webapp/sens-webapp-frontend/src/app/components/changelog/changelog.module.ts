import { NgModule } from '@angular/core';
import { SharedModule } from '@app/shared/shared.module';
import { ChangelogClient } from './changelog-client';
import { ChangelogHistoryComponent } from './changelog-history/changelog-history.component';
import { ChangelogSummaryComponent } from './changelog-summary/changelog-summary.component';
import { ChangelogComponent } from './changelog.component';

@NgModule({
  imports: [
    SharedModule
  ],
  declarations: [
    ChangelogComponent,
    ChangelogSummaryComponent,
    ChangelogHistoryComponent
  ],
  providers: [
    ChangelogClient
  ],
  exports: [
    ChangelogComponent
  ]
})
export class ChangelogModule {}
