
import { DecisionTreeReportsMenuComponent } from './decision-tree-reports-menu.component';
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { TestModule } from '@app/test/test.module';
import { WINDOW } from '@app/shared/window.service';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

const MockWindow = {
  location: {
    assign(param) { }
  }
};

describe('DecisionTreeReportsMenuComponent', () => {
  let component: DecisionTreeReportsMenuComponent;
  let fixture: ComponentFixture<DecisionTreeReportsMenuComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [TestModule],
      declarations: [ DecisionTreeReportsMenuComponent ],
      providers: [
        {
          provide: WINDOW,
          useValue: MockWindow
        },
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({decisionTreeId: 1})
          }
        }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DecisionTreeReportsMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call backend API with report url, when generateReport invoked', () => {
    const spy = spyOn(component.window.location, 'assign');

    component.generateReport();

    expect(component.decisionTreeId).toEqual(1);
    expect(spy).toHaveBeenCalledWith('/rest/webapp/api/decision-tree/1/circuit-breaker-triggered-alerts');
  });
});
