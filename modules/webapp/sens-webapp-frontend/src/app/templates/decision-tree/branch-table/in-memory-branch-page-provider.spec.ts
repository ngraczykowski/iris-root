import { InMemoryBranchPageProvider } from '@app/templates/decision-tree/branch-table/in-memory-branch-page-provider';
import { Branch, BranchModel } from '@app/templates/model/branch.model';

describe('InMemoryBranchPageProvider', () => {
  const branches = [
    <Branch>{matchGroupId: 1},
    <Branch>{matchGroupId: 2},
    <Branch>{matchGroupId: 3},
    <Branch>{matchGroupId: 4},
    <Branch>{matchGroupId: 5},
    <Branch>{matchGroupId: 6}
  ];
  const model = <BranchModel>{featureNames: ['feature1']};

  let provider: InMemoryBranchPageProvider;

  beforeEach(() => {
    provider = new InMemoryBranchPageProvider(branches, model);
  });

  it('should return branches: 1, 2, 3 on select page 1 with size = 3', done => {
    provider.getBranches(1, 3).subscribe(data => {
      expect(data.items).toEqual([
        <Branch>{matchGroupId: 1},
        <Branch>{matchGroupId: 2},
        <Branch>{matchGroupId: 3}
      ]);
      done();
    });
  });

  it('should return branches: 5, 6 on select page 2 with size = 4', done => {
    provider.getBranches(2, 4).subscribe(data => {
      expect(data.items).toEqual([
        <Branch>{matchGroupId: 5},
        <Branch>{matchGroupId: 6}
      ]);
      done();
    });
  });

  it('should return total = 6', done => {
    provider.getBranches(1, 1).subscribe(data => {
      expect(data.total).toEqual(6);
      done();
    });
  });

  it('should return valid model', done => {
    provider.getBranches(1, 1).subscribe(data => {
      expect(data.model).toEqual(model);
      done();
    });
  });
});
