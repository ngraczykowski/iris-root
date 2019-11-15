import { Component, ElementRef, HostListener, OnDestroy, OnInit } from '@angular/core';
import { AuthService } from '@app/shared/auth/auth.service';
import { Subscription } from 'rxjs';
import { ApplicationHeaderService } from '../application-header.service';

@Component({
  selector: 'app-user-section',
  templateUrl: './user-section.component.html',
  styleUrls: ['./user-section.component.scss']
})
export class UserSectionComponent implements OnInit, OnDestroy {

  public usernamePrimary;
  public usernameSecondary;
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
    private authService: AuthService,
    private applicationHeaderService: ApplicationHeaderService
  ) {}

  ngOnInit() {
    this.loadUsername();
    this.applicationVersionSubscription = this.applicationHeaderService.getAppInfo().subscribe((data) => {
      this.applicationVersion = data.build.version;
    });

  }

  loadUsername() {
    const userName = this.authService.getUserName();
    const displayName = this.authService.getDisplayName();

    if (displayName) {
      this.usernamePrimary = displayName;
      this.usernameSecondary = userName;
    } else {
      this.usernamePrimary = userName;
    }
  }

  ngOnDestroy() {
    this.applicationVersionSubscription.unsubscribe();
  }

  toggleUserMenu() {
    this.userMenu = !this.userMenu;
  }

  logout(): void {
    this.authService.logout();
  }

}
