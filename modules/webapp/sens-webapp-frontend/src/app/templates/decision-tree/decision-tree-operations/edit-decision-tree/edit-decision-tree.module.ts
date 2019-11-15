import { NgModule } from '@angular/core';
import { SharedModule } from '../../../../shared/shared.module';
import { EditDecisionTreeComponent } from './edit-decision-tree.component';
import { EditDecisionTreeService } from './edit-decision-tree.service';

@NgModule({
  imports: [
    SharedModule
  ],
  providers: [
    EditDecisionTreeService
  ],
  declarations: [
    EditDecisionTreeComponent
  ],
  exports: [
    EditDecisionTreeComponent
  ]
})
export class EditDecisionTreeModule {}
