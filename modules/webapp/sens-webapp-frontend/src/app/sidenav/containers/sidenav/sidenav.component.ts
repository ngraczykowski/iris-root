import { Component, OnInit } from '@angular/core';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';

@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.scss']
})
export class SidenavComponent implements OnInit {

  sectionsPrefix = 'sideNav.sections.';

  showNav = false;
  mainNav = [
    {
      name: this.sectionsPrefix + 'reasoningBranches',
      visible: false,
      links: [
        {
          label: 'reasoningBranchBrowser.title',
          url: '/reasoning-branch',
          icon: 'visibility'
        },
        {
          label: 'changeRequest.title',
          url: 'reasoning-branches/change-request',
          icon: 'add_circle'
        }
      ]
    },
    {
      name: this.sectionsPrefix + 'users',
      visible: false,
      links: [
        {
          label: 'usersList.title',
          url: '/user-management',
          icon: 'people'
        }
      ]
    },
    {
      name: this.sectionsPrefix + 'reports',
      visible: false,
      links: [
        {
          label: 'auditTrail.title',
          url: '/reports/audit-trail',
          icon: 'announcement'
        },
        {
          label: 'securityMatrix.title',
          url: '/security-matrix',
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
