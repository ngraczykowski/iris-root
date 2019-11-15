import { NgModule } from '@angular/core';
import { SharedModule } from '../../../shared/shared.module';
import { BatchTypeManagementModule } from './batch-type-management/batch-type-management.module';
import { CopyDecisionTreeModule } from './copy-decision-tree/copy-decision-tree.module';
import { CreateNewDecisionTreeModule } from './create-new-decision-tree/create-new-decision-tree.module';
import { DecisionTreeOperationService } from './decision-tree-operation.service';
import { DecisionTreeOperationsComponent } from './decision-tree-operations.component';
import { EditDecisionTreeModule } from './edit-decision-tree/edit-decision-tree.module';
import { RemoveDecisionTreeModule } from './remove-decision-tree/remove-decision-tree.module';

@NgModule({
  imports: [
    SharedModule,
    BatchTypeManagementModule,
    RemoveDecisionTreeModule,
    CreateNewDecisionTreeModule,
    EditDecisionTreeModule,
    CopyDecisionTreeModule
  ],
  providers: [
    DecisionTreeOperationService
  ],
  declarations: [
    DecisionTreeOperationsComponent
  ],
  exports: [
    DecisionTreeOperationsComponent
  ]
})
export class DecisionTreeOperationsModule {}
