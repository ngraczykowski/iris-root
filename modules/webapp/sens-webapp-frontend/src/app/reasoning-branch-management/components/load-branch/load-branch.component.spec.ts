import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoadBranchComponent } from './load-branch.component';
import { TestModule } from '@app/test/test.module';

describe('LoadBranchComponent', () => {
  let component: LoadBranchComponent;
  let fixture: ComponentFixture<LoadBranchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LoadBranchComponent ],
      imports: [ TestModule ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoadBranchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
