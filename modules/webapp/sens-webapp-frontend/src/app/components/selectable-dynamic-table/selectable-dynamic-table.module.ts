import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { PageableDynamicTableModule } from '../pageable-dynamic-table/pageable-dynamic-table.module';
import { SelectableDynamicTableComponent } from './selectable-dynamic-table.component';
import { SelectAllViewComponent } from './views/select-all-view/select-all-view.component';
import { SelectOneViewComponent } from './views/select-one-view/select-one-view.component';

@NgModule({
  imports: [
    SharedModule,
    PageableDynamicTableModule
  ],
  declarations: [
    SelectOneViewComponent,
    SelectAllViewComponent,
    SelectableDynamicTableComponent
  ],
  entryComponents: [
    SelectOneViewComponent,
    SelectAllViewComponent
  ],
  exports: [
    SelectableDynamicTableComponent
  ]
})
export class SelectableDynamicTableModule {}
