import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';
import { Observable, of } from 'rxjs';

@Component({
  selector: 'app-main-navigation',
  templateUrl: './main-navigation.component.html',
  styleUrls: ['./main-navigation.component.scss']
})
export class MainNavigationComponent implements OnInit {

  showMenu: boolean;

  @HostListener('document:click', ['$event'])
  handleMouseClick(event) {
    const clickInsideComponent = this.eRef.nativeElement.contains(event.target);
    if (!clickInsideComponent) {
      this.showMenu = false;
    }
  }

  constructor(private eRef: ElementRef, private authenticatedUser: AuthenticatedUserFacade) { }

  ngOnInit() { }

  logout(): void {
    this.authenticatedUser.logout();
  }

  hasAccessToUrl(url: string): Observable<boolean> {
    return this.authenticatedUser.hasAccessToUrl(url);
  }
}
