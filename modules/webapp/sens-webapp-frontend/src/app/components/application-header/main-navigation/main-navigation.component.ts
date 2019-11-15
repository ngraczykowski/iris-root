import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { AuthService } from '@app/shared/auth/auth.service';

@Component({
  selector: 'app-main-navigation',
  templateUrl: './main-navigation.component.html',
  styleUrls: ['./main-navigation.component.scss']
})
export class MainNavigationComponent implements OnInit {

  hasDecisionTreeAccess: boolean;
  hasInboxAccess: boolean;
  hasAuditTrailAccess: boolean;
  hasUserManagementAccess: boolean;
  hasWorkflowManagementAccess: boolean;
  hasApproverAccess: boolean;

  showMenu: boolean;

  @HostListener('document:click', ['$event'])
  handleMouseClick(event) {
    const clickInsideComponent = this.eRef.nativeElement.contains(event.target);
    if (!clickInsideComponent) {
      this.showMenu = false;
    }
  }

  constructor(private eRef: ElementRef, private authService: AuthService) { }

  ngOnInit() {
    this.hasDecisionTreeAccess = this.authService.hasAccessToUrl('/decision-tree');
    this.hasInboxAccess = false;
    this.hasAuditTrailAccess = this.authService.hasAccessToUrl('/audit-trail');
    this.hasUserManagementAccess = this.authService.hasAccessToUrl('/user-management');
    this.hasWorkflowManagementAccess = this.authService.hasAccessToUrl('/workflow-management');
    this.hasApproverAccess = this.authService.hasAccessToUrl('/approver');
  }

  logout(): void {
    this.authService.logout();
  }
}
