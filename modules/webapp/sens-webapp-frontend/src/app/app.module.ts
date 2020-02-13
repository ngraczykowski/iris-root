import { ApplicationRef, DoBootstrap, NgModule } from '@angular/core';
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
import { NotAuthenticatedComponent } from '@app/pages/not-authenticated/not-authenticated.component';
import { PageNotFoundComponent } from '@app/pages/page-not-found/page-not-found.component';
import { KeycloakInitializer } from '@app/shared/security/bootstrap/keycloak-initializer';
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
import { WorkflowManagementModule } from '@app/templates/workflow-management/workflow-management.module';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { concat, Observable } from 'rxjs';
import { reducers } from './reducers';
import { ReasoningBranchManagementModule } from './reasoning-branch-management/reasoning-branch-management.module';
import { UserManagementModule } from './user-management/user-management.module';


@NgModule({
  declarations: [
    AppComponent,
    AccessDeniedComponent,
    NotAuthenticatedComponent,
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
    AuditTrailModule,
    AnalystHomeModule,
    WorkflowManagementModule,
    AlertRestrictionsManagementModule,
    ApproverModule,
    StoreModule.forRoot(reducers),
    EffectsModule.forRoot([]),
    StoreDevtoolsModule.instrument({
      maxAge: 10
    }),
    KeycloakAngularModule,
    ReasoningBranchManagementModule,
    UserManagementModule
  ],
  providers: [
    WINDOW_PROVIDERS,
    {
      provide: KeycloakService,
      useFactory: () => new KeycloakService(),
    }

  ],
  entryComponents: [AppComponent]
})
export class AppModule implements DoBootstrap {

  constructor(private readonly keycloakService: KeycloakService) {
  }

  ngDoBootstrap(appRef: ApplicationRef): void {
    concat(
        new KeycloakInitializer(this.keycloakService).doInitialize(),
        this.bootstrapApp(appRef)
    ).subscribe({
      error: error => console.error('[ngDoBootstrap] init failed', error),
      complete: () => console.log('[ngDoBootstrap] completed')
    });
  }
  private bootstrapApp(appRef: ApplicationRef): Observable<void> {
    return new Observable(obs => {
      console.log('[ngDoBootstrap] bootstraping AppComponent');
      appRef.bootstrap(AppComponent);
      obs.complete();
    });
  }
}
