import { TableHelper } from './table-helper';

describe('TableHelper', () => {
  let helper: TableHelper;

  beforeEach(() => {
    helper = new TableHelper();
  });

  it('should return valid result when invoke hasManyValues', () => {
    expect(helper.hasManyValues(null)).toBeFalsy();
    expect(helper.hasManyValues(undefined)).toBeFalsy();
    expect(helper.hasManyValues([])).toBeFalsy();
    expect(helper.hasManyValues(['a'])).toBeFalsy();
    expect(helper.hasManyValues([{}])).toBeFalsy();
    expect(helper.hasManyValues(['a', 'b', 'c'])).toBeTruthy();
    expect(helper.hasManyValues([{}, {}, {}])).toBeTruthy();
  });

  it('should return valid result when invoke hasOneValue', () => {
    expect(helper.hasOneValue(null)).toBeFalsy();
    expect(helper.hasOneValue(undefined)).toBeFalsy();
    expect(helper.hasOneValue([])).toBeFalsy();
    expect(helper.hasOneValue(['a'])).toBeTruthy();
    expect(helper.hasOneValue([{}])).toBeTruthy();
    expect(helper.hasOneValue(['a', 'b', 'c'])).toBeFalsy();
    expect(helper.hasOneValue([{}, {}, {}])).toBeFalsy();
  });

  it('should return valid result when invoke isEmptyValue', () => {
    expect(helper.isEmptyValue(null)).toBeTruthy();
    expect(helper.isEmptyValue(undefined)).toBeTruthy();
    expect(helper.isEmptyValue([])).toBeTruthy();
    expect(helper.isEmptyValue(['a'])).toBeFalsy();
    expect(helper.isEmptyValue([{}])).toBeFalsy();
    expect(helper.isEmptyValue(['a', 'b', 'c'])).toBeFalsy();
    expect(helper.isEmptyValue([{}, {}, {}])).toBeFalsy();
  });
});
