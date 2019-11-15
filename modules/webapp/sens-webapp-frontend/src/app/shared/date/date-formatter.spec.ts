import { DateFormatter, ParseDateException } from '@app/shared/date/date-formatter';

describe('DateFormatter', () => {

  function assertDate(dateString, expectedDate) {
    expect(DateFormatter.format(dateString)).toEqual(expectedDate);
  }

  beforeEach(() => {
    Date.prototype.toLocaleString = Date.prototype.toUTCString;
  });

  it('should format date correctly', () => {
    assertDate('2018-07-06T06:35:55.455Z', 'Fri, 06 Jul 2018 06:35:55 GMT');
  });

  it('should throw exception when pass invalid date', () => {
    expect(function () { DateFormatter.format(null); }).toThrow(new ParseDateException());
    expect(function () { DateFormatter.format('asdad'); }).toThrow(new ParseDateException());
  });
});
