import { NgModule } from '@angular/core';
import { SharedModule } from '../../../../shared/shared.module';
import { CreateNewDecisionTreeComponent } from './create-new-decision-tree.component';
import { CreateNewDecisionTreeService } from './create-new-decision-tree.service';

@NgModule({
  imports: [
    SharedModule
  ],
  providers: [
    CreateNewDecisionTreeService
  ],
  declarations: [
    CreateNewDecisionTreeComponent
  ],
  exports: [
    CreateNewDecisionTreeComponent
  ]
})
export class CreateNewDecisionTreeModule {}
