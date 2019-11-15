import { Injectable, InjectionToken } from '@angular/core';
import { Router } from '@angular/router';
import { LastUserActivityStorage } from '@app/shared/activity-monitor/last-user-activity-storage/last-user-activity-storage.model';
import { AuthService } from '@app/shared/auth/auth.service';
import { BackgroundService } from '@app/shared/background-services-manager';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { environment } from '@env/environment';
import { interval, Subscription } from 'rxjs';

export const AuthActivityMonitorServiceToken = new InjectionToken<number>('inactivityTimeToLogout');

class AuthActivityMonitorConfiguration {
  inactivitySecondsToLogout = environment.auth.activityMonitor.inactivityTimeToLogoutInSec;
  inactivitySecondsToNotify = environment.auth.activityMonitor.inactivityTimeToDisplayNotificationInSec;
  poolingMilliseconds = environment.auth.activityMonitor.activityPoolingTimeInMillis;
}

@Injectable()
export class AuthActivityMonitorService implements BackgroundService {

  private readonly config = new AuthActivityMonitorConfiguration();

  private subscriptions: Subscription[] = [];

  private hasSentCloseToExpireMessage = false;

  constructor(
      private lastUserActivityStorage: LastUserActivityStorage,
      private authService: AuthService,
      private router: Router,
      private eventService: LocalEventService) {
  }

  private static getCurrentTimeInMilliseconds(): number {
    return new Date().getTime();
  }

  startService(): void {
    this.subscriptions.push(
        interval(this.config.poolingMilliseconds)
            .subscribe(() => this.checkActivity())
    );
  }

  stopService(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }

  private checkActivity() {
    if (this.authService.isLoggedIn()) {
      if (this.isSessionExpired()) {
        this.eventService.sendEvent({
          key: EventKey.AUTO_LOGOUT,
          data: {reasonKey: 'logout.notification.reason.USER_INACTIVITY'}
        });
      } else if (this.isSessionCloseToExpire() && !this.hasSentCloseToExpireMessage) {
        this.eventService.sendEvent({
          key: EventKey.SESSION_CLOSE_TO_EXPIRE,
          data: {expirationTime: this.calculateSessionExpirationTime()}
        });
        this.hasSentCloseToExpireMessage = true;
      }
    }

    if (!this.isSessionCloseToExpire() && this.hasSentCloseToExpireMessage) {
      this.hasSentCloseToExpireMessage = false;
    }
  }

  private calculateSessionExpirationTime() {
    return this.getLastActivityTime() + this.config.inactivitySecondsToLogout * 1000;
  }

  private isSessionExpired() {
    const expirationTime = this.getLastActivityTime() + this.config.inactivitySecondsToLogout * 1000;
    return AuthActivityMonitorService.getCurrentTimeInMilliseconds() > expirationTime;
  }

  private isSessionCloseToExpire() {
    const closeExpirationTime = this.getLastActivityTime() + this.config.inactivitySecondsToNotify * 1000;
    return AuthActivityMonitorService.getCurrentTimeInMilliseconds() > closeExpirationTime;
  }

  private getLastActivityTime() {
    return this.lastUserActivityStorage.getLastUserActivityTime();
  }
}
