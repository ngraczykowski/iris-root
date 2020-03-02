import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { ActivityMonitorModule } from '@app/shared/activity-monitor/activity-monitor.module';
import { AuthenticationGuard } from '@app/shared/security/guard/authentication-guard.service';

import { AuthorityGuard } from '@app/shared/security/guard/authority-guard.service';

import { AuthService } from '@app/shared/security/auth.service';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';
import { SecurityEffects } from '@app/shared/security/store/security-effects.service';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { KeycloakAngularModule } from 'keycloak-angular';
import { Ng2Webstorage } from 'ngx-webstorage';
import * as fromSecurity from './store/security.reducer';

@NgModule({
  declarations: [],
  imports: [
    Ng2Webstorage.forRoot({prefix: 'sens'}),
    HttpClientModule,
    ActivityMonitorModule,
    KeycloakAngularModule,
    StoreModule.forFeature('security', fromSecurity.reducer),
    EffectsModule.forFeature([SecurityEffects])
  ],
  providers: [
    HttpClientModule,
    AuthService,
    AuthenticationGuard,
    AuthorityGuard,
    // {provide: BackgroundServiceToken, useClass: AutoLogoutService, multi: true},
    AuthenticatedUserFacade,
    // {provide: BackgroundServiceToken, useClass: AuthActivityMonitorService, multi: true}
  ]
})
export class AuthModule {}
