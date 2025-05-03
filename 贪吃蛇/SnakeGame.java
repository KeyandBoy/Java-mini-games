import javax.swing.*;                   // 导入Swing组件库
import java.awt.*;                      // 导入AWT图形库
import java.awt.event.*;                // 导入事件处理库
import java.util.LinkedList;           // 导入链表数据结构
import java.util.Random;               // 导入随机数工具

public class SnakeGame extends JFrame { // 主窗口继承JFrame
    private GamePanel gamePanel;        // 游戏主画布对象
    private JPanel startPanel;          // 开始界面容器
    private JLabel scoreLabel;          // 得分显示组件
    
    public SnakeGame() {                // 主窗口构造函数
        initStartScreen();              // 初始化开始界面
        setTitle("贪吃蛇游戏");         // 设置窗口标题
        setSize(900, 720);              // 设置窗口尺寸(宽900,高720)
        setDefaultCloseOperation(EXIT_ON_CLOSE); // 设置关闭按钮行为
        setLocationRelativeTo(null);    // 窗口居中屏幕
        setResizable(false);            // 禁止调整窗口大小
    }

    private void initStartScreen() {    // 初始化开始界面方法
        startPanel = new JPanel(new BorderLayout()); // 创建带边框布局的面板
        JButton startButton = new JButton("开始游戏"); // 创建开始按钮
        startButton.setFont(new Font("微软雅黑", Font.BOLD, 40)); // 设置按钮字体
        startButton.addActionListener(e -> startGame()); // 添加按钮点击监听
        
        JLabel titleLabel = new JLabel("贪吃蛇", SwingConstants.CENTER); // 创建标题标签
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 60)); // 设置标题字体
        titleLabel.setForeground(new Color(0, 100, 0)); // 设置标题颜色(深绿)
        
        startPanel.add(titleLabel, BorderLayout.CENTER); // 添加标题到中心区域
        startPanel.add(startButton, BorderLayout.SOUTH); // 添加按钮到底部
        startPanel.setBackground(new Color(240, 240, 240)); // 设置背景色(浅灰)
        add(startPanel);                // 将开始界面添加到主窗口
    }

    private void startGame() {          // 开始游戏方法
        remove(startPanel);             // 移除开始界面组件
        gamePanel = new GamePanel();    // 创建游戏画布实例
        scoreLabel = new JLabel("得分: 0", SwingConstants.CENTER); // 初始化得分标签
        scoreLabel.setFont(new Font("微软雅黑", Font.BOLD, 24)); // 设置得分字体
        scoreLabel.setForeground(Color.BLUE); // 设置得分颜色
        
        add(scoreLabel, BorderLayout.NORTH); // 添加得分标签到窗口顶部
        add(gamePanel);                 // 添加游戏画布到中心区域
        revalidate();                   // 刷新布局
        gamePanel.requestFocus();      // 让画布获得焦点(接收键盘事件)
    }

    class GamePanel extends JPanel implements ActionListener, KeyListener {
        private final int CELL_SIZE = 25;     // 单元格像素大小
        private final int GRID_WIDTH = 34;   // 横向单元格数量(34 * 25=850)
        private final int GRID_HEIGHT = 24;   // 纵向单元格数量(24 * 25=600)
        private LinkedList<Point> snake;      // 用链表存储蛇身坐标
        private Point food;                   // 食物坐标对象
        private Direction direction = Direction.RIGHT; // 初始移动方向
        private Timer timer = new Timer(150, this); // 游戏循环定时器(150ms间隔)
        private boolean isRunning = false;    // 游戏运行状态标志
        private Random random = new Random(); // 随机数生成器

        enum Direction { UP, DOWN, LEFT, RIGHT } // 方向枚举定义

        public GamePanel() {            // 游戏画布构造函数
            setBackground(new Color(245, 245, 245)); // 设置背景色(浅灰)
            addKeyListener(this);       // 注册键盘事件监听器
            initGame();                 // 初始化游戏数据
        }

        private void initGame() {       // 初始化游戏数据方法
            snake = new LinkedList<>(); // 创建空链表
            snake.add(new Point(5, 5)); // 添加初始蛇头坐标(第6列,第6行)
            generateFood();             // 生成首个食物
            isRunning = true;           // 设置游戏运行状态
            timer.start();              // 启动定时器
        }

        @Override
        protected void paintComponent(Graphics g) { // 重写绘制方法
            super.paintComponent(g);   // 调用父类绘制逻辑
            drawGrid(g);               // 绘制网格线
            drawSnake(g);              // 绘制蛇身
            drawFood(g);               // 绘制食物
        }

        private void drawGrid(Graphics g) { // 绘制网格线方法
            g.setColor(new Color(220, 220, 220)); // 设置网格颜色(浅灰)
            for (int x = 0; x < GRID_WIDTH; x++) { // 横向遍历网格
                for (int y = 0; y < GRID_HEIGHT; y++) { // 纵向遍历网格
                    g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE); // 绘制单元格边框
                }
            }
        }

        private void drawSnake(Graphics g) { // 绘制蛇身方法
            g.setColor(new Color(0, 150, 0)); // 设置蛇身颜色(深绿)
            for (Point p : snake) {     // 遍历蛇身坐标
                // 绘制圆角矩形表示身体
                g.fillRoundRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE, 5, 5);
            }
            // 绘制蛇头（颜色更亮）
            g.setColor(new Color(0, 200, 0)); 
            Point head = snake.getFirst(); // 获取头部坐标
            g.fillRoundRect(head.x * CELL_SIZE, head.y * CELL_SIZE, CELL_SIZE, CELL_SIZE, 5, 5);
        }

        private void drawFood(Graphics g) { // 绘制食物方法
            g.setColor(Color.RED);      // 设置食物颜色
            // 绘制圆形食物
            g.fillOval(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        private void generateFood() {   // 生成食物方法
            do {                        // 循环直到找到合适位置
                food = new Point(       // 创建新食物坐标
                    random.nextInt(GRID_WIDTH),  // 随机X坐标(0-33)
                    random.nextInt(GRID_HEIGHT)  // 随机Y坐标(0-23)
                );
            } while (snake.contains(food)); // 避免食物出现在蛇身上
        }

        @Override
        public void actionPerformed(ActionEvent e) { // 定时器事件处理方法
            if (!isRunning) return;     // 游戏未运行时直接返回
            
            Point head = snake.getFirst(); // 获取当前蛇头坐标
            Point newHead = new Point(head); // 创建新头坐标副本
            
            // 根据方向计算新坐标
            switch (direction) {
                case UP:    newHead.y--; break; // 上移：Y坐标减1
                case DOWN:  newHead.y++; break; // 下移：Y坐标加1
                case LEFT:  newHead.x--; break; // 左移：X坐标减1
                case RIGHT: newHead.x++; break; // 右移：X坐标加1
            }

            if (checkCollision(newHead)) { // 碰撞检测
                gameOver();         // 触发游戏结束逻辑
                return;             // 提前返回
            }

            snake.addFirst(newHead); // 将新头添加到链表首部
            if (newHead.equals(food)) { // 如果吃到食物
                scoreLabel.setText("得分: " + (snake.size() - 1) * 10); // 更新得分显示
                generateFood();     // 生成新食物
            } else {
                snake.removeLast(); // 未吃到则移除尾部
            }
            repaint();              // 请求重绘画布
        }

        private boolean checkCollision(Point head) { // 碰撞检测方法
            // 判断是否触碰边界
            boolean wallCollision = head.x < 0 || head.x >= GRID_WIDTH || 
                                   head.y < 0 || head.y >= GRID_HEIGHT;
            // 判断是否触碰自身
            boolean selfCollision = snake.contains(head);
            return wallCollision || selfCollision; // 返回碰撞结果
        }

        private void gameOver() {   // 游戏结束处理方法
            timer.stop();           // 停止定时器
            isRunning = false;      // 更新游戏状态
            
            // 弹出结束对话框
            int choice = JOptionPane.showOptionDialog(
                this, 
                "得分: " + (snake.size() - 1) * 10, // 显示最终得分
                "游戏结束", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.INFORMATION_MESSAGE,
                null, 
                new Object[]{"重新开始", "退出游戏"}, // 对话框按钮
                "重新开始"           // 默认选中按钮
            );
            
            if (choice == 0) {     // 用户选择重新开始
                initGame();         // 重置游戏数据
            } else {                // 用户选择退出
                System.exit(0);     // 终止程序
            }
        }

        @Override
        public void keyPressed(KeyEvent e) { // 键盘按下事件处理
            switch (e.getKeyCode()) {       // 根据按键代码处理
                case KeyEvent.VK_UP:         // 上方向键
                    if (direction != Direction.DOWN) direction = Direction.UP;
                    break;
                case KeyEvent.VK_DOWN:       // 下方向键
                    if (direction != Direction.UP) direction = Direction.DOWN;
                    break;
                case KeyEvent.VK_LEFT:       // 左方向键
                    if (direction != Direction.RIGHT) direction = Direction.LEFT;
                    break;
                case KeyEvent.VK_RIGHT:      // 右方向键
                    if (direction != Direction.LEFT) direction = Direction.RIGHT;
                    break;
            }
        }

        @Override 
        public void keyTyped(KeyEvent e) {}  // 必须实现的空方法（未使用）
        
        @Override 
        public void keyReleased(KeyEvent e) {} // 必须实现的空方法（未使用）
    }

    public static void main(String[] args) { // 程序入口
        new SnakeGame().setVisible(true);    // 创建窗口并显示
    }
}