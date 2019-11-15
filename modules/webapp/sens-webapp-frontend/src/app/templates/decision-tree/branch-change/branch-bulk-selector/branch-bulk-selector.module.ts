import { NgModule } from '@angular/core';
import { SharedModule } from '@app/shared/shared.module';
import { BranchBulkSelectorComponent } from '@app/templates/decision-tree/branch-change/branch-bulk-selector/branch-bulk-selector.component';
import { BranchTableModule } from '@app/templates/decision-tree/branch-table/branch-table.module';
import { DecisionTreeOperationsModule } from '@app/templates/decision-tree/decision-tree-operations/decision-tree-operations.module';

@NgModule({
  imports: [
    SharedModule,
    BranchTableModule,
    DecisionTreeOperationsModule,
  ],
  declarations: [
    BranchBulkSelectorComponent
  ],
  exports: [
    BranchBulkSelectorComponent
  ]
})
export class BranchBulkSelectorModule {

}
