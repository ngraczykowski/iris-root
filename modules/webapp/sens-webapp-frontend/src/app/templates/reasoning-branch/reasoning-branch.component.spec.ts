import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ActivatedRoute, ChildrenOutletContexts, Router } from '@angular/router';
import { TestModule } from '@app/test/test.module';
import { EMPTY, Subject } from 'rxjs';
import { BranchDetails } from '../model/branch.model';
import { AlertRow } from './alert-table-data-mapper';

import { ReasoningBranchComponent } from './reasoning-branch.component';
import { ReasoningBranchModule } from './reasoning-branch.module';
import { ReasoningBranchService } from './reasoning-branch.service';

const mockRoute: any  = { snapshot: {}};
mockRoute.parent = { params: new Subject<any>()};
mockRoute.params = new Subject<any>();
mockRoute.queryParams = new Subject<any>();

describe('ReasoningBranchComponent', () => {
  let component: ReasoningBranchComponent;
  let fixture: ComponentFixture<ReasoningBranchComponent>;

  let reasoningBranchService: ReasoningBranchService;

  const mockRouter = {
    navigate: jasmine.createSpy('navigate')
  };

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule, ReasoningBranchModule],
          providers: [
            ChildrenOutletContexts,
            {
              provide: ActivatedRoute, useValue: mockRoute
            },
            {
              provide: Router, useValue: mockRouter
            }
          ]
        })
        .compileComponents();

    reasoningBranchService = TestBed.get(ReasoningBranchService);
  }));

  function initComponent() {
    fixture = TestBed.createComponent(ReasoningBranchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }

  it('should create', () => {
    spyOn(reasoningBranchService, 'getBranchDetails').and.returnValue(EMPTY);

    initComponent();

    expect(component).toBeTruthy();
  });

  it('should get param from url', async() => {
    initComponent();

    const paramsMock = { learningQualityPage: 2, otherPage: 1, suspendingPage: 1 };
    component.loadQueryParams();
    mockRoute.queryParams.next(paramsMock);

    fixture.detectChanges();

    expect(component.queryParamsPages.learningQualityTablePage).toEqual(paramsMock.learningQualityPage);
    expect(component.queryParamsPages.otherTablePage).toEqual(paramsMock.otherPage);
    expect(component.queryParamsPages.suspendingTablePage).toEqual(paramsMock.suspendingPage);
  });

  it('should open alert in the same window when selectAlert', () => {
    spyOn(reasoningBranchService, 'getBranchDetails').and.returnValue(EMPTY);
    initComponent();
    component.branchDetails = <BranchDetails>{decisionTreeInfo: {id: 1}, matchGroupId: 2};

    component.selectAlert(<AlertRow>{externalId: 'externalId1'});

    expect (mockRouter.navigate).toHaveBeenCalledWith (['/decision-tree/1/reasoning-branch/2/alert/externalId1']);

  });
});
