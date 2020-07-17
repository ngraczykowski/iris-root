import { AuthoritiesConfiguration } from '@core/authorities/model/authorities-configuration';
import { Authority } from '@core/authorities/model/authority.enum';

export const authoritiesConfig: AuthoritiesConfiguration = {
  features: {
    users_list: [Authority.ADMIN],
    rb_browser: [Authority.BUSINESS_OPERATOR, Authority.APPROVER],
    rb_change_request: [Authority.BUSINESS_OPERATOR],
    rb_pending_changes: [Authority.BUSINESS_OPERATOR, Authority.APPROVER],
    rb_cb_dashboard: [Authority.BUSINESS_OPERATOR],
    reports_audit_trail: [Authority.AUDITOR],
    reports_reasoning_branches: [Authority.BUSINESS_OPERATOR, Authority.APPROVER, Authority.AUDITOR],
    reports_security_matrix: [Authority.AUDITOR],
    reports_users: [Authority.AUDITOR],
  }
};
