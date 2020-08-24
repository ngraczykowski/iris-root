import { ApplicationRef, DoBootstrap, NgModule } from '@angular/core';
import { MatButtonModule, MatDialogModule } from '@angular/material';
import { MatSidenavModule } from '@angular/material/sidenav';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { AppBarModule } from '@app/app-bar/app-bar.module';
import { rolesByRedirect, routes } from '@app/app-routes';
import { AppComponent } from '@app/app.component';
import { AuditTrailModule } from '@app/audit-trail/audit-trail.module';
import { BasicRoleDefaultPageMappings } from '@app/basic-default-paegs-mappings';
import { CircuitBreakerDashboardModule } from '@app/circuit-breaker-dashboard/circuit-breaker-dashboard.module';
import { PendingChangesModule } from '@app/pending-changes/pending-changes.module';
import { ChangeRequestModule } from '@app/change-request/change-request.module';
import { BriefMessageComponent } from '@app/components/brief-message/brief-message.component';
import { ErrorWindowComponent } from '@app/components/communication-error/error-window.component';
import { ReasoningBranchesBrowserModule } from '@app/reasoning-branches-browser/reasoning-branches-browser.module';
import { ReasoningBranchesReportModule } from '@app/reasoning-branches-report/reasoning-branches-report.module';
import { KeycloakInitializer } from '@app/shared/security/bootstrap/keycloak-initializer';
import { ROLES_REDIRECT_CONFIG } from '@app/shared/security/role-default-page-mappings';
import { SharedModule } from '@app/shared/shared.module';
import { WINDOW_PROVIDERS } from '@app/shared/window.service';
import { SidenavModule } from '@app/sidenav/sidenav.module';
import { AnalystHomeModule } from '@app/templates/analyst-home/analyst-home.module';
import { SecurityMatrixModule } from '@app/templates/audit-trail/audit-trail.module';
import { UsersReportModule } from '@app/users-report/users-report.module';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { TranslateModule } from '@ngx-translate/core';
import { AnimationModule } from '@ui/animation/animation.module';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { concat, Observable } from 'rxjs';
import { reducers } from './reducers';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
  declarations: [
    AppComponent,
    BriefMessageComponent,
    ErrorWindowComponent
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
    AuditTrailModule,
    BrowserModule,
    BrowserAnimationsModule,
    AppBarModule,
    SidenavModule,
    MatSidenavModule,
    MatDialogModule,
    MatButtonModule,
    ChangeRequestModule,
    SecurityMatrixModule,
    PendingChangesModule,
    ChangeRequestModule,
    CircuitBreakerDashboardModule,
    ReasoningBranchesReportModule,
    ReasoningBranchesBrowserModule,
    UsersReportModule,
    TranslateModule
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
