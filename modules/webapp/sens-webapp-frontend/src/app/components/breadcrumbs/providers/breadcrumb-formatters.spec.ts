import {
  AlertBreadcrumbFormatter,
  BranchBreadcrumbFormatter,
  DecisionTreeBreadcrumbFormatter,
  DecisionTreesBreadcrumbFormatter
} from './breadcrumb-formatters';

describe('DecisionTreesBreadcrumbFormatter', () => {
  let formatter: DecisionTreesBreadcrumbFormatter;

  beforeEach(() => {
    formatter = new DecisionTreesBreadcrumbFormatter();
  });

  it('Should format name correctly', () => {
    expect(formatter.formatName()).toEqual('Decision Trees');
  });

  it('Should format url correctly', () => {
    expect(formatter.formatUrl()).toEqual('/decision-tree');
  });
});

describe('DecisionTreeBreadcrumbFormatter', () => {
  let formatter: DecisionTreeBreadcrumbFormatter;

  beforeEach(() => {
    formatter = new DecisionTreeBreadcrumbFormatter();
  });

  it('Should format name correctly', () => {
    expect(formatter.formatName({ id: null })).toEqual('Decision Tree #null');
    expect(formatter.formatName({ id: 'id' })).toEqual('Decision Tree #id');
    expect(formatter.formatName({ id: 1 })).toEqual('Decision Tree #1');
  });

  it('Should format url correctly', () => {
    expect(formatter.formatUrl({ id: null })).toEqual('/decision-tree/null');
    expect(formatter.formatUrl({ id: 'id' })).toEqual('/decision-tree/id');
    expect(formatter.formatUrl({ id: 1 })).toEqual('/decision-tree/1');
  });
});

describe('BranchBreadcrumbFormatter', () => {
  let formatter: BranchBreadcrumbFormatter;

  beforeEach(() => {
    formatter = new BranchBreadcrumbFormatter();
  });

  it('Should format name correctly', () => {
    expect(formatter.formatName({ decisionTreeId: null, id: null })).toEqual('Reasoning Branch #null-null');
    expect(formatter.formatName({ decisionTreeId: 'id', id: 'id' })).toEqual('Reasoning Branch #id-id');
    expect(formatter.formatName({ decisionTreeId: 1, id: 1 })).toEqual('Reasoning Branch #1-1');
  });

  it('Should format url correctly', () => {
    expect(formatter.formatUrl({ decisionTreeId: null, matchGroupId: null })).toEqual('/decision-tree/null/reasoning-branch/null');
    expect(formatter.formatUrl({ decisionTreeId: 'dtId', matchGroupId: 'mgId' })).toEqual('/decision-tree/dtId/reasoning-branch/mgId');
    expect(formatter.formatUrl({ decisionTreeId: 1, matchGroupId: 2 })).toEqual('/decision-tree/1/reasoning-branch/2');
  });
});
describe('AlertBreadcrumbFormatter', () => {
  let formatter: AlertBreadcrumbFormatter;

  beforeEach(() => {
    formatter = new AlertBreadcrumbFormatter();
  });

  it('Should format name correctly', () => {
    expect(formatter.formatName({ id: null })).toEqual('Alert (null)');
    expect(formatter.formatName({ id: 'id' })).toEqual('Alert (id)');
    expect(formatter.formatName({ id: 1 })).toEqual('Alert (1)');
  });
});
