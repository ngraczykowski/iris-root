import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BranchPage } from '@app/templates/decision-tree/branch-table/branch-page';
import { BranchPageLoader } from '@app/templates/decision-tree/branch-table/branch-page-loader';
import { TestModule } from '@app/test/test.module';
import { Observable, of, throwError } from 'rxjs';
import { Branch, BranchModel } from '../../../model/branch.model';
import { BranchChangeStateService } from '../branch-change-state.service';
import { BranchSelectStateData, BranchUpdateStateData } from '../branch-change.component';
import { BranchChangeModule } from '../branch-change.module';

import { BranchBulkSelectorComponent } from './branch-bulk-selector.component';
import createSpyObj = jasmine.createSpyObj;


describe('BranchBulkSelectorComponent', () => {
  let component: BranchBulkSelectorComponent;
  let fixture: ComponentFixture<BranchBulkSelectorComponent>;

  let service: BranchChangeStateService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            BranchChangeModule
          ]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BranchBulkSelectorComponent);
    component = fixture.componentInstance;

    service = TestBed.get(BranchChangeStateService);
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should getSelectedBranchesCount return 2 when 2 branches are selected', () => {
    component.store.add('id1', <Branch>{});
    component.store.add('id2', <Branch>{});
    fixture.detectChanges();

    expect(component.getSelectedBranchesCount()).toEqual(2);
  });

  it('should hasSelectedBranches be false where there is no branches in store', () => {
    fixture.detectChanges();

    expect(component.hasSelectedBranches()).toBeFalsy();
  });

  it('should hasSelectedBranches be true where there are selected branches in store', () => {
    component.store.add('id1', <Branch>{});
    fixture.detectChanges();

    expect(component.hasSelectedBranches()).toBeTruthy();
  });

  it('should set update state onBranchSelected', () => {
    fixture.detectChanges();
    component.data = <BranchSelectStateData> {decisionTreeId: 1};
    component.store.storeModel(<BranchModel> {featureNames: ['feature1']});
    component.store.add('id1', <Branch>{matchGroupId: 1});
    component.store.add('id2', <Branch>{matchGroupId: 2});
    spyOn(service, 'setUpdateState');

    component.onBranchSelected();

    expect(service.setUpdateState).toHaveBeenCalledWith(<BranchUpdateStateData> {
      decisionTreeId: 1,
      branchModel: {featureNames: ['feature1']},
      branches: [
        {matchGroupId: 1},
        {matchGroupId: 2}
      ]
    });
  });

  it('should clear store and error on loader change', () => {
    component.store.add('id1', <Branch>{});
    component.store.add('id2', <Branch>{});

    component.onLoaderChange(createSpyObj(['load', 'registerListener', 'unregisterListener']));

    expect(component.store.length()).toEqual(0);
    expect(component.showErrorSelectAll).toBeFalsy();
  });

  it('should clear error on clear selection', () => {
    component.showErrorSelectAll = true;
    component.clearSelection();

    expect(component.showErrorSelectAll).toBeFalsy();
    expect(component.store.length()).toEqual(0);
  });

  it('should set all branches on load all', () => {
    const loader: BranchPageLoader = createSpyObj(['load', 'registerListener', 'unregisterListener']);
    const branches: Branch[] = [
      <Branch>{decisionTreeInfo: {id: 1}, matchGroupId: 1},
      <Branch>{decisionTreeInfo: {id: 1}, matchGroupId: 2}
    ];
    loader.loadWithLimit = () => of(<BranchPage>{total: 2, model: null, items: branches});
    component.onLoaderChange(loader);
    component.total = 2;

    component.selectAllBranches();

    expect(component.showErrorSelectAll).toBeFalsy();
    expect(component.areAllSelected()).toBeTruthy();
    expect(component.loading).toBeFalsy();
  });

  it('should set error on load all error', () => {
    component.total = 5;
    component.loader = <BranchPageLoader><unknown>{loadWithLimit(): Observable<any> {
        return throwError('error');
      }
    };

    component.selectAllBranches();

    expect(component.showErrorSelectAll).toBeTruthy();
    expect(component.loading).toBeFalsy();
  });
});
