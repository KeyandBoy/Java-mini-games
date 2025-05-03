// src/Revolver.java
import java.util.*;

public class Revolver {
    // 常量定义：左轮手枪的弹膛数量
    private static final int CHAMBER_SIZE = 6; // 固定6发容量
    //java作业
    // 随机数生成器（用于生成子弹位置和旋转结果）
    private final Random random = new Random(); 
    
    // 存储装有子弹的弹膛位置（使用Set避免重复）
    private Set<Integer> bulletChambers = new HashSet<>(); 
    
    // 当前旋转后指向的弹膛位置（0~5之间的整数）
    private int currentChamber;

    /**
     * 装弹方法 (原样保留)
     * @param count 要装入的子弹数量：
     *              - 0：空弹
     *              - 6：装满子弹
     *              - 其他数值：随机装入指定数量的子弹
     */
    public void loadBullet(int count) {
        bulletChambers.clear(); // 清空原有子弹
        
        if (count == 6) {
            // 装满所有弹膛
            for (int i = 0; i < CHAMBER_SIZE; i++) {
                bulletChambers.add(i); // 0~5全部加入集合
            }
        } else {
            // 随机装入指定数量的子弹
            while (bulletChambers.size() < count) {
                // 可能产生重复值，但会被Set自动过滤
                bulletChambers.add(random.nextInt(CHAMBER_SIZE));
            }
        }
    }

    /**
     * 旋转弹膛 (原样保留)
     * 随机设置当前指向的弹膛位置
     */
    public void spinChamber() {
        currentChamber = random.nextInt(CHAMBER_SIZE); // 生成0~5的随机数
    }

    /**
     * 射击逻辑 (原样保留)
     * @return ShootResult 包含是否命中和是否卡壳的状态
     */
    public ShootResult shoot() {
        boolean jammed = false;  // 卡壳状态
        boolean hit = false;     // 命中状态
        
        // 特殊规则：装满6发子弹的情况
        if (bulletChambers.size() == 6) {
            // 5%概率卡壳
            jammed = random.nextDouble() < 0.05;
            // 如果卡壳则不命中，否则必中
            hit = !jammed; 
        } else {
            // 普通情况：检查当前弹膛是否有子弹
            hit = bulletChambers.contains(currentChamber);
        }
        
        // 返回结果对象（需确保ShootResult类存在）
        return new ShootResult(hit, jammed);
    }
}