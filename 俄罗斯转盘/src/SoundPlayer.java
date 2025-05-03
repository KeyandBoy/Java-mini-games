// src/SoundPlayer.java
import javax.sound.sampled.*;
import java.io.File;
import javax.swing.JOptionPane;

public class SoundPlayer {
    
    /**java作业
     * 播放音效方法（修复错误提示版）
     * @param filename 声音文件名（对应resources/sounds目录）
     */
    public void playSound(String filename) {
        new Thread(() -> {
            File file = null;
            try {
                //━━━━━━ 资源路径处理 ━━━━━━//
                file = new File("resources/sounds/" + filename);
                System.out.println("[DEBUG] 加载音效: " + file.getAbsolutePath());

                //━━━━━━ 文件存在性验证 ━━━━━━//
                if (!file.exists()) {
                    throw new IllegalArgumentException("❌ 文件不存在: " + file.getName());
                }

                //━━━━━━ 音频流处理 ━━━━━━//
                AudioInputStream stream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(stream);

                //━━━━━━ 资源释放保障 ━━━━━━//
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                        try { stream.close(); } catch (Exception e) {}
                    }
                });
                clip.start();
                
            } catch (UnsupportedAudioFileException e) {
                // 关键修复：修正错误信息中的英文描述
                showError("🚫 不支持的音频格式: " + filename.toUpperCase() + "\n(File of UNSUPPORTED format)", file);
            } catch (LineUnavailableException e) {
                showError("🔊 音频设备被占用，请关闭其他声音程序", file);
            } catch (IllegalArgumentException e) {
                showError(e.getMessage(), file);
            } catch (Exception e) {
                showError("❓ 未知错误: " + e.getClass().getSimpleName(), file);
            }
        }).start();
    }

    /**
     * 增强版错误提示（显示具体文件信息）
     */
    private void showError(String message, File file) {
        String errorDetails = "【错误详情】\n"
                + message + "\n\n"
                + "文件名: " + (file != null ? file.getName() : "未知文件") + "\n"
                + "完整路径: " + (file != null ? file.getAbsolutePath() : "路径无效");

        JOptionPane.showMessageDialog(
            null, 
            errorDetails,
            "资源错误", 
            JOptionPane.ERROR_MESSAGE
        );
    }
}