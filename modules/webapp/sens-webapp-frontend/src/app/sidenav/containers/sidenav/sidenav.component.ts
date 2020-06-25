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
      name: this.sectionsPrefix + 'adminPanel',
      visible: false,
      links: [
        {
          label: 'usersList.title',
          url: '/users/user-management',
          icon: 'people_outline'
        }
      ]
    },
    {
      name: this.sectionsPrefix + 'reasoningBranches',
      visible: false,
      links: [
        {
          label: 'reasoningBranchBrowser.title',
          url: '/reasoning-branch/browser',
          icon: 'visibility_outline'
        },
        {
          label: 'changeRequest.title',
          url: '/reasoning-branches/change-request',
          icon: 'add_circle_outline'
        },
        {
          label: 'pendingChanges.title',
          url: '/reasoning-branches/pending-changes',
          icon: 'library_add_check'
        },
        {
          label: 'circuitBreakerDashboard.title',
          url: '/reasoning-branches/circuit-breaker-dashboard',
          icon: 'error_outline'
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
          icon: 'description'
        },
        {
          label: 'reasoningBranchesReport.title',
          url: '/reports/reasoning-branches-report',
          icon: 'description'
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
