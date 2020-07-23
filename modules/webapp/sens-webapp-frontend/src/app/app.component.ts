import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';
import { BackgroundServicesManager } from '@app/shared/background-services-manager';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { SessionExpirationDialogComponent } from '@app/shared/security/components/session-expiration-dialog/session-expiration-dialog.component';
import { SessionService } from '@app/shared/security/session.service';
import { TranslateService } from '@ngx-translate/core';
import { DialogService } from '@ui/dialog/services/dialog.service';
import { Observable } from 'rxjs';
import { distinctUntilChanged, map } from 'rxjs/operators';

const TWO_MINUTES: number = 2 * 60;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  showNavigation = false;
  sessionExpireDialog: MatDialogRef<SessionExpirationDialogComponent>;

  sessionTimeoutWarning: Observable<boolean> = this.sessionService.secondsToExpire.pipe(
      map((seconds) => seconds < TWO_MINUTES),
      distinctUntilChanged()
  );

  constructor(
      private translate: TranslateService,
      private backgroundServicesManager: BackgroundServicesManager,
      private localEventService: LocalEventService,
      private sessionService: SessionService,
      private matDialog: MatDialog,
      private dialogService: DialogService
  ) {
    translate.setDefaultLang('en');
    translate.use('en');
    this.sessionTimeoutWarning.subscribe((sessionEnding: boolean) => {
      if (sessionEnding && !this.sessionExpireDialog) {
        this.sessionExpireDialog =
            this.matDialog.open(SessionExpirationDialogComponent, dialogService.defaultConfig);
      } else if (!sessionEnding && this.sessionExpireDialog) {
        this.sessionExpireDialog.close();
        this.sessionExpireDialog = null;
      }
    });
  }

  @HostListener('click', ['$event.target'])
  onClickBtn(target) {
    const onButtonClickEvent = {key: EventKey.CLICK, data: {'target': target}};
    this.localEventService.sendEvent(onButtonClickEvent);
  }

  @HostListener('document:keypress', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
    const onKeyboardClickEvent = {key: EventKey.KEY_PRESS, data: {'event': event}};
    this.localEventService.sendEvent(onKeyboardClickEvent);
  }

  onActivate(event) {
    const scrollToTop = window.setInterval(() => {
      const pos = window.pageYOffset;
      if (pos > 0) {
        window.scrollTo(0, pos - 20);
      } else {
        window.clearInterval(scrollToTop);
      }
    }, 16);
  }

  shouldShowNavigation(showNavigation) {
    this.showNavigation = showNavigation;
  }
}
