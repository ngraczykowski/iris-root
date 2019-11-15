import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { TestModule } from '@app/test/test.module';
import { DecisionTreeDetailsModule } from '../../decision-tree-details.module';

import { DecisionTreeInfoFeaturesComponent } from './decision-tree-info-features.component';

describe('DecisionTreeInfoFeaturesComponent', () => {
  let component: DecisionTreeInfoFeaturesComponent;
  let fixture: ComponentFixture<DecisionTreeInfoFeaturesComponent>;

  const fakeDecisionTreeAgentsList = [
    {
      name: 'fakeName',
      description: 'fakeVisibleName'
    }
  ];

  const fakeDecisionTreeFeaturesList = [
    {
      name: 'fakeName',
      description: 'fakeVisibleName'
    }
  ];

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule, DecisionTreeDetailsModule]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DecisionTreeInfoFeaturesComponent);
    component = fixture.componentInstance;
    component.decisionTreeFeatures = [];
    component.decisionTreeAgents = [];
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should show empty state when there is nothing to show', () => {

    const decisionTreeDetailsFeaturesEmptyState = fixture.debugElement.query(By.css('.empty-state-content'));
    expect(decisionTreeDetailsFeaturesEmptyState).toBeTruthy();
  });

  it('should hide empty state when there are items for the Features and Agents list', () => {
    component.decisionTreeFeatures = fakeDecisionTreeFeaturesList;
    component.decisionTreeAgents = fakeDecisionTreeAgentsList;

    fixture.detectChanges();

    const decisionTreeDetailsFeaturesEmptyState = fixture.debugElement.query(By.css('.empty-state-content'));
    expect(decisionTreeDetailsFeaturesEmptyState).toBeFalsy();
  });

  it('should hide empty state when there are items only for the Features list', () => {
    component.decisionTreeFeatures = fakeDecisionTreeFeaturesList;

    fixture.detectChanges();

    const decisionTreeDetailsFeaturesEmptyState = fixture.debugElement.query(By.css('.empty-state-content'));
    expect(decisionTreeDetailsFeaturesEmptyState).toBeFalsy();
  });

  it('should hide empty state when there are items only for the Agents list', () => {
    component.decisionTreeAgents = fakeDecisionTreeAgentsList;

    fixture.detectChanges();

    const decisionTreeDetailsFeaturesEmptyState = fixture.debugElement.query(By.css('.empty-state-content'));
    expect(decisionTreeDetailsFeaturesEmptyState).toBeFalsy();
  });
});
