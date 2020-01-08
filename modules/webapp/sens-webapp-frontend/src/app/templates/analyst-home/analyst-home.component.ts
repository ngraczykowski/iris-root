import { Component, OnInit } from '@angular/core';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';

@Component({
  selector: 'app-analyst-home',
  templateUrl: './analyst-home.component.html',
  styleUrls: ['./analyst-home.component.scss']
})
export class AnalystHomeComponent implements OnInit {

  constructor(private authenticatedUser: AuthenticatedUserFacade) { }

  ngOnInit() {
    setTimeout('window.location.reload();', 600000);
  }

  logout(): void {
    this.authenticatedUser.logout();
  }

}
