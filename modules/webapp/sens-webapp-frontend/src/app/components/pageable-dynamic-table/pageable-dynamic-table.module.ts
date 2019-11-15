import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { PageRowCountSelectorComponent } from './page-row-count-selector/page-row-count-selector.component';
import { PageSelectorComponent } from './page-selector/page-selector.component';
import { PageableDynamicTableComponent } from './pageable-dynamic-table.component';

@NgModule({
  imports: [
    SharedModule
  ],
  declarations: [
    PageSelectorComponent,
    PageRowCountSelectorComponent,
    PageableDynamicTableComponent
  ],
  exports: [
    PageableDynamicTableComponent
  ]
})
export class PageableDynamicTableModule {}
