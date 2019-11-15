export interface BatchType {
  value: string;
  active: boolean;
}

export class BatchTypeListBuilder {

  constructor(private assignments: string[], private activations: string[]) { }

  build(): BatchType[] {
    return this.getActiveBatchTypes().concat(this.getInactiveBatchTypes());
  }

  private getActiveBatchTypes(): BatchType[] {
    return this.emptyIfNullOrUndefined(this.activations)
        .map(bt => <BatchType>{value: bt, active: true});
  }

  private getInactiveBatchTypes(): BatchType[] {
    return this.emptyIfNullOrUndefined(this.assignments)
        .filter(bt => !this.activations || !this.activations.includes(bt))
        .map(bt => <BatchType>{value: bt, active: false});
  }

  private emptyIfNullOrUndefined(array: any[]) {
    return array ? array : [];
  }
}
