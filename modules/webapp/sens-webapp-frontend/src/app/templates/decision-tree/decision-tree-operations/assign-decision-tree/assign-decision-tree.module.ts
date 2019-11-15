import { NgModule } from '@angular/core';
import { SharedModule } from '../../../../shared/shared.module';
import { AssignDecisionTreeComponent } from './assign-decision-tree.component';
import { AssignedToService } from './assigned-to.service';
import { AssignedToStore } from './assigned-to.store';

@NgModule({
  imports: [
    SharedModule
  ],
  providers: [
    AssignedToStore,
    AssignedToService
  ],
  declarations: [
    AssignDecisionTreeComponent
  ],
  exports: [
    AssignDecisionTreeComponent
  ]
})
export class AssignDecisionTreeModule {}
