import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { HintFeedbackModule } from '@app/components/hint-feedback/hint-feedback.module';
import { InputModule } from '@app/components/input/input.module';
import { SharedModule } from '@app/shared/shared.module';
import { RestrictionPanelEditorComponent } from './restriction-panel-editor/restriction-panel-editor.component';
import { RestrictionPanelUsersManagementComponent } from './restriction-panel-editor/restriction-panel-users-management/restriction-panel-users-management.component';
import { RestrictionPanelComponent } from './restriction-panel.component';

@NgModule({
  declarations: [
    RestrictionPanelComponent,
    RestrictionPanelEditorComponent,
    RestrictionPanelUsersManagementComponent,
  ],
  exports: [
    RestrictionPanelComponent
  ],
  imports: [
    CommonModule,
    InputModule,
    SharedModule,
    HintFeedbackModule,
  ]
})
export class RestrictionPanelModule {}
