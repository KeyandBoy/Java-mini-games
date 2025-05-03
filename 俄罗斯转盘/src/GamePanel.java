// src/GamePanel.java
import javax.swing.*;                  // 图形界面组件
import javax.sound.sampled.*;          // 音频处理
import java.awt.*;                     // 图形绘制
import java.io.File;                   // 文件操作
import java.util.List;                 // 列表数据结构
import java.util.stream.Collectors;    // 流式数据处理
//javawork
public class GamePanel extends JPanel { // 继承JPanel的游戏主面板

    // 资源路径常量
    private static final String IMG_PATH = "resources/images/";// 图片子目录
    private static final String SOUND_PATH = "resources/sounds/";// 音效子目录
    
    // 图片资源加载
    private final ImageIcon revolverIcon = loadImage("revolver.png");
private final ImageIcon skullIcon = loadImage("SKull.png");     // 骷髅图标
    private final JLabel revolverImage = new JLabel();                // 图片显示组件
    
    // 音频文件列表已删除，因为未使用
    
    // 游戏组件
    private final JTextArea infoArea = new JTextArea();     // 信息显示文本框
    private final List<Player> players;                     // 玩家列表
    private int currentPlayerIndex;                         // 当前玩家索引
    private final Revolver revolver = new Revolver();       // 左轮手枪实例

    public GamePanel(List<Player> players) {  // 构造方法
        this.players = players;              // 初始化玩家列表
        setLayout(new BorderLayout());        // 设置边界布局
        initUI();                             // 初始化界面
        startGame();                          // 开始游戏循环
    }

