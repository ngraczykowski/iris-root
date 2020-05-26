import { Routes } from '@angular/router';
import { AuditTrailComponent } from '@app/audit-trail/containers/audit-trail/audit-trail.component';
import { PendingChangesComponent } from '@app/pending-changes/containers/pending-changes/pending-changes.component';
import { NotAuthenticatedComponent } from '@app/pages/not-authenticated/not-authenticated.component';
import { AuthenticationGuard } from '@app/shared/security/guard/authentication-guard.service';
import { AnalystHomeComponent } from '@app/templates/analyst-home/analyst-home.component';
import { ExternalComponent } from './layout/external/external.component';

import { AccessDeniedComponent } from './pages/access-denied/access-denied.component';
import { ErrorPageComponent } from './pages/error-page/error-page.component';
import { InternalServerErrorComponent } from './pages/internal-server-error/internal-server-error.component';
import { MaintenanceComponent } from './pages/maintenance/maintenance.component';

import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { ReasoningBranchManagementPageComponent } from './reasoning-branch-management/containers/reasoning-branch-management-page/reasoning-branch-management-page.component';
import { AuthorityGuard } from './shared/security/guard/authority-guard.service';
import { UserManagementPageComponent } from './user-management/containers/user-management-page/user-management-page.component';
import { ChangeRequestComponent } from '@app/change-request/containers/change-request/change-request.component';
import { SecurityMatrixComponent } from './templates/audit-trail/audit-trail.component';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: '/reasoning-branch',
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Admin', 'Business Operator']
    }
  },
  {
    path: 'reasoning-branch/:id',
    pathMatch: 'full',
    component: ReasoningBranchManagementPageComponent,
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Admin', 'Business Operator']
    }
  },
  {
    path: 'reasoning-branch',
    pathMatch: 'full',
    component: ReasoningBranchManagementPageComponent,
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Admin', 'Business Operator']
    }
  },
  {
    path: 'reasoning-branches/change-request',
    pathMatch: 'full',
    component: ChangeRequestComponent,
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Admin', 'Business Operator']
    }
  },
  {
    path: 'reports/audit-trail',
    pathMatch: 'full',
    component: AuditTrailComponent,
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Admin', 'Auditor']
    }
  },
  {
    path: 'reasoning-branches/pending-changes',
    pathMatch: 'full',
    component: PendingChangesComponent,
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Business Operator', 'Approver']
    }
  },
  {
    path: 'users/user-management',
    component: UserManagementPageComponent,
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Admin']
    }
  },
  {
    path: 'reports/security-matrix',
    component: SecurityMatrixComponent,
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Admin', 'Auditor']
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
    component: ExternalComponent,
    children: [
      {
        path: '404',
        component: PageNotFoundComponent,
      },
      {
        path: '403',
        component: AccessDeniedComponent,
        canActivate: [AuthenticationGuard]
      },
      {
        path: '401',
        component: NotAuthenticatedComponent,
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

export const rolesByRedirect: Map<string, string> = new Map([
  ['Admin', 'users/user-management'],
  ['Business Operator', '/reasoning-branch'],
  ['Auditor', 'reports/audit-trail'],
  ['Analyst', '/analyst'],
  ['Approver', '/reasoning-branches/pending-changes']
]);
