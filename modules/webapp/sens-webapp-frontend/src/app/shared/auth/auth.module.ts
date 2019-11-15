import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { ActivityMonitorModule } from '@app/shared/activity-monitor/activity-monitor.module';

import { AccessGuard } from '@app/shared/auth/access-guard';
import { AuthActivityMonitorService } from '@app/shared/auth/auth-activity-monitor.service';
import { AuthGuard } from '@app/shared/auth/auth-guard';

import { AuthService } from '@app/shared/auth/auth.service';
import { AutoLogoutService } from '@app/shared/auth/auto-logout.service';

import { StartPageGuard } from '@app/shared/auth/start-page-guard';
import { BackgroundServiceToken } from '@app/shared/background-services-manager';
import { CookieService } from 'ngx-cookie-service';
import { Ng2Webstorage } from 'ngx-webstorage';

@NgModule({
  declarations: [],
  imports: [
    Ng2Webstorage.forRoot({prefix: 'sens'}),
    HttpClientModule,
    ActivityMonitorModule
  ],
  providers: [
    HttpClientModule,
    AuthService,
    StartPageGuard,
    AuthGuard,
    AccessGuard,
    {provide: BackgroundServiceToken, useClass: AutoLogoutService, multi: true},
    CookieService,
    {provide: BackgroundServiceToken, useClass: AuthActivityMonitorService, multi: true}
  ]
})
export class AuthModule {}