    private void initUI() {                   // 界面初始化方法
        // 左轮图片显示设置
        revolverImage.setIcon(revolverIcon);               // 设置初始图标
        revolverImage.setHorizontalAlignment(JLabel.CENTER);// 居中显示
        add(revolverImage, BorderLayout.CENTER);            // 添加至面板中央

        // 信息区域设置
        infoArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));// 设置字体
        add(new JScrollPane(infoArea), BorderLayout.SOUTH); // 添加滚动文本框至底部
    }

    private ImageIcon loadImage(String filename) {  // 图片加载方法
        File imgFile = new File(IMG_PATH + filename);// 构建文件路径
        if (!imgFile.exists()) {                    // 检查文件是否存在
            showError("图片缺失: " + imgFile.getAbsolutePath());// 显示错误
        }
        return new ImageIcon(imgFile.getAbsolutePath());// 返回加载的图片
    }

    private void playSound(String filename) {       // 音效播放方法
        new Thread(() -> {                          // 新建线程避免阻塞UI
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(// 获取音频流
                    new File(SOUND_PATH + filename));
                Clip clip = AudioSystem.getClip();  // 创建音频剪辑
                clip.open(ais);                     // 打开音频流
                clip.start();                       // 开始播放
            } catch (Exception ex) {
                showError("音效错误: " + ex.getMessage());// 异常处理
            }
        }).start();
    }

    // 游戏主循环
    private void startGame() {                      // 启动游戏线程
        new Thread(() -> {                          // 避免阻塞事件分发线程
            while (getAliveCount() > 1) {           // 存活玩家>1时继续游戏
                Player current = getNextPlayer();    // 获取下一个玩家
                processTurn(current);               // 处理玩家回合
            }
            showFinalResult();                      // 显示最终结果
        }).start();
    }

    private void processTurn(Player player) {       // 处理单个玩家回合
        appendInfo("\n--- " + player.getName() + "的回合 ---");// 显示回合信息
        
        // 获取玩家输入
        String input = JOptionPane.showInputDialog(  // 弹出输入对话框
            revolverImage,                          // 父组件
            player.getName() + "请输入装弹数（0-6）",// 提示信息
            "回合操作",                              // 对话框标题
            JOptionPane.QUESTION_MESSAGE            // 问题类型
        );

        if (input == null) input = "0";             // 处理取消输入的情况
        int bullets = Integer.parseInt(input);      // 转换输入为整数
        
        // 装弹和旋转
        revolver.loadBullet(bullets);               // 装载指定数量子弹
        revolver.spinChamber();                     // 随机旋转弹膛
        playSound("spin.wav");                      // 播放旋转音效
        
        // 执行射击
        ShootResult result = revolver.shoot();      // 获取射击结果
        updateUI(player, bullets, result);          // 更新界面显示
        checkGameStatus(player, bullets, result);   // 检查游戏状态
    }

    private void updateUI(Player player, int bullets, ShootResult result) {
        SwingUtilities.invokeLater(() -> {          // 在事件线程更新UI
            revolverImage.setIcon(result.isHit() ? skullIcon : revolverIcon);// 更新图标
            appendInfo("装弹数：" + bullets);         // 显示装弹数
            appendInfo("结果：" + getResultText(player, bullets, result));// 显示结果
            updatePlayerStatus();                   // 更新玩家状态
        });
    }
    private String getResultText(Player player, int bullets, ShootResult result) {
        if (bullets == 6) {                        // 装满子弹的特殊情况
            return result.isJammed() ?              
                "卡壳！获得1000赏金" : "装满子弹被击中！";
        }
        return result.isHit() ?                    // 普通情况
            "被子弹击中！" : "安全，获得" + (bullets * 100) + "赏金";
    }

    private void checkGameStatus(Player player, int bullets, ShootResult result) {
        if (result.isJammed()) {                   // 卡壳情况
            player.addBounty(1000);                // 增加赏金
            playSound("GAMEover.wav");             // 播放结束音效
        } else if (result.isHit()) {               // 被击中
            player.setAlive(false);                // 标记玩家死亡
            playSound("hit.wav");                  // 播放击中音效
        } else {                                   // 安全
            player.addBounty(bullets == 0 ? 50 : bullets * 100);// 计算赏金
        }
    }

    // 辅助方法
    private void appendInfo(String text) {         // 追加文本信息
        infoArea.append("\n" + text);
    }

    private void updatePlayerStatus() {            // 更新玩家状态显示
        StringBuilder sb = new StringBuilder("\n当前状态：");
        players.forEach(p -> sb.append("\n")       // 遍历所有玩家
            .append(p.getName()).append(": ")      // 显示名称
            .append(p.getBounty()).append("赏金 | ") // 显示赏金
            .append(p.isAlive() ? "存活" : "死亡"));// 显示存活状态
        infoArea.setText(sb.toString());           // 更新文本框内容
    }

    private int getAliveCount() {                  // 获取存活玩家数量
        return (int) players.stream()              // 使用流处理
            .filter(Player::isAlive)               // 过滤存活玩家
            .count();                              // 统计数量
    }

    private Player getNextPlayer() {               // 获取下一个存活玩家
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();// 循环索引
        } while (!players.get(currentPlayerIndex).isAlive());// 跳过死亡玩家
        return players.get(currentPlayerIndex);    // 返回存活玩家
    }

    private void showFinalResult() {               // 显示最终排名
        List<Player> ranking = players.stream()    // 创建玩家流
            .sorted((p1, p2) -> p2.getBounty() - p1.getBounty())// 按赏金降序排序
            .collect(Collectors.toList());         // 转换为列表

        StringBuilder result = new StringBuilder("最终排名：\n");
        for (int i = 0; i < ranking.size(); i++) { // 遍历排名
            result.append(i + 1).append(". ")      // 排名序号
                .append(ranking.get(i).getName())  // 玩家名称
                .append(" - ").append(ranking.get(i).getBounty()).append("\n");// 赏金
        }

        revolverImage.setIcon(skullIcon);          // 显示骷髅图标
        JOptionPane.showMessageDialog(this, result.toString());// 弹出结果对话框
    }

    private void showError(String msg) {           // 错误提示方法
        JOptionPane.showMessageDialog(this, msg, "资源错误", JOptionPane.ERROR_MESSAGE);
        System.exit(1);                            // 终止程序
    }
}