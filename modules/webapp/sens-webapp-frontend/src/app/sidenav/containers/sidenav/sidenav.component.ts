import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';
import { AuthoritiesService } from '@core/authorities/services/authorities.service';
import { ExternalAppsService } from '@core/external-apps/services/external-apps.service';
import { ExternalApp } from '@endpoint/external-apps/model/external-app.enum';

@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html'
})
export class SidenavComponent implements OnInit {

  public static featuresLinks = {
    users_list: {
      label: 'usersList.title',
      url: '/user-management',
      icon: 'people_outline'
    },
    rb_browser: {
      label: 'reasoningBranchesBrowser.title',
      url: '/reasoning-branch/browser',
      icon: 'visibility_outline'
    },
    rb_change_request: {
      label: 'changeRequest.title',
      url: '/reasoning-branches/change-request',
      icon: 'add_circle_outline'
    },
    rb_pending_changes: {
      label: 'pendingChanges.title',
      url: '/reasoning-branches/pending-changes',
      icon: 'library_add_check'
    },
    rb_cb_dashboard: {
      label: 'circuitBreakerDashboard.title',
      url: '/reasoning-branches/circuit-breaker-dashboard',
      icon: 'error_outline'
    },
    reports_audit_trail: {
      label: 'auditTrail.title',
      url: '/reports/audit-trail',
      icon: 'description'
    },
    reports_reasoning_branches: {
      label: 'reasoningBranchesReport.title',
      url: '/reports/reasoning-branches-report',
      icon: 'description'
    },
    reports_security_matrix: {
      label: 'securityMatrix.title',
      url: '/reports/security-matrix',
      icon: 'lock'
    },
    reports_users: {
      label: 'usersReport.title',
      url: '/reports/users-report',
      icon: 'lock'
    }
  };

  public static sections = [{
      name: 'adminPanel',
      features: ['users_list']
    },
    {
      name: 'reasoningBranches',
      features: ['rb_browser', 'rb_change_request', 'rb_pending_changes', 'rb_cb_dashboard']
    },
    {
      name: 'reports',
      features: [
        'reports_audit_trail', 'reports_reasoning_branches',
        'reports_security_matrix', 'reports_users']
    }];

  @Output() showNavigation = new EventEmitter();
  mainNav;

  constructor(
      private authenticatedUser: AuthenticatedUserFacade,
      private authoritiesService: AuthoritiesService,
      private externalAppsService: ExternalAppsService
  ) {}

  ngOnInit() {

    SidenavComponent.sections.forEach((group: any) => {
      group.features = group.features.filter((key =>
        this.authoritiesService.hasAuthorityForFeature(key)))
          .map((key) => SidenavComponent.featuresLinks[key]);
    });

    this.externalAppsService.getAvailableApps().subscribe((apps: ExternalApp[]) => {

      this.mainNav = SidenavComponent.sections.filter((section) => section.features.length);

      if (apps && apps.length) {
        this.mainNav = [
          ...this.mainNav,
          {
            name: 'externalApps',
            features: apps.map((app: ExternalApp) => {
              return {
                label: 'externalApps.app.' + app,
                action: () => this.externalAppsService.openApp(app),
                icon: 'analytics'
              };
            })
          }];
      }

      if (this.mainNav.length) {
        this.showNavigation.emit(true);
      }
    });


  }

}
