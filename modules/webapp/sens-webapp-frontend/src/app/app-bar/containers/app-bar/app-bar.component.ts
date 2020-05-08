import { Component, OnInit } from '@angular/core';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';
import { AppBarService } from '../../services/app-bar.service';
import { KeycloakProfile } from 'keycloak-js';
import { Observable, Subscription  } from 'rxjs';

@Component({
  selector: 'app-app-bar',
  templateUrl: './app-bar.component.html',
  styleUrls: ['./app-bar.component.scss']
})
export class AppBarComponent implements OnInit {
  public userDetails: Observable<KeycloakProfile> = this.authenticatedUser.getPrincipal();
  applicationVersion: string;
  applicationVersionSubscription: Subscription;

  constructor(
    private authenticatedUser: AuthenticatedUserFacade,
    private applicationBarService: AppBarService
  ) { }

  displayUserDetails(userData) {
    if (userData.firstName || userData.lastName) {
      return {
        userName: userData.username,
        displayName: userData.firstName + ' ' + userData.lastName
      };
    } else {
      return {
        userName: userData.username,
        displayName: userData.username
      };
    }
  }

  logout(): void {
    this.authenticatedUser.logout();
  }

  ngOnInit() {
    this.applicationVersionSubscription = this.applicationBarService.getAppInfo().subscribe((data) => {
      this.applicationVersion = data.git.build.version;
    });
  }

}
