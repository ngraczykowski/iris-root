import { Component, ElementRef, OnInit } from '@angular/core';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';

@Component({
  selector: 'app-access-denied',
  templateUrl: './access-denied.component.html',
  styleUrls: ['./access-denied.component.scss']
})
export class AccessDeniedComponent implements OnInit {

  constructor(private eRef: ElementRef, private authenticatedUser: AuthenticatedUserFacade) { }

  ngOnInit() {
  }

  logout() {
    this.authenticatedUser.logout();
  }
}
