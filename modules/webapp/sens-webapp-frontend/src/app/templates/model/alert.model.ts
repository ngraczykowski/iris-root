import { Branch } from './branch.model';

export class FieldEntity {
  fields: Field[];
}

export class Field {
  name: string;
  value: any;
}

export class Party extends FieldEntity {
  externalId: string;
}

export class Solution {
  decision: string;
  comment: string;
  date: string;
}

export class Alert {
  id: number;
  externalId: string;
  alertFields: string[];
  matchFields: string[];
  analystSolution: Solution;
}

export class AlertDetails {
  id: number;
  externalId: string;
  party: Party;
  branchInfos: Branch[];
  analystSolution: Solution;
  matchFieldNames: string[];
  aiSolution: Solution;

  deserialize(input: any): AlertDetails {
    Object.assign(this, input);
    return this;
  }
}

export class AlertModel {
  alertFieldNames: string[];
  matchFieldNames: string[];
}

export class AlertPageResponse {
  total: number;
  alertModel: AlertModel;
  alerts: Alert[];

  deserialize(input: any): AlertPageResponse {
    Object.assign(this, input);
    return this;
  }
}
