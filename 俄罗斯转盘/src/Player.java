// src/Player.java
/**
 * 玩家信息封装类
 * 对应游戏规则：
 * - 玩家状态与resources/images/Skull.png显示关联
 * - 赏金累计规则与游戏逻辑匹配
 */
//java作业
public class Player {
    // 不可变属性（与界面显示相关）
    private final String name;  // 玩家名称（用于界面展示）
    
    // 可变状态（与游戏逻辑相关）
    private int bounty;          // 当前赏金（单位：游戏币）
    private boolean alive = true; // 存活状态（控制是否参与下一回合）

    /**
     * 构造方法（必须与类名一致）
     * @param name 玩家名称（建议使用UTF-8字符）
     */
    public Player(String name) {
        this.name = name;
    }

    //━━━━━━━━━━━━━━ 状态访问方法 ━━━━━━━━━━━━━━//
    
    /**
     * 获取玩家名称（用于界面显示和日志记录）
     */
    public String getName() {
        return name; // 返回不可修改的名称
    }

    /**
     * 获取当前赏金（影响最终排名）
     */
    public int getBounty() {
        return bounty; // 返回累计赏金
    }

    /**
     * 存活状态检查（决定是否参与下一回合）
     */
    public boolean isAlive() {
        return alive; // true=存活，false=已淘汰
    }

    //━━━━━━━━━━━━━━ 状态修改方法 ━━━━━━━━━━━━━━//
    
    /**
     * 设置存活状态（当被击中时调用）
     * @param alive 新状态（通常设置为false表示淘汰）
     */
    public void setAlive(boolean alive) {
        this.alive = alive; // 修改存活标志
    }

    /**
     * 增加赏金（根据游戏规则调用）
     * @param amount 赏金增量（可正可负）
     */
    public void addBounty(int amount) {
        bounty += amount; // 累加到当前赏金
    }
    
}