import { FriendlyTextFormatter } from './friendly-text-formatter';

describe('FriendlyTextFormatter', () => {

  function assertPipe(value, expected) {
    const pipe = new FriendlyTextFormatter();
    expect(pipe.apply(value)).toEqual(expected);
  }

  it('create an instance', () => {
    const pipe = new FriendlyTextFormatter();
    expect(pipe).toBeTruthy();
  });

  it('should not change value if it is not dictionary value', () => {
    assertPipe(null, null);
    assertPipe('', '');
    assertPipe(' ', ' ');
    assertPipe('This is some value', 'This is some value');
    assertPipe('This is some value with _', 'This is some value with _');
    assertPipe('_Test', '_Test');
    assertPipe('some_email@test.com', 'some_email@test.com');
    assertPipe('VARiable', 'VARiable');
  });

  it('should change value if it is dictionary value', () => {
    assertPipe('TRUE_POSITIVE', 'True Positive');
    assertPipe('POTENTIAL_TRUE_POSITIVE', 'Potential True Positive');
    assertPipe('birthDate', 'Birth Date');
    assertPipe('advancedNameSolver', 'Advanced Name Solver');
  });
});
