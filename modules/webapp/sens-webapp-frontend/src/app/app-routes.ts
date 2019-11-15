import { Routes } from '@angular/router';
import { AlertRestrictionsManagementComponent } from '@app/templates/alert-restrictions-management/alert-restrictions-management.component';
import { UserRole } from '@app/templates/model/user.model';
import { ExternalComponent } from './layout/external/external.component';

import { InternalComponent } from './layout/internal/internal.component';
import { AccessDeniedComponent } from './pages/access-denied/access-denied.component';
import { ErrorPageComponent } from './pages/error-page/error-page.component';
import { InternalServerErrorComponent } from './pages/internal-server-error/internal-server-error.component';
import { MaintenanceComponent } from './pages/maintenance/maintenance.component';

import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { AccessGuard } from './shared/auth/access-guard';
import { AuthGuard } from './shared/auth/auth-guard';
import { Authority } from './shared/auth/principal.model';

import { StartPageGuard } from './shared/auth/start-page-guard';
import { AlertComponent } from './templates/alert/alert.component';
import { AnalystHomeComponent } from './templates/analyst-home/analyst-home.component';
import { ApproverComponent } from './templates/approver/approver.component';
import { AuditTrailComponent } from './templates/audit-trail/audit-trail.component';
import { DecisionTreeDetailsComponent } from './templates/decision-tree/decision-tree-details/decision-tree-details.component';
import { DecisionTreeListComponent } from './templates/decision-tree/decision-tree-list/decision-tree-list.component';

import { InboxComponent } from './templates/inbox/inbox.component';
import { ReasoningBranchComponent } from './templates/reasoning-branch/reasoning-branch.component';

import { UserManagementComponent } from './templates/user-management/user-management.component';
import { WorkflowManagementComponent } from './templates/workflow-management/workflow-management.component';

export const routes: Routes = [
  {
    path: '',
    canActivate: [StartPageGuard],
    children: [],
    pathMatch: 'full'
  },
  {
    path: '',
    canActivate: [AuthGuard],
    canActivateChild: [AccessGuard],
    component: InternalComponent,
    children: [
      {
        path: 'decision-tree',
        component: DecisionTreeListComponent,
        data: {
          authority: Authority.DECISION_TREE_LIST
        },
      },
      {
        path: 'decision-tree/:decisionTreeId',
        component: DecisionTreeDetailsComponent,
        data: {
          authority: Authority.DECISION_TREE_LIST
        },
      },
      {
        path: 'decision-tree/:decisionTreeId/reasoning-branch/:matchGroupId',
        component: ReasoningBranchComponent,
        data: {
          authority: Authority.DECISION_TREE_LIST
        },
      },
      {
        path: 'decision-tree/:decisionTreeId/reasoning-branch/:matchGroupId/alert/:externalId',
        component: AlertComponent,
        data: {
          authority: Authority.DECISION_TREE_LIST
        },
      },
      {
        path: 'decision-tree/:decisionTreeId/alert/:externalId',
        component: AlertComponent,
        data: {
          authority: Authority.DECISION_TREE_LIST
        },
      },
      {
        path: 'alert/:alertIdOrExternalId',
        component: AlertComponent,
        data: {
          authority: Authority.DECISION_TREE_LIST
        },
      },
      {
        path: 'inbox',
        component: InboxComponent,
        data: {
          authority: Authority.INBOX_MANAGE
        },
      },
      {
        path: 'audit-trail',
        component: AuditTrailComponent,
        data: {
          authority: Authority.AUDIT_GENERATE_REPORT
        },
      },
      {
        path: 'user-management',
        component: UserManagementComponent,
        data: {
          authority: Authority.USER_MANAGE
        }
      },
      {
        path: 'alert-restrictions-management',
        component: AlertRestrictionsManagementComponent,
        data: {
          authority: Authority.USER_MANAGE
        }
      },
      {
        path: 'workflow-management',
        component: WorkflowManagementComponent,
        data: {
          authority: Authority.WORKFLOW_MANAGE
        }
      },
      {
        path: 'analyst-home',
        component: AnalystHomeComponent,
        data: {
          authority: Authority.SOLUTION_VIEW
        }
      },
      {
        path: 'approver',
        component: ApproverComponent,
        data: {
          authority: UserRole.ROLE_APPROVER
        }
      }
    ]
  },
  {
    path: '',
    component: ExternalComponent,
    children: [
      {
        path: '404',
        component: PageNotFoundComponent,
      },
      {
        path: '403',
        component: AccessDeniedComponent,
      },
      {
        path: 'maintenance',
        component: MaintenanceComponent,
      },
      {
        path: '500',
        component: InternalServerErrorComponent,
      },
      {
        path: 'error-page',
        component: ErrorPageComponent,
      }
    ]
  },
  {
    path: '**',
    redirectTo: '/404',
  }
];
