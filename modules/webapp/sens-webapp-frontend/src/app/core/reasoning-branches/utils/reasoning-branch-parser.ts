export class ReasoningBranchParser {

  private static whiteChar = /[,\n]+/;

  public static parseIds(input: string): number[] {
    return input.split(ReasoningBranchParser.whiteChar)
        .map(id => parseInt(id, 0)).filter(Boolean);
  }

  public static parseSignatures(input: string): any[] {
    return input.split(ReasoningBranchParser.whiteChar)
        .filter((value: any) => value !== '');
  }

}
