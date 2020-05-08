import { Component, OnInit } from '@angular/core';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';

@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.scss']
})
export class SidenavComponent implements OnInit {

  showNav = false;
  mainNav = [
    {
      name: 'reasoningBranches.title',
      visible: false,
      links: [
        {
          label: 'reasoningBranches.changeRequest.title',
          url: '/reasoning-branch',
          icon: 'visibility'
        }
      ]
    },
    {
      name: 'users.title',
      visible: false,
      links: [
        {
          label: 'users.usersList.title',
          url: '/user-management',
          icon: 'people'
        }
      ]
    },
    {
      name: 'reports.title',
      visible: false,
      links: [
        {
          label: 'auditTrail.title',
          url: '/reports',
          icon: 'lock'
        }
      ]
    }
  ];

  constructor(
      private authenticatedUser: AuthenticatedUserFacade
  ) { }

  ngOnInit() {
    this.generateNavigation();
  }

  generateNavigation() {
    this.mainNav.forEach((section) => {
      section.links.forEach((link, linkIndex) => {
        if (this.authenticatedUser.checkUrlAccess(link.url)) {
          section.visible = true;
        } else {
          section.links.splice(linkIndex, 1);
        }
      });
    });
    this.showNav = true;
  }
}
