import { Location } from '@angular/common';
import { ChangeDetectionStrategy, Component, OnDestroy } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { AuthService } from '../../../shared/security/auth.service';
import { AuthenticatedUserFacade } from '../../../shared/security/authenticated-user-facade.service';
import { Observable } from 'rxjs';
import { map, shareReplay, tap } from 'rxjs/operators';

enum WarningPageType {
  E403 = '403',
  E401 = '401',
  MAINTENANCE = 'maintenance',
  E500 = '500',
  DEFAULT = 'default'
}

@Component({
  selector: 'app-warnings-page',
  templateUrl: './warnings-page.component.html',
  styleUrls: ['./warnings-page.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class WarningsPageComponent implements OnDestroy {

  types: typeof WarningPageType = WarningPageType;

  type: Observable<string> = this.activatedRoute.params.pipe(
      map((params: Params) => params && params.type ? params.type : null),
      map((type: WarningPageType) =>
          [ WarningPageType.E403 ,
            WarningPageType.E401,
            WarningPageType.MAINTENANCE,
            WarningPageType.E500].includes(type) ? type : WarningPageType.DEFAULT),
      tap((type: string) => this.scheduleReload(type)),
      shareReplay()
  );

  private timeout;

  constructor(private activatedRoute: ActivatedRoute,
              private router: Router,
              private nativeLocation: Location,
              private authenticatedUser: AuthenticatedUserFacade,
              private authService: AuthService) { }

  scheduleReload(type: string): void {
    if (type === 'maintenance') {
      const minute: number = 60 * 1000;
      this.timeout = setTimeout(() => {
        this.router.navigate(['/']);
      }, minute);
    }
  }

  back(): void {
    this.nativeLocation.back();
  }

  logout(): void {
    this.authenticatedUser.logout();
  }

  login(): void {
    this.authService.login();
  }

  ngOnDestroy(): void {
    if (this.timeout) {
      clearTimeout(this.timeout);
    }
  }


}
