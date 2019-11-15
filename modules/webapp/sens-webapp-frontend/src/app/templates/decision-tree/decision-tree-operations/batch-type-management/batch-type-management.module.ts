import { NgModule } from '@angular/core';
import { SharedModule } from '@app/shared/shared.module';
import { BatchTypeListElementComponent } from './batch-type-list/batch-type-list-element/batch-type-list-element.component';
import { BatchTypeListComponent } from './batch-type-list/batch-type-list.component';
import { BatchTypeManagementComponent } from './batch-type-management.component';
import { BatchTypeManagementService } from './batch-type-management.service';
import { BatchTypeManagementStore } from './batch-type-management.store';

@NgModule({
  imports: [
    SharedModule
  ],
  providers: [
    BatchTypeManagementStore,
    BatchTypeManagementService
  ],
  declarations: [
    BatchTypeManagementComponent,
    BatchTypeListComponent,
    BatchTypeListElementComponent
  ],
  exports: [
    BatchTypeManagementComponent
  ]
})
export class BatchTypeManagementModule {}
