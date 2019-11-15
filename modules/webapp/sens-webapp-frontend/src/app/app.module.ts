import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { routes } from '@app/app-routes';
import { AppComponent } from '@app/app.component';
import { ApplicationHeaderModule } from '@app/components/application-header/application-header.module';
import { BriefMessageComponent } from '@app/components/brief-message/brief-message.component';
import { ErrorWindowComponent } from '@app/components/communication-error/error-window.component';
import { ExtendSessionComponent } from '@app/components/extend-session/extend-session.component';
import { ExternalComponent } from '@app/layout/external/external.component';
import { InternalComponent } from '@app/layout/internal/internal.component';
import { AccessDeniedComponent } from '@app/pages/access-denied/access-denied.component';
import { ErrorPageComponent } from '@app/pages/error-page/error-page.component';
import { InternalServerErrorComponent } from '@app/pages/internal-server-error/internal-server-error.component';
import { MaintenanceComponent } from '@app/pages/maintenance/maintenance.component';
import { PageNotFoundComponent } from '@app/pages/page-not-found/page-not-found.component';
import { SharedModule } from '@app/shared/shared.module';
import { WINDOW_PROVIDERS } from '@app/shared/window.service';
import { AlertRestrictionsManagementModule } from '@app/templates/alert-restrictions-management/alert-restrictions-management.module';
import { AlertModule } from '@app/templates/alert/alert.module';
import { AnalystHomeModule } from '@app/templates/analyst-home/analyst-home.module';
import { ApproverModule } from '@app/templates/approver/approver.module';
import { AuditTrailModule } from '@app/templates/audit-trail/audit-trail.module';
import { DecisionTreeModule } from '@app/templates/decision-tree/decision-tree.module';
import { InboxModule } from '@app/templates/inbox/inbox.module';
import { ReasoningBranchModule } from '@app/templates/reasoning-branch/reasoning-branch.module';
import { UserManagementModule } from '@app/templates/user-management/user-management.module';
import { WorkflowManagementModule } from '@app/templates/workflow-management/workflow-management.module';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { reducers } from './reducers';

@NgModule({
  declarations: [
    AppComponent,
    AccessDeniedComponent,
    BriefMessageComponent,
    ExtendSessionComponent,
    ErrorWindowComponent,
    PageNotFoundComponent,
    InternalComponent,
    ExternalComponent,
    MaintenanceComponent,
    ErrorPageComponent,
    InternalServerErrorComponent
  ],
  imports: [
    RouterModule.forRoot(routes),
    SharedModule,
    ApplicationHeaderModule,
    DecisionTreeModule,
    ReasoningBranchModule,
    AlertModule,
    InboxModule,
    UserManagementModule,
    AuditTrailModule,
    AnalystHomeModule,
    WorkflowManagementModule,
    AlertRestrictionsManagementModule,
    ApproverModule,
    StoreModule.forRoot(reducers),
    EffectsModule.forRoot([]),
    StoreDevtoolsModule.instrument({
      maxAge: 10
    })
  ],
  providers: [WINDOW_PROVIDERS],
  bootstrap: [AppComponent]
})
export class AppModule {}
