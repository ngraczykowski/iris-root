import { NgModule } from '@angular/core';
import { SharedModule } from '@app/shared/shared.module';
import { DecisionTreeOperationsModule } from '../../../decision-tree-operations/decision-tree-operations.module';
import { DecisionTreeAssignmentsViewComponent } from './decision-tree-assignments-view/decision-tree-assignments-view.component';
import { DecisionTreeIdViewComponent } from './decision-tree-id-view/decision-tree-id-view.component';
import { DecisionTreeLabelViewComponent } from './decision-tree-label-view/decision-tree-label-view.component';
import { DecisionTreeNameViewComponent } from './decision-tree-name-view/decision-tree-name-view.component';
import { DecisionTreeOperationsViewComponent } from './decision-tree-operations-view/decision-tree-operations-view.component';
import { DecisionTreeStatusViewComponent } from './decision-tree-status-view/decision-tree-status-view.component';

@NgModule({
  imports: [
    SharedModule,
    DecisionTreeOperationsModule,
  ],
  declarations: [
    DecisionTreeIdViewComponent,
    DecisionTreeNameViewComponent,
    DecisionTreeStatusViewComponent,
    DecisionTreeAssignmentsViewComponent,
    DecisionTreeOperationsViewComponent,
    DecisionTreeLabelViewComponent
  ],
  entryComponents: [
    DecisionTreeIdViewComponent,
    DecisionTreeNameViewComponent,
    DecisionTreeStatusViewComponent,
    DecisionTreeAssignmentsViewComponent,
    DecisionTreeOperationsViewComponent,
    DecisionTreeLabelViewComponent
  ]
})
export class DecisionTreeTableViewsModule {}
