import { Routes } from '@angular/router';
import { NotAuthenticatedComponent } from '@app/pages/not-authenticated/not-authenticated.component';
import { AuthenticationGuard } from '@app/shared/security/guard/authentication-guard.service';
import { AnalystHomeComponent } from '@app/templates/analyst-home/analyst-home.component';
import { AuditTrailComponent } from '@app/templates/audit-trail/audit-trail.component';
import { ExternalComponent } from './layout/external/external.component';

import { AccessDeniedComponent } from './pages/access-denied/access-denied.component';
import { ErrorPageComponent } from './pages/error-page/error-page.component';
import { InternalServerErrorComponent } from './pages/internal-server-error/internal-server-error.component';
import { MaintenanceComponent } from './pages/maintenance/maintenance.component';

import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { ReasoningBranchManagementPageComponent } from './reasoning-branch-management/containers/reasoning-branch-management-page/reasoning-branch-management-page.component';
import { AuthorityGuard } from './shared/security/guard/authority-guard.service';
import { UserManagementPageComponent } from './user-management/containers/user-management-page/user-management-page.component';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: '/reasoning-branch',
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Business Operator']
    }
  },
  {
    path: 'reasoning-branch/:id',
    pathMatch: 'full',
    component: ReasoningBranchManagementPageComponent,
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Business Operator']
    }
  },
  {
    path: 'reasoning-branch',
    pathMatch: 'full',
    component: ReasoningBranchManagementPageComponent,
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Business Operator']
    }
  },
  {
    path: 'user-management',
    component: UserManagementPageComponent,
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Admin']
    }
  },
  {
    path: 'reports',
    component: AuditTrailComponent,
    canActivate: [AuthenticationGuard, AuthorityGuard],
    data: {
      authorities: ['Auditor']
    },
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
  ['Admin', '/user-management'],
  ['Business Operator', '/reasoning-branch'],
  ['Auditor', '/reports'],
  ['Analyst', '/analyst'],
]);
