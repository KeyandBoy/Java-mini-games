// src/MainApplication.java
import javax.swing.*;          // GUI组件
import java.util.ArrayList;    // 动态数组
import java.util.List;         // 列表接口
//javawork
/**
 * 程序入口类（主类）
 * 职责：
 * 1. 初始化玩家数量
 * 2. 创建游戏主窗口
 * 3. 启动游戏循环
 */
public class MainApplication {
    
    /**
     * 主方法（程序启动入口）
     * @param args 命令行参数（本程序未使用）
     */
    public static void main(String[] args) {
        // 在事件分发线程中启动GUI（Swing规范要求）
        SwingUtilities.invokeLater(() -> {
            //━━━━━━ 玩家数量输入 ━━━━━━//
            // 弹出输入对话框获取玩家数量
            String input = JOptionPane.showInputDialog(
                null, 
                "请输入参与人数（2-6）：", 
                "多人俄罗斯轮盘", 
                JOptionPane.QUESTION_MESSAGE
            );
            // 转换输入为整数（未做输入验证）
            int playerCount = Integer.parseInt(input);
            
            //━━━━━━ 玩家初始化 ━━━━━━//
            List<Player> players = new ArrayList<>(); // 玩家列表
            for (int i = 1; i <= playerCount; i++) {
                // 创建玩家实例（名称格式："玩家1","玩家2"...）
                players.add(new Player("玩家" + i));
            }
            
            //━━━━━━ 游戏窗口设置 ━━━━━━//
            // 创建主窗口
            JFrame frame = new JFrame("俄罗斯轮盘 - 赏金赛");
            // 设置关闭行为（退出程序）
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // 设置窗口尺寸（800x600像素）
            frame.setSize(800, 600);
            // 添加游戏主面板（传递玩家列表）
            frame.add(new GamePanel(players));
            // 窗口居中显示
            frame.setLocationRelativeTo(null);
            // 设为可见（启动GUI渲染）
            frame.setVisible(true);
        });
    }
}