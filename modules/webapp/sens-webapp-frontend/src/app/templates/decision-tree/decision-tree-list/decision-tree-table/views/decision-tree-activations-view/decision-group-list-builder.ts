export interface DecisionGroup {
  value: string;
}

export class DecisionGroupListBuilder {

  constructor(private activations: string[]) { }

  build(): DecisionGroup[] {
    return this.getDecisionGroups();
  }

  private getDecisionGroups(): DecisionGroup[] {
    return this.emptyIfNullOrUndefined(this.activations)
        .map(bt => <DecisionGroup>{value: bt.name});
  }

  private emptyIfNullOrUndefined(array: any[]) {
    return array ? array : [];
  }
}
