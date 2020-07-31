import { Injectable } from '@angular/core';
import {
  SetSessionExpireTime
} from '@app/shared/security/store/security.actions';
import { StateWithSecurity } from '@app/shared/security/store/security.state';
import { Store } from '@ngrx/store';
import { TranslateService } from '@ngx-translate/core';
import { DialogService } from '@ui/dialog/services/dialog.service';
import { KeycloakService } from 'keycloak-angular';
import { interval, Observable } from 'rxjs';
import {
  distinctUntilChanged,
  filter,
  map, mapTo, shareReplay,
  switchMap, tap,
} from 'rxjs/operators';

const MAX_SESSION_LENGTH: number = 9999999999;
const SECOND: number = 1000;

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  constructor(private keycloakService: KeycloakService,
              private translateSerivce: TranslateService,
              private dialogService: DialogService,
              private store: Store<StateWithSecurity>) {}

  private secondsToExpirePipe: Observable<number> = this.getSessionExpireTime()
    .pipe(
      switchMap((expTime: number) => interval(SECOND).pipe(mapTo(expTime)))
  ).pipe(
    map((secondsLeft: number) => this.calculateSecondsLeft(secondsLeft)),
    tap((secondsLeft) => secondsLeft < 1 && this.invalidateSession()),
    filter((secondsLeft) => secondsLeft > 0),
    shareReplay({ bufferSize: 1, refCount: true })
  );

  public get secondsToExpire(): Observable<number> {
    return this.secondsToExpirePipe;
  }

  public extendSession(): void {
    this.keycloakService.updateToken(MAX_SESSION_LENGTH);
  }

  public invalidateSession(): void {
    this.keycloakService.logout();
  }

  public getSessionExpireTime(checkingTreshold: number = SECOND): Observable<number> {
    return interval(checkingTreshold).pipe(
      map(() => this.keycloakService.getKeycloakInstance()),
      filter(Boolean),
      map((sessionInstance: Keycloak.KeycloakInstance) => sessionInstance.tokenParsed
          ? sessionInstance.refreshTokenParsed.exp * SECOND : 0 ),
      distinctUntilChanged()
    );
  }

  public init(): Promise<any> {
    this.getSessionExpireTime().subscribe((time: number) => {
      this.store.dispatch(new SetSessionExpireTime(time));
    });
    return Promise.resolve();
  }

  private calculateSecondsLeft(secondsLeft: number): number {
    const now: number = new Date().getTime();
    return (secondsLeft - now) / SECOND;
  }

}
