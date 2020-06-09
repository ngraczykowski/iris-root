import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';

@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html'
})
export class SidenavComponent implements OnInit {

  @Output() showNavigation = new EventEmitter();

  sectionsPrefix = 'sideNav.sections.';

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
          url: '/reasoning-branches/change-request',
          icon: 'add_circle'
        },
        {
          label: 'pendingChanges.title',
          url: '/reasoning-branches/pending-changes',
          icon: 'view_list'
        }
      ]
    },
    {
      name: this.sectionsPrefix + 'users',
      visible: false,
      links: [
        {
          label: 'usersList.title',
          url: '/users/user-management',
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
          label: 'reasoningBranchesReport.title',
          url: '/reports/reasoning-branches-report',
          icon: 'announcement'
        },
        {
          label: 'securityMatrix.title',
          url: '/reports/security-matrix',
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
          this.showNavigation.emit(true);
        } else {
          section.links.splice(linkIndex, 1);
        }
      });
    });
  }
}
