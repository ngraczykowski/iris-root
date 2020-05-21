import { Component, OnInit, OnDestroy } from '@angular/core';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';
import { AppBarService } from '../../services/app-bar.service';
import { KeycloakProfile } from 'keycloak-js';
import { Observable, Subscription  } from 'rxjs';

@Component({
  selector: 'app-app-bar',
  templateUrl: './app-bar.component.html',
  styleUrls: ['./app-bar.component.scss']
})
export class AppBarComponent implements OnInit, OnDestroy {
  applicationVersion: string;
  subscriptions: Subscription[] = [];
  userDetails;

  constructor(
    private authenticatedUser: AuthenticatedUserFacade,
    private applicationBarService: AppBarService
  ) { }

  ngOnInit() {
    this.subscriptions.push(
      this.applicationBarService.getAppInfo().subscribe((data) => {
        this.applicationVersion = data.git.build.version;
      })
    );

    this.getUserDetails();
  }

  ngOnDestroy() {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }

  getUserDetails() {
    this.subscriptions.push(
      this.authenticatedUser.getPrincipal()
        .subscribe(userData => {
          if (userData.firstName && userData.lastName) {
            this.userDetails = {
              userName: userData.username,
              displayName: `${userData.firstName} ${userData.lastName}`
            };
          } else {
            this.userDetails = {
              userName: userData.username,
              displayName: userData.username
            };
          }
      })
    );
  }

  logout(): void {
    this.authenticatedUser.logout();
  }
}
