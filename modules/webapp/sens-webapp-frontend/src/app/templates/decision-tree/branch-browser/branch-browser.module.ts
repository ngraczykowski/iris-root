import { NgModule } from '@angular/core';
import { DecisionTreeOperationsModule } from '@app/templates/decision-tree/decision-tree-operations/decision-tree-operations.module';
import { SharedModule } from '../../../shared/shared.module';
import { BranchTableModule } from '../branch-table/branch-table.module';
import { BranchBrowserComponent } from './branch-browser.component';

@NgModule({
  imports: [
    SharedModule,
    BranchTableModule,
    DecisionTreeOperationsModule
  ],
  declarations: [
    BranchBrowserComponent
  ],
  exports: [
    BranchBrowserComponent
  ]
})
export class BranchBrowserModule {}
