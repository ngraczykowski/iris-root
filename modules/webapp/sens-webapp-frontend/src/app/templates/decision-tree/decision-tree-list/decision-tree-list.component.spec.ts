import {
  async,
  ComponentFixture,
  discardPeriodicTasks,
  fakeAsync,
  TestBed,
  tick
} from '@angular/core/testing';
import { AuthService } from '@app/shared/security/auth.service';
import { Authority } from '@app/shared/security/principal.model';
import { TestModule } from '@app/test/test.module';
import { CollectionResponse } from '@model/collection-response.model';
import { DecisionTree } from '@model/decision-tree.model';
import { EMPTY, of, timer } from 'rxjs';
import { mapTo } from 'rxjs/operators';
import { DecisionTreeListComponent } from './decision-tree-list.component';
import { DecisionTreeListModule } from './decision-tree-list.module';
import { DecisionTreeListService } from './decision-tree-list.service';

// TODO(iwnek)
describe('DecisionTreesComponent', () => {
  let component: DecisionTreeListComponent;
  let fixture: ComponentFixture<DecisionTreeListComponent>;

  let decisionTreesService: DecisionTreeListService;
  let authService: AuthService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule, DecisionTreeListModule]
        })
        .compileComponents();

    decisionTreesService = TestBed.get(DecisionTreeListService);
    authService = TestBed.get(AuthService);
  }));

  function initComponent() {
    fixture = TestBed.createComponent(DecisionTreeListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }

  it('should create', () => {
    spyOn(decisionTreesService, 'listDecisionTrees').and.returnValue(EMPTY);

    initComponent();

    expect(component).toBeTruthy();
  });

  it('should set firstTimeLoaded true, after first load', fakeAsync(() => {
    const response = <CollectionResponse<DecisionTree>>{total: 0, results: []};
    spyOn(decisionTreesService, 'listDecisionTrees')
        .and.returnValue(timer(50).pipe(mapTo(response)));
    initComponent();

    expect(component.firstTimeLoaded).toBeFalsy();
    tick(200);
    expect(component.firstTimeLoaded).toBeTruthy();

    discardPeriodicTasks();
  }));

  it('should hasDecisionTreeManageAccess be true if user has DECISION_TREE_MANAGE authority', () => {
    spyOn(authService, 'hasAuthority')
        .withArgs(Authority.DECISION_TREE_MANAGE).and.returnValue(true)
        .and.returnValue(false);

    initComponent();

    expect(component.hasDecisionTreeManageAccess).toBeTruthy();
  });

  it('should hasDecisionTreeManageAccess be false if user does not have DECISION_TREE_MANAGE authority', () => {
    spyOn(authService, 'hasAuthority')
        .withArgs(Authority.DECISION_TREE_MANAGE).and.returnValue(false)
        .and.returnValue(false);

    initComponent();

    expect(component.hasDecisionTreeManageAccess).toBeFalsy();
  });

  it('should update decisionTrees only if server response changed', fakeAsync(() => {
    const dt1 = {
      id: 1,
      name: 'dt1',
      status: { name: 'ACTIVE' },
      assignments: ['BT1']
    } as DecisionTree;

    const dt2 = {
      id: 2,
      name: 'dt2',
      status: { name: 'ACTIVE' },
      assignments: ['BT2']
    } as DecisionTree;

    const response1 = <CollectionResponse<DecisionTree>>{ total: 0, results: [] };
    const response2 = <CollectionResponse<DecisionTree>>{ total: 0, results: [dt1] };
    const response3 = <CollectionResponse<DecisionTree>>{ total: 0, results: [dt1] };
    const response4 = <CollectionResponse<DecisionTree>>{ total: 0, results: [dt2] };
    const response5 = <CollectionResponse<DecisionTree>>{ total: 0, results: [dt1, dt2] };
    const response6 = <CollectionResponse<DecisionTree>>{ total: 0, results: [dt1] };

    spyOn(decisionTreesService, 'listDecisionTrees')
      .and.returnValues(
        of(response1),
        of(response2),
        of(response2),
        of(response3),
        of(response4),
        of(response5),
        of(response6)
      );

    initComponent();
    tick();

    let previousDecisionTrees = component.decisionTrees;
    expect(component.decisionTrees).toEqual(response1.results);

    component.updateDecisionTrees();
    expect(component.decisionTrees).toEqual(response2.results);
    expect(component.decisionTrees).not.toEqual(previousDecisionTrees);
    expect(component.decisionTrees === previousDecisionTrees).toBeFalsy();
    previousDecisionTrees = component.decisionTrees;

    component.updateDecisionTrees();
    expect(component.decisionTrees).toEqual(response2.results);
    expect(component.decisionTrees).toEqual(previousDecisionTrees);
    expect(component.decisionTrees === previousDecisionTrees).toBeTruthy();
    previousDecisionTrees = component.decisionTrees;

    component.updateDecisionTrees();
    expect(component.decisionTrees).toEqual(response3.results);
    expect(component.decisionTrees).toEqual(previousDecisionTrees);
    expect(component.decisionTrees === previousDecisionTrees).toBeTruthy();
    previousDecisionTrees = component.decisionTrees;

    component.updateDecisionTrees();
    expect(component.decisionTrees).toEqual(response4.results);
    expect(component.decisionTrees).not.toEqual(previousDecisionTrees);
    expect(component.decisionTrees === previousDecisionTrees).toBeFalsy();
    previousDecisionTrees = component.decisionTrees;

    component.updateDecisionTrees();
    expect(component.decisionTrees).toEqual(response5.results);
    expect(component.decisionTrees).not.toEqual(previousDecisionTrees);
    expect(component.decisionTrees === previousDecisionTrees).toBeFalsy();

    component.updateDecisionTrees();
    expect(component.decisionTrees).toEqual(response6.results);
    expect(component.decisionTrees).not.toEqual(previousDecisionTrees);
    expect(component.decisionTrees === previousDecisionTrees).toBeFalsy();

    discardPeriodicTasks();
  }));
});
