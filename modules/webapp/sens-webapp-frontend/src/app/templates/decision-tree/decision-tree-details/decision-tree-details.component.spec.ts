import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { EMPTY } from 'rxjs';
import { TestModule } from '../../../test/test.module';
import { DecisionTreeDetailsComponent } from './decision-tree-details.component';
import { DecisionTreeDetailsModule } from './decision-tree-details.module';
import { DecisionTreeDetailsService } from './decision-tree-details.service';

describe('DecisionTreeComponent', () => {
  let component: DecisionTreeDetailsComponent;
  let fixture: ComponentFixture<DecisionTreeDetailsComponent>;

  let decisionTreeService: DecisionTreeDetailsService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule, DecisionTreeDetailsModule]
        })
        .compileComponents();

    decisionTreeService = TestBed.get(DecisionTreeDetailsService);
  }));

  function initComponent() {
    fixture = TestBed.createComponent(DecisionTreeDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }

  it('should create', () => {
    spyOn(decisionTreeService, 'getDecisionTreeDetails').and.returnValue(EMPTY);

    initComponent();

    expect(component).toBeTruthy();
  });
});
