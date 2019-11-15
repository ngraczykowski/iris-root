import { FilterPipe } from './filter.pipe';

describe('FilterPipe', () => {

  function assertNamePassed(name, query) {
    return assertElementPassed({ name: name }, query);
  }

  function assertNameFiltered(name, query) {
    return assertElementFiltered({ name: name }, query);
  }

  function assertElementPassed(element, query) {
    return _testElement(element, query).toEqual([element]);
  }

  function assertElementFiltered(element, query) {
    return _testElement(element, query).toEqual([]);
  }

  function _testElement(element, query): jasmine.Matchers<any> {
    const pipe = new FilterPipe();
    return expect(pipe.transform([element], query, 'name'));
  }

  it('create an instance', () => {
    const pipe = new FilterPipe();
    expect(pipe).toBeTruthy();
  });

  it('should pass when invoke with null, undefined or empty query', () => {
    assertNamePassed('US_BTCH_PEPL', null);
    assertNamePassed('US_BTCH_PEPL', undefined);
    assertNamePassed('US_BTCH_PEPL', '');
  });

  it('should pass exactly the same name', () => {
    assertNamePassed('US_BTCH_PEPL', 'US_BTCH_PEPL');
  });

  it('should pass prefix', () => {
    assertNamePassed('US_BTCH_PEPL', 'US_BTCH');
  });

  it('should pass when invoke with lower case query and upper case name', () => {
    assertNamePassed('US_BTCH_PEPL', 'US_BTCH_PEPL'.toLowerCase());
  });

  it('should pass when invoke with upper case query and lower case name', () => {
    assertNamePassed('US_BTCH_PEPL'.toLowerCase(), 'US_BTCH_PEPL');
  });

  it('should pass when invoke with name with whitespaces', () => {
    assertNamePassed('    US_BTCH_PEPL   ', 'US BTCH PEPL');
  });

  it('should pass when invoke with query with whitespaces', () => {
    assertNamePassed('US BTCH PEPL', '    US_BTCH_PEPL   ');
  });

  it('should pass when invoke with query without special characters', () => {
    assertNamePassed('US BTCH PEPL', 'USBTCHPEPL');
  });

  it('should pass when invoke with name without special characters', () => {
    assertNamePassed('USBTCHPEPL', 'US BTCH PEPL');
  });

  it('should pass when invoke with name with skipped first valid characters', () => {
    assertNamePassed('US_BTCH_PEPL', 'SBTCH');
  });

  it('should filter out when name is null, undefined or empty', () => {
    assertNameFiltered(null, 'IN_BTCH_PEPL');
    assertNameFiltered(undefined, 'IN_BTCH_PEPL');
    assertNameFiltered('', 'IN_BTCH_PEPL');
  });

  it('should filter out different name', () => {
    assertNameFiltered('US_BTCH_PEPL', 'IN_BTCH_PEPL');
  });

  it('should filter out when element is null, undefined or empty', () => {
    assertElementFiltered(null, 'IN_BTCH_PEPL');
    assertElementFiltered(undefined, 'IN_BTCH_PEPL');
    assertElementFiltered({}, 'IN_BTCH_PEPL');
  });

  it('should filter out name with skipped valid characters in the middle', () => {
    assertNameFiltered('US_BTCH_PEPL', 'US_CH_PEPL');
  });
});
