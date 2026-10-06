package oit.is.z3412.kaizi.janken.model;

public class Janken {
  private String myHand;
  private String cpuHand = "グー"; // CPUの手はグー固定
  private String result;

  public Janken(String myHand) {
    this.myHand = myHand;
    this.result = judge(myHand, cpuHand);
  }

  // じゃんけんの勝敗判定ロジック
  private String judge(String my, String cpu) {
    if (my.equals(cpu)) {
      return "あいこ";
    }
    if ((my.equals("グー") && cpu.equals("チョキ")) ||
        (my.equals("チョキ") && cpu.equals("パー")) ||
        (my.equals("パー") && cpu.equals("グー"))) {
      return "勝ち";
    }
    return "負け";
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
