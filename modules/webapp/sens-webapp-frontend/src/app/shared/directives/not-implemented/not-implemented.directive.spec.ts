import { NotImplementedDirective } from './not-implemented.directive';
import createSpyObj = jasmine.createSpyObj;

describe('NotImplementedDirective', () => {
  let underTest: NotImplementedDirective;

  it('should invoke remove on element refs nativeElement ' +
      'given in constructor', () => {
    const nativeElement = createSpyObj('nativeElement', ['remove']);
    const elementRef = {nativeElement};

    underTest = new NotImplementedDirective(elementRef);

    expect(nativeElement.remove).toHaveBeenCalled();
  });
});
