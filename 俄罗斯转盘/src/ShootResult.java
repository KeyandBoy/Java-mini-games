// src/ShootResult.java
/**
 * 射击结果封装类
 * 对应资源路径：
 * - images/SKull.png （命中状态显示）
 * - sounds/hit.wav （命中音效）
 * - sounds/gameover.wav （卡壳音效）
 */
public class ShootResult {
    private final boolean hit;
    private final boolean jammed;

    // 构造方法
    // hit 是否命中
    //jammed 是否卡壳（当装满6发子弹时可能触发）
    public ShootResult(boolean hit, boolean jammed) { 
        this.hit = hit; // 命中状态
        this.jammed = jammed; // 卡壳状态
    }

    // 获取命中状态（与resources/images/SKull.png显示相关）
    public boolean isHit() {
        return hit; // 返回命中状态
    }

    // 获取卡壳状态（与resources/sounds/gameover.wav音效相关
    public boolean isJammed() {
        return jammed; // 返回卡壳状态
    }
}