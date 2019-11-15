import { NgModule } from '@angular/core';
import { PageableDynamicTableModule } from '../../../components/pageable-dynamic-table/pageable-dynamic-table.module';
import { SelectableDynamicTableModule } from '../../../components/selectable-dynamic-table/selectable-dynamic-table.module';
import { SharedModule } from '../../../shared/shared.module';
import { BranchFilterModule } from '../branch-filter/branch-filter.module';
import { BranchPageClient } from './branch-page-client';
import { BranchTableLoaderSettingsComponent } from './branch-table-loader-settings/branch-table-loader-settings.component';
import { SelectableBranchTableComponent } from './selectable-branch-table/selectable-branch-table.component';
import { SimpleBranchTableComponent } from './simple-branch-table/simple-branch-table.component';
import { BranchViewsModule } from './views/branch-views.module';
import { BranchLimitInfoComponent } from './branch-table-loader-settings/branch-limit-info/branch-limit-info.component';

@NgModule({
  imports: [
    SharedModule,
    SelectableDynamicTableModule,
    PageableDynamicTableModule,
    BranchFilterModule,
    BranchViewsModule
  ],
  providers: [
    BranchPageClient
  ],
  declarations: [
    SelectableBranchTableComponent,
    SimpleBranchTableComponent,
    BranchTableLoaderSettingsComponent,
    BranchLimitInfoComponent
  ],
  exports: [
    SelectableBranchTableComponent,
    SimpleBranchTableComponent,
    BranchTableLoaderSettingsComponent
  ]
})
export class BranchTableModule {}
