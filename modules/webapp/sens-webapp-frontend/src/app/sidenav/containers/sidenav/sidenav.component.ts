import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';
import { Authority } from '@core/authorities/model/authority.enum';
import { AuthoritiesService } from '@core/authorities/services/authorities.service';

@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html'
})
export class SidenavComponent implements OnInit {

  public static featuresLinks = {
    users_list: {
      label: 'usersList.title',
      url: '/users/user-management',
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
      features: ['reports_audit_trail', 'reports_reasoning_branches', 'reports_security_matrix']
    }];

  @Output() showNavigation = new EventEmitter();
  mainNav;

  constructor(
      private authenticatedUser: AuthenticatedUserFacade,
      private authoritiesService: AuthoritiesService
  ) { }

  ngOnInit() {

    SidenavComponent.sections.forEach((group: any) => {
      group.features = group.features.filter((key =>
        this.authoritiesService.hasAuthorityForFeature(key)))
          .map((key) => SidenavComponent.featuresLinks[key]);
    });

    this.mainNav = SidenavComponent.sections.filter((section) => section.features.length);

    if (this.mainNav.length) {
      this.showNavigation.emit(true);
    }
  }

}
