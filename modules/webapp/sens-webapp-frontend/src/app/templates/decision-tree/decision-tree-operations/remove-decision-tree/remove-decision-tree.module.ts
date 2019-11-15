import { NgModule } from '@angular/core';
import { SharedModule } from '../../../../shared/shared.module';
import { RemoveDecisionTreeComponent } from './remove-decision-tree.component';
import { RemoveDecisionTreeService } from './remove-decision-tree.service';

@NgModule({
  imports: [
    SharedModule
  ],
  providers: [
    RemoveDecisionTreeService
  ],
  declarations: [
    RemoveDecisionTreeComponent
  ],
  exports: [
    RemoveDecisionTreeComponent
  ]

})
export class RemoveDecisionTreeModule {}
