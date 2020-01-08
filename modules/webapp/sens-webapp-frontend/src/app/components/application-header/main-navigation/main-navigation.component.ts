import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';
import { Observable, of } from 'rxjs';

@Component({
  selector: 'app-main-navigation',
  templateUrl: './main-navigation.component.html',
  styleUrls: ['./main-navigation.component.scss']
})
export class MainNavigationComponent implements OnInit {

  hasDecisionTreeAccess: Observable<boolean>;
  hasInboxAccess: Observable<boolean>;
  hasAuditTrailAccess: Observable<boolean>;
  hasUserManagementAccess: Observable<boolean>;
  hasWorkflowManagementAccess: Observable<boolean>;
  hasApproverAccess: Observable<boolean>;

  showMenu: boolean;

  @HostListener('document:click', ['$event'])
  handleMouseClick(event) {
    const clickInsideComponent = this.eRef.nativeElement.contains(event.target);
    if (!clickInsideComponent) {
      this.showMenu = false;
    }
  }

  constructor(private eRef: ElementRef, private authenticatedUser: AuthenticatedUserFacade) { }

  ngOnInit() {
    this.hasDecisionTreeAccess = this.authenticatedUser.hasAccessToUrl('/decision-tree');
    this.hasInboxAccess = of(false);
    this.hasAuditTrailAccess = this.authenticatedUser.hasAccessToUrl('/audit-trail');
    this.hasUserManagementAccess = this.authenticatedUser.hasAccessToUrl('/user-management');
    this.hasWorkflowManagementAccess = this.authenticatedUser.hasAccessToUrl('/workflow-management');
    this.hasApproverAccess = this.authenticatedUser.hasAccessToUrl('/approver');
  }

  logout(): void {
    this.authenticatedUser.logout();
  }
}
