import { DecisionGroup, DecisionGroupListBuilder } from './decision-group-list-builder';

describe('DecisionGroupListBuilder', () => {

  function assertDecisionGroups(actual: DecisionGroup[], expected: DecisionGroup[]) {
    expect(actual).toEqual(expected);
  }

  it('should create empty decision groups when pass null activations', () => {
    const decisionGroups = new DecisionGroupListBuilder(null).build();

    assertDecisionGroups(decisionGroups, []);
  });

  it('should create empty decision groups when pass undefined activations', () => {
    const decisionGroups = new DecisionGroupListBuilder(undefined).build();

    assertDecisionGroups(decisionGroups, []);
  });

  it('should create decisionGroups when pass some activations', () => {
    const decisionGroups = new DecisionGroupListBuilder(['a', 'b', 'c']).build();

    assertDecisionGroups(decisionGroups, [
      {value: 'a'},
      {value: 'b'},
      {value: 'c'}
    ]);
  });
});
