import { NgModule } from '@angular/core';
import { SharedModule } from '@app/shared/shared.module';
import { InboxModule } from '@app/templates/inbox/inbox.module';
import { ApplicationHeaderComponent } from './application-header.component';
import { MainNavigationComponent } from './main-navigation/main-navigation.component';
import { UserSectionComponent } from './user-section/user-section.component';

@NgModule({
  imports: [
    SharedModule,
    InboxModule
  ],
  declarations: [
    MainNavigationComponent,
    UserSectionComponent,
    ApplicationHeaderComponent
  ],
  exports: [
    ApplicationHeaderComponent
  ]
})
export class ApplicationHeaderModule {}
