import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { ActivityMonitorModule } from '@app/shared/activity-monitor/activity-monitor.module';

import { AuthGuard } from '@app/shared/auth/auth-guard';

import { AuthService } from '@app/shared/auth/auth.service';
import { AuthenticatedUserFacade } from '@app/shared/auth/authenticated-user-facade.service';
import { AuthEffects } from '@app/shared/auth/store/auth.effects';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { KeycloakAngularModule } from 'keycloak-angular';
import { CookieService } from 'ngx-cookie-service';
import { Ng2Webstorage } from 'ngx-webstorage';
import authReducer from './store/auth.reducer';

@NgModule({
  declarations: [],
  imports: [
    Ng2Webstorage.forRoot({prefix: 'sens'}),
    HttpClientModule,
    ActivityMonitorModule,
    KeycloakAngularModule,
    StoreModule.forFeature('auth', authReducer),
    EffectsModule.forFeature([AuthEffects])
  ],
  providers: [
    HttpClientModule,
    AuthService,
    AuthGuard,
    // {provide: BackgroundServiceToken, useClass: AutoLogoutService, multi: true},
    CookieService,
    AuthenticatedUserFacade,
    // {provide: BackgroundServiceToken, useClass: AuthActivityMonitorService, multi: true}
  ]
})
export class AuthModule {}
