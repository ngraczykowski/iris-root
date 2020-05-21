import { Component, OnInit } from '@angular/core';

interface AuditTrailReport {
  name: string;
  description: string;
  url: string;
}

@Component({
  selector: 'app-audit-trail',
  templateUrl: './audit-trail.component.html',
  styleUrls: ['./audit-trail.component.scss']
})
export class SecurityMatrixComponent implements OnInit {

  readonly reports: AuditTrailReport[] = [
    /* {
      name: 'Model',
      description: 'model',
      url: '/rest/webapp/api/audit-trail/model'
    },
    {
      name: 'Decision Tree',
      description: 'decision-tree',
      url: '/rest/webapp/api/audit-trail/decision-tree'
    },
    {
      name: 'Reasoning Branch',
      description: 'reasoning-branch',
      url: '/rest/webapp/api/audit-trail/reasoning-branch'
    },
    {
      name: 'User Profile',
      description: 'user-profile',
      url: '/rest/webapp/api/audit-trail/user'
    },
    {
      name: 'Change Request',
      description: 'change-request',
      url: '/rest/webapp/api/audit-trail/change-request'
    }, */
    {
      name: 'Security Matrix',
      description: 'security-matrix',
      url: '/rest/webapp/api/report/security-matrix-report'
    } /*,
    {
      name: 'Users List',
      description: 'users-list',
      url: '/rest/webapp/api/users/export'
    },
    {
      name: 'Circuit Breaker Triggered Alerts',
      description: 'cb-triggered-alerts',
      url: '/rest/webapp/api/audit-trail/circuit-breaker-triggered-alerts'
    } */
  ];

  constructor() { }

  ngOnInit() {
  }
}
