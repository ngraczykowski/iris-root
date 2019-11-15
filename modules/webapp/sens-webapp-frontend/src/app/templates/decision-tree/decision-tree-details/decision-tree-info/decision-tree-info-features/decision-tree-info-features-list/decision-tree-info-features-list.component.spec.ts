import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ClipboardService } from '@app/shared/clipboard/clipboard.service';
import { DecisionTreeDetailsModule } from '@app/templates/decision-tree/decision-tree-details/decision-tree-details.module';
import { TestModule } from '@app/test/test.module';

import { DecisionTreeInfoFeaturesListComponent } from './decision-tree-info-features-list.component';

describe('DecisionTreeInfoFeaturesListComponent', () => {
  let component: DecisionTreeInfoFeaturesListComponent;
  let fixture: ComponentFixture<DecisionTreeInfoFeaturesListComponent>;

  let clipboardService: ClipboardService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule, DecisionTreeDetailsModule]
        })
        .compileComponents();

    clipboardService = TestBed.get(ClipboardService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DecisionTreeInfoFeaturesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should copy to clipboard', () => {
    const spy = spyOn(clipboardService, 'copy');
    component.onCopyToClipboard('test');
    expect(spy).toHaveBeenCalledTimes(1);
    expect(spy).toHaveBeenCalledWith('f_test');
  });

});
