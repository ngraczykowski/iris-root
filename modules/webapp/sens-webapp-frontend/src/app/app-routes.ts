import { Routes } from '@angular/router';
import { NotAuthenticatedComponent } from '@app/pages/not-authenticated/not-authenticated.component';
import { AuthenticationGuard } from '@app/shared/security/guard/authentication-guard.service';
import { AlertRestrictionsManagementComponent } from '@app/templates/alert-restrictions-management/alert-restrictions-management.component';
import { UserRole } from '@app/templates/model/user.model';
import { ExternalComponent } from './layout/external/external.component';

import { InternalComponent } from './layout/internal/internal.component';
import { AccessDeniedComponent } from './pages/access-denied/access-denied.component';
import { ErrorPageComponent } from './pages/error-page/error-page.component';
import { InternalServerErrorComponent } from './pages/internal-server-error/internal-server-error.component';
import { MaintenanceComponent } from './pages/maintenance/maintenance.component';

import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { AuthorityGuard } from './shared/security/guard/authority-guard.service';
import { Authority } from './shared/security/principal.model';
import { AlertComponent } from './templates/alert/alert.component';
import { AnalystHomeComponent } from './templates/analyst-home/analyst-home.component';
import { ApproverComponent } from './templates/approver/approver.component';
import { AuditTrailComponent } from './templates/audit-trail/audit-trail.component';

import { InboxComponent } from './templates/inbox/inbox.component';

import { WorkflowManagementComponent } from './templates/workflow-management/workflow-management.component';
import { ReasoningBranchManagementPageComponent } from './reasoning-branch-management/containers/reasoning-branch-management-page/reasoning-branch-management-page.component';
import { UserManagementPageComponent } from './user-management/containers/user-management-page/user-management-page.component';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    canActivate: [AuthenticationGuard],
    component: ReasoningBranchManagementPageComponent
  },
  {
    path: 'user-management',
    canActivate: [AuthenticationGuard],
    component: UserManagementPageComponent
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
