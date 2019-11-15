import { NgModule } from '@angular/core';
import { SharedModule } from '@app/shared/shared.module';
import { BranchUpdaterComponent } from '@app/templates/decision-tree/branch-change/branch-updater/branch-updater.component';
import { BranchUpdaterService } from '@app/templates/decision-tree/branch-change/branch-updater/branch-updater.service';
import { ChangeRequestEditFormModule } from '@app/templates/decision-tree/branch-change/branch-updater/change-request-edit-form/change-request-edit-form.module';
import { CreateChangeRequestClient } from '@app/templates/decision-tree/branch-change/branch-updater/create-change-request-client.service';
import { BranchTableModule } from '@app/templates/decision-tree/branch-table/branch-table.module';

@NgModule({
  imports: [
    SharedModule,
    BranchTableModule,
    ChangeRequestEditFormModule
  ],
  providers: [
    CreateChangeRequestClient,
    BranchUpdaterService
  ],
  declarations: [
    BranchUpdaterComponent,
  ],
  exports: [
    BranchUpdaterComponent
  ]
})
export class BranchUpdaterModule {

}
