import { ApplicationRef, DoBootstrap, NgModule } from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { RouterModule } from '@angular/router';
import { AppBarModule } from '@app/app-bar/app-bar.module';
import { rolesByRedirect, routes } from '@app/app-routes';
import { AppComponent } from '@app/app.component';
import { AuditTrailModule } from '@app/audit-trail/audit-trail.module';
import { BasicRoleDefaultPageMappings } from '@app/basic-default-paegs-mappings';
import { PendingChangesModule } from '@app/pending-changes/pending-changes.module';
import { ChangeRequestModule } from '@app/change-request/change-request.module';
import { BriefMessageComponent } from '@app/components/brief-message/brief-message.component';
import { ErrorWindowComponent } from '@app/components/communication-error/error-window.component';
import { ExternalComponent } from '@app/layout/external/external.component';
import { AccessDeniedComponent } from '@app/pages/access-denied/access-denied.component';
import { ErrorPageComponent } from '@app/pages/error-page/error-page.component';
import { InternalServerErrorComponent } from '@app/pages/internal-server-error/internal-server-error.component';
import { MaintenanceComponent } from '@app/pages/maintenance/maintenance.component';
import { NotAuthenticatedComponent } from '@app/pages/not-authenticated/not-authenticated.component';
import { PageNotFoundComponent } from '@app/pages/page-not-found/page-not-found.component';
import { KeycloakInitializer } from '@app/shared/security/bootstrap/keycloak-initializer';
import { ROLES_REDIRECT_CONFIG } from '@app/shared/security/role-default-page-mappings';
import { SharedModule } from '@app/shared/shared.module';
import { WINDOW_PROVIDERS } from '@app/shared/window.service';
import { SidenavModule } from '@app/sidenav/sidenav.module';
import { AnalystHomeModule } from '@app/templates/analyst-home/analyst-home.module';
import { SecurityMatrixModule } from '@app/templates/audit-trail/audit-trail.module';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { concat, Observable } from 'rxjs';
import { ReasoningBranchManagementModule } from './reasoning-branch-management/reasoning-branch-management.module';
import { reducers } from './reducers';
import { UserManagementModule } from './user-management/user-management.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';


@NgModule({
  declarations: [
    AppComponent,
    AccessDeniedComponent,
    NotAuthenticatedComponent,
    BriefMessageComponent,
    ErrorWindowComponent,
    PageNotFoundComponent,
    ExternalComponent,
    MaintenanceComponent,
    ErrorPageComponent,
    InternalServerErrorComponent
  ],
  imports: [
    RouterModule.forRoot(routes),
    SharedModule,
    AuditTrailModule,
    AnalystHomeModule,
    StoreModule.forRoot(reducers),
    EffectsModule.forRoot([]),
    StoreDevtoolsModule.instrument({
      maxAge: 10
    }),
    KeycloakAngularModule,
    ReasoningBranchManagementModule,
    AuditTrailModule,
    UserManagementModule,
    BrowserAnimationsModule,
    AppBarModule,
    SidenavModule,
    MatSidenavModule,
    ChangeRequestModule,
    SecurityMatrixModule,
    PendingChangesModule,
    ChangeRequestModule,
  ],
  providers: [
    WINDOW_PROVIDERS,
    {
      provide: KeycloakService,
      useFactory: () => new KeycloakService(),
    },
    {provide: ROLES_REDIRECT_CONFIG, useValue: new BasicRoleDefaultPageMappings(rolesByRedirect)}

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
