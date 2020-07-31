import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { APP_INITIALIZER, NgModule, Type } from '@angular/core';
import { MatButtonModule, MatDialogModule } from '@angular/material';
import { ActivityMonitorModule } from '@app/shared/activity-monitor/activity-monitor.module';
import { AuthenticationGuard } from '@app/shared/security/guard/authentication-guard.service';
import { AuthorityGuard } from '@app/shared/security/guard/authority-guard.service';
import { AuthService } from '@app/shared/security/auth.service';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';
import { SessionService } from '@app/shared/security/session.service';
import { SecurityEffects } from '@app/shared/security/store/security-effects.service';
import { SECURITY_STATE_NAME } from '@app/shared/security/store/security.state';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';
import { KeycloakAngularModule } from 'keycloak-angular';
import { Ng2Webstorage } from 'ngx-webstorage';
import * as fromSecurity from './store/security.reducer';
import { SessionExpirationDialogComponent } from './components/session-expiration-dialog/session-expiration-dialog.component';

export function sessionInit(sessionService: SessionService) {
  return () => sessionService.init();
}

const dialogs: Type<any>[] = [SessionExpirationDialogComponent];

// @dynamic
@NgModule({
  declarations: [...dialogs],
  exports: [...dialogs],
  imports: [
    CommonModule,
    Ng2Webstorage.forRoot({prefix: 'sens'}),
    HttpClientModule,
    ActivityMonitorModule,
    KeycloakAngularModule,
    MatDialogModule,
    MatButtonModule,
    StoreModule.forFeature(SECURITY_STATE_NAME, fromSecurity.reducer),
    EffectsModule.forFeature([SecurityEffects]),
    TranslateModule,
  ],
  providers: [
    HttpClientModule,
    AuthService,
    AuthenticationGuard,
    AuthorityGuard,
    SessionService,
    {provide: APP_INITIALIZER, useFactory: sessionInit, deps: [SessionService], multi: true},
    AuthenticatedUserFacade,
  ],
  entryComponents: [...dialogs]
})
export class AuthModule {}
