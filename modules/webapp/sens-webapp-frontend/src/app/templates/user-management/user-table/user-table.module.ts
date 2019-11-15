import { NgModule } from '@angular/core';
import { PageableDynamicTableModule } from '../../../components/pageable-dynamic-table/pageable-dynamic-table.module';
import { SharedModule } from '../../../shared/shared.module';
import { UserTableDataMapper, UserTableDataProvider } from './user-table-data-provider';
import { UserTableComponent } from './user-table.component';
import { UserViewModule } from './views/user-view.module';

@NgModule({
  imports: [
    SharedModule,
    UserViewModule,
    PageableDynamicTableModule
  ],
  declarations: [
    UserTableComponent
  ],
  providers: [
    UserTableDataMapper,
    UserTableDataProvider
  ],
  exports: [
    UserTableComponent
  ]
})
export class UserTableModule {}
