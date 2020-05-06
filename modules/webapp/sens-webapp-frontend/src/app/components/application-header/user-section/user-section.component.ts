import { Component, ElementRef, HostListener, OnDestroy, OnInit } from '@angular/core';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';
import { Principal } from '@app/shared/security/principal.model';
import { Observable, Subscription } from 'rxjs';
import { ApplicationHeaderService } from '../application-header.service';
import { KeycloakProfile } from 'keycloak-js';

@Component({
  selector: 'app-user-section',
  templateUrl: './user-section.component.html',
  styleUrls: ['./user-section.component.scss']
})
export class UserSectionComponent implements OnInit, OnDestroy {

  public userDetails: Observable<KeycloakProfile> = this.authenticatedUser.getPrincipal();
  userMenu = false;
  applicationVersion: string;
  applicationVersionSubscription: Subscription;

  @HostListener('document:click', ['$event'])
  handleMouseClick(event) {
    const clickInsideComponent = this.eRef.nativeElement.contains(event.target);
    if (!clickInsideComponent) {
      this.userMenu = false;
    }
  }

  constructor(
      private eRef: ElementRef,
      private authenticatedUser: AuthenticatedUserFacade,
      private applicationHeaderService: ApplicationHeaderService
  ) {}

  ngOnInit() {
    this.applicationVersionSubscription = this.applicationHeaderService.getAppInfo().subscribe((data) => {
      this.applicationVersion = data.git.build.version;
    });
  }

  ngOnDestroy() {
    this.applicationVersionSubscription.unsubscribe();
  }

  toggleUserMenu() {
    this.userMenu = !this.userMenu;
  }

  logout(): void {
    this.authenticatedUser.logout();
  }

}
