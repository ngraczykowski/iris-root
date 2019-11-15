import EnumHelper from './enum-helper';

describe('EnumHelper', () => {

  enum StringEnum {
    VALUE1 = 'VALUE1',
    VALUE2 = 'VALUE2',
    VALUE3 = 'VALUE3'
  }

  it('find index of enum value', () => {
    expect(EnumHelper.indexOf(StringEnum, StringEnum.VALUE1)).toBe(0);
    expect(EnumHelper.indexOf(StringEnum, StringEnum.VALUE2)).toBe(1);
    expect(EnumHelper.indexOf(StringEnum, StringEnum.VALUE3)).toBe(2);
  });
});
