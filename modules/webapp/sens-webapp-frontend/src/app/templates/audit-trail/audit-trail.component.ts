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
export class AuditTrailComponent implements OnInit {

  readonly reports: AuditTrailReport[] = [
    {
      name: 'Model',
      description: 'model',
      url: '/api/audit-trail/model'
    },
    {
      name: 'Decision Tree',
      description: 'decision-tree',
      url: '/api/audit-trail/decision-tree'
    },
    {
      name: 'Reasoning Branch',
      description: 'reasoning-branch',
      url: '/api/audit-trail/reasoning-branch'
    },
    {
      name: 'User Profile',
      description: 'user-profile',
      url: '/api/audit-trail/user'
    },
    {
      name: 'Change Request',
      description: 'change-request',
      url: '/api/audit-trail/change-request'
    },
    {
      name: 'Security Matrix',
      description: 'security-matrix',
      url: '/assets/files/security-matrix/security-matrix-report.xlsx'
    },
    {
      name: 'Users List',
      description: 'users-list',
      url: '/api/users/export'
    },
    {
      name: 'Circuit Breaker Triggered Alerts',
      description: 'cb-triggered-alerts',
      url: '/api/audit-trail/circuit-breaker-triggered-alerts'
    },
  ];

  constructor() { }

  ngOnInit() {
  }
}
