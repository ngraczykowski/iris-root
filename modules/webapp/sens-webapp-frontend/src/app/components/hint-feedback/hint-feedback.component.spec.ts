import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { TestModule } from '@app/test/test.module';

import { HintFeedbackComponent } from './hint-feedback.component';

describe('HintFeedbackComponent', () => {
  let component: HintFeedbackComponent;
  let fixture: ComponentFixture<HintFeedbackComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [TestModule],
      declarations: [ HintFeedbackComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HintFeedbackComponent);
    component = fixture.componentInstance;
    component.settings = {
      visible: true,
      type: 'info'
    };

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should show if visibility is true', () => {
    component.settings.visible = true;
    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css('.hint-feedback')).nativeElement).toBeTruthy();
  });

  it('should not show if visibility is true', () => {
    component.settings.visible = false;
    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css('.hint-feedback'))).toBeNull();
  });

  it('should add a prefix to the type of notification', () => {
    component.settings.type = 'info';
    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css('.hint-feedback-status-info')).nativeElement).toBeTruthy();
  });

  it('should show list of elements', () => {
    component.settings.visible = true;
    component.settings.elementsList = [{label: 'label1'}];
    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css('.hint-feedback-content-elements-list')).nativeElement).toBeTruthy();
  });

  it('should hide list of elements', () => {
    component.settings.visible = true;
    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css('.hint-feedback-content-elements-list'))).toBeNull();
  });

  it('should show options', () => {
    component.settings.visible = true;
    component.settings.options = [{label: 'label1', url: 'url1'}];
    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css('.hint-feedback-content-footer')).nativeElement).toBeTruthy();
  });

  it('should hide options', () => {
    component.settings.visible = true;
    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css('.hint-feedback-content-footer'))).toBeNull();
  });

  it('should show secondary description', () => {
    component.settings.visible = true;
    component.settings.descriptionSecondary = 'description';
    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css('.hint-feedback-content-description-secondary')).nativeElement).toBeTruthy();
  });

  it('should hide secondary description', () => {
    component.settings.visible = true;
    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css('.hint-feedback-content-description-secondary'))).toBeNull();
  });
});
