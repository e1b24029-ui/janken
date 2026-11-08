package oit.is.z3412.kaizi.janken.model;

public class Janken {
  private String myHand;
  private String cpuHand = "Gu";
  private String result;

  public Janken(String myHand) {
    this.myHand = myHand;
    this.result = judge(myHand, cpuHand);
  }

  private String judge(String my, String cpu) {
    if (my.equals(cpu)) {
      return "あいこ";
    }
    if ((my.equals("Gu") && cpu.equals("Choki")) ||
        (my.equals("Choki") && cpu.equals("Pa")) ||
        (my.equals("Pa") && cpu.equals("Gu"))) {
      return "勝ち";
    }
    return "負け";
  }

  // 二人対戦用の勝敗判定メソッド（static）
  public static String judgeMatch(String hand1, String hand2, String name1, String name2) {
    if (hand1.equals(hand2)) {
      return "引き分け";
    }
    // "Gu", "Choki", "Pa" の組み合わせで判定するように修正
    if ((hand1.equals("Gu") && hand2.equals("Choki")) ||
        (hand1.equals("Choki") && hand2.equals("Pa")) ||
        (hand1.equals("Pa") && hand2.equals("Gu"))) {
      return name1;
    }
    return name2;
  }

  public String getMyHand() {
    return myHand;
  }

  public String getCpuHand() {
    return cpuHand;
  }

  public String getResult() {
    return result;
  }
}
