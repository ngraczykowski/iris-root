import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExternalComponent } from '@app/layout/external/external.component';
import { TestModule } from '@app/test/test.module';

describe('ExternalComponent', () => {
  let component: ExternalComponent;
  let fixture: ComponentFixture<ExternalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ExternalComponent],
      imports: [TestModule]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExternalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
