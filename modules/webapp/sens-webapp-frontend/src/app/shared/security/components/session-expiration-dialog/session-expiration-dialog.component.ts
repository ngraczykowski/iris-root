import { ChangeDetectorRef, Component, OnDestroy } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { SessionService } from '@app/shared/security/session.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-session-expiration-dialog',
  templateUrl: './session-expiration-dialog.component.html'
})
export class SessionExpirationDialogComponent implements OnDestroy {

  public minutes: string;
  public seconds: string;

  sessionExpireTimeSub: Subscription;

  constructor(private matDialogRef: MatDialogRef<SessionExpirationDialogComponent>,
              private sessionService: SessionService,
              cdr: ChangeDetectorRef) {
    this.sessionExpireTimeSub = this.sessionService.secondsToExpire.subscribe((seconds: number) => {
      this.minutes = String(Math.floor(seconds / 60)).padStart(2, '0');
      this.seconds = String(Math.floor(seconds % 60)).padStart(2, '0');
      cdr.markForCheck();
    });
  }

  extend(): void {
    this.sessionService.extendSession();
  }

  logout(): void {
    this.matDialogRef.close();
    this.sessionService.invalidateSession();
  }

  ngOnDestroy(): void {
    this.sessionExpireTimeSub.unsubscribe();
  }

}
