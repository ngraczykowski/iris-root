import { Routes } from '@angular/router';
import { AuditTrailComponent } from '@app/audit-trail/containers/audit-trail/audit-trail.component';
import { ChangeRequestComponent } from '@app/change-request/containers/change-request/change-request.component';
import { CircuitBreakerBranchListComponent } from '@app/circuit-breaker-dashboard/containers/circuit-breaker-branch-list/circuit-breaker-branch-list.component';
import { CircuitBreakerDashboardComponent } from '@app/circuit-breaker-dashboard/containers/circuit-breaker-dashboard/circuit-breaker-dashboard.component';
import { CircuitBreakerDiscrepancyStatus } from '@app/circuit-breaker-dashboard/models/circuit-breaker';
import { PendingChangesTabsContainerComponent } from '@app/pending-changes/containers/pending-changes-tabs-container/pending-changes-tabs-container.component';
import { PendingChangesComponent } from '@app/pending-changes/containers/pending-changes/pending-changes.component';
import { PendingChangesStatus } from '@app/pending-changes/models/pending-changes';
import { ReasoningBranchesBrowserComponent } from '@app/reasoning-branches-browser/containers/reasoning-branches-browser/reasoning-branches-browser.component';
import { ReasoningBranchesReportComponent } from '@app/reasoning-branches-report/containers/reasoning-branches-report/reasoning-branches-report.component';
import { AuthenticationGuard } from '@app/shared/security/guard/authentication-guard.service';
import { AnalystHomeComponent } from '@app/templates/analyst-home/analyst-home.component';
import { UsersReportComponent } from '@app/users-report/containers/users-report/users-report.component';
import { AuthorityGuard } from './shared/security/guard/authority-guard.service';
import { SecurityMatrixComponent } from './templates/audit-trail/audit-trail.component';
import { UserManagementPageComponent } from './user-management/containers/user-management-page/user-management-page.component';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'reasoning-branch/browser',
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Business Operator', 'Approver']
    }
  },
  {
    path: 'reasoning-branch/browser',
    pathMatch: 'full',
    component: ReasoningBranchesBrowserComponent,
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Business Operator', 'Approver']
    }
  },
  {
    path: 'reasoning-branches/change-request/:decisionTreeId/:reasoningBranchId',
    pathMatch: 'full',
    component: ChangeRequestComponent,
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Business Operator']
    }
  },
  {
    path: 'reasoning-branches/change-request',
    pathMatch: 'full',
    component: ChangeRequestComponent,
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Business Operator']
    }
  },
  {
    path: 'reasoning-branches/circuit-breaker-dashboard',
    component: CircuitBreakerDashboardComponent,
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Business Operator']
    },
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'active'
      },
      {
        path: 'active',
        component: CircuitBreakerBranchListComponent,
        data: {
          discrepancyStatuses: [CircuitBreakerDiscrepancyStatus.ACTIVE]
        }
      },
      {
        path: 'archived',
        component: CircuitBreakerBranchListComponent,
        data: {
          discrepancyStatuses: [CircuitBreakerDiscrepancyStatus.ARCHIVED]
        }
      }
    ]
  },
  {
    path: 'reports/audit-trail',
    pathMatch: 'full',
    component: AuditTrailComponent,
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Auditor']
    }
  },
  {
    path: 'reports/reasoning-branches-report',
    pathMatch: 'full',
    component: ReasoningBranchesReportComponent,
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Business Operator', 'Auditor', 'Approver']
    }
  },
  {
    path: 'reasoning-branches/pending-changes',
    component: PendingChangesTabsContainerComponent,
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Business Operator', 'Approver']
    },
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'queue'
      },
      {
        path: 'queue',
        pathMatch: 'full',
        component: PendingChangesComponent,
        data: {
          changeRequestStatuses: [PendingChangesStatus.PENDING]
        }
      },
      {
        path: 'archived',
        pathMatch: 'full',
        component: PendingChangesComponent,
        data: {
          changeRequestStatuses: [PendingChangesStatus.CLOSED]
        }
      },
    ]
  },
  {
    path: 'users/user-management',
    component: UserManagementPageComponent,
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Admin', 'Administrator']
    }
  },
  {
    path: 'reports/security-matrix',
    component: SecurityMatrixComponent,
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Auditor']
    }
  },
  {
    path: 'reports/users-report',
    pathMatch: 'full',
    component: UsersReportComponent,
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Auditor']
    }
  },
  {
    path: 'analyst',
    component: AnalystHomeComponent,
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Analyst']
    }
  },
  {
    path: '',
    loadChildren: './warnings/warnings.module#WarningsModule'
  },
  {
    path: '**',
    redirectTo: '/404',
  }
];

export const rolesByRedirect: Map<string, string> = new Map([
  ['Admin', 'users/user-management'],
  ['Administrator', 'users/user-management'],
  ['Business Operator', '/reasoning-branch'],
  ['Auditor', 'reports/audit-trail'],
  ['Analyst', '/analyst'],
  ['Approver', '/reasoning-branches/pending-changes']
]);
