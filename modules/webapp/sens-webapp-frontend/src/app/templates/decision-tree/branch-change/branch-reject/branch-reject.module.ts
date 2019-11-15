import { NgModule } from '@angular/core';
import { SharedModule } from '@app/shared/shared.module';
import { BranchRejectComponent } from '@app/templates/decision-tree/branch-change/branch-reject/branch-reject.component';
import { BranchRejectService } from '@app/templates/decision-tree/branch-change/branch-reject/branch-reject.service';
import { ChangeRequestRejectFormModule } from '@app/templates/decision-tree/branch-change/branch-reject/change-request-reject-form/change-request-reject-form.module';
import { RejectChangeRequestClient } from '@app/templates/decision-tree/branch-change/branch-reject/reject-change-request-client.service';
import { BranchTableModule } from '@app/templates/decision-tree/branch-table/branch-table.module';

@NgModule({
  imports: [
    SharedModule,
    BranchTableModule,
    ChangeRequestRejectFormModule
  ],
  providers: [
    RejectChangeRequestClient,
    BranchRejectService
  ],
  declarations: [
    BranchRejectComponent
  ],
  exports: [
    BranchRejectComponent
  ]
})
export class BranchRejectModule {

}
