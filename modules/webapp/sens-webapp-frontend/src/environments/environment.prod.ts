import { Authority } from '@app/shared/auth/principal.model';
import { UserRole } from '@app/templates/model/user.model';

export const environment = {
  production: true,
  serverApiUrl: '/',

  decisionTrees: {
    pollIntervalInMs: 5 * 1000
  },

  mainNavigation: {
    infoboxTab: {
      pollInboxStatsInMs: 5 * 1000
    }
  },

  auth: {
    authorityPageUrlMapping: {
      '/decision-tree': Authority.DECISION_TREE_LIST,
      '/inbox': Authority.INBOX_MANAGE,
      '/audit-trail': Authority.AUDIT_GENERATE_REPORT,
      '/user-management': Authority.USER_MANAGE,
      '/analyst-home': Authority.SOLUTION_VIEW,
      '/workflow-management': Authority.WORKFLOW_MANAGE,
      '/approver': UserRole.ROLE_APPROVER
    },
    authorityStartPageUrlPriority: [
      '/decision-tree', '/inbox', '/audit-trail', '/user-management', '/workflow-management',
      '/analyst-home', '/approver'
    ],
    oauth2: {
      clientId: 'SENS-frontend',
      clientSecret: 'yQTUy6aJp1OXI4a5LvYyyvFUJlfFo4lp',
      refreshTokenMaxAttempts: 3
    },
    activityMonitor: {
      inactivityTimeToLogoutInSec: 15 * 60,
      inactivityTimeToDisplayNotificationInSec: 12 * 60,
      activityPoolingTimeInMillis: 500,
    }
  },

  notifications: {
    defaultTimeoutInSec: 5
  },

  perfectScrollbarConfig: {},
  http: {
    error: {
      retryIntervalInMs: 5000
    }
  },

  internalServerErrorRedirectRequestMatchers: [
    {
      urlRegex: /^api\/.*/,
      httpMethods: ['GET', 'POST', 'DELETE', 'PUT', 'PATCH']
    }
  ],
  accessDeniedErrorRedirectRequestMatchers: [
    {
      urlRegex: /^api\/.*/,
      httpMethods: ['GET', 'POST', 'DELETE', 'PUT', 'PATCH']
    }
  ],
  notFoundErrorRedirectRequestMatchers: [
    {
      urlRegex: /^api\/decision-tree\/[0-9]+$/,
      httpMethods: ['GET']
    },
    {
      urlRegex: /^api\/decision-tree\/[0-9]+\/branch\/[0-9]+$/,
      httpMethods: ['GET']
    },
    {
      urlRegex: /^api(\/decision-tree\/[0-9]+)?(\/branch\/[0-9]+)?\/alert\/.+$/,
      httpMethods: ['GET']
    }
  ]
};


