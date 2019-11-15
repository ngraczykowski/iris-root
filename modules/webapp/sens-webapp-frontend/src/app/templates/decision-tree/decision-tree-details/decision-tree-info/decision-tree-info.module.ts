import { NgModule } from '@angular/core';
import { SharedModule } from '@app/shared/shared.module';
import { DecisionTreeBatchTypesListComponent } from './decision-tree-info-assignments/decision-tree-batch-types-list/decision-tree-batch-types-list.component';
import { DecisionTreeInfoAssignmentsComponent } from './decision-tree-info-assignments/decision-tree-info-assignments.component';
import { DecisionTreeInfoDetailsComponent } from './decision-tree-info-details/decision-tree-info-details.component';
import { DecisionTreeInfoFeaturesListComponent } from './decision-tree-info-features/decision-tree-info-features-list/decision-tree-info-features-list.component';
import { DecisionTreeInfoFeaturesComponent } from './decision-tree-info-features/decision-tree-info-features.component';
import { DecisionTreeInfoComponent } from './decision-tree-info.component';

@NgModule({
  imports: [
    SharedModule,
  ],
  declarations: [
    DecisionTreeInfoComponent,
    DecisionTreeInfoDetailsComponent,
    DecisionTreeInfoFeaturesComponent,
    DecisionTreeInfoAssignmentsComponent,
    DecisionTreeInfoFeaturesListComponent,
    DecisionTreeInfoAssignmentsComponent,
    DecisionTreeBatchTypesListComponent
  ],
  exports: [
    DecisionTreeInfoComponent
  ]
})
export class DecisionTreeInfoModule {}
