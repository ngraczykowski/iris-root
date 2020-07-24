import { Authority } from '@app/shared/security/principal.model';
import { UserRole } from '@app/templates/model/user.model';

export const environment = {
  production: true,
  serverApiUrl: '/rest/webapp/api',
  managementApiUrl: '/rest/webapp/management',

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
    keycloak: {
      url: '/auth',
      realm: 'sens-webapp',
      clientId: 'frontend',
      adminRoleName: 'admin',
      excludedTokenUrls: [
          '/assets'
      ]
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
      urlRegex: /^rest\/webapp\/api\/.*/,
      httpMethods: ['GET', 'POST', 'DELETE', 'PUT', 'PATCH']
    }
  ],
  accessDeniedErrorRedirectRequestMatchers: [
    {
      urlRegex: /^rest\/webapp\/api\/.*/,
      httpMethods: ['GET', 'POST', 'DELETE', 'PUT', 'PATCH']
    }
  ],
  notFoundErrorRedirectRequestMatchers: [
    {
      urlRegex: /^rest\/webapp\/api\/decision-tree\/[0-9]+$/,
      httpMethods: ['GET']
    },
    {
      urlRegex: /^rest\/webapp\/api\/decision-tree\/[0-9]+\/branch\/[0-9]+$/,
      httpMethods: ['GET']
    },
    {
      urlRegex: /^rest\/webapp\/api(\/decision-tree\/[0-9]+)?(\/branch\/[0-9]+)?\/alert\/.+$/,
      httpMethods: ['GET']
    }
  ],

  decimal: 10,

  tablePagination: {
    firstPage: 0,
    rowsPerPage: [20, 50, 100],
    defaultPageSize: 20
  },

  dateFormatting: 'yyyy-MM-dd HH:mm:ss',

};


