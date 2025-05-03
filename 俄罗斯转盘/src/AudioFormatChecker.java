// src/AudioFormatChecker.java
import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
//javawork
public class AudioFormatChecker {
    // 定义Java支持的音频格式标准
    private static final AudioFormat REQUIRED_FORMAT = new AudioFormat(
        AudioFormat.Encoding.PCM_SIGNED, // 编码格式
        44100.0f,                       // 采样率
        16,                             // 位深
        2,                              // 声道数
        4,                              // 帧大小（位深*声道数/8）
        44100.0f,                       // 帧率
        false                           // 小端字节序
    );

    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("请选择音频文件（.wav）");
        
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            checkFormatCompatibility(file);
        }
    }

    private static void checkFormatCompatibility(File file) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            AudioFormat actualFormat = stream.getFormat();
            stream.close();

            // 构建对比报告
            String report = buildFormatReport(actualFormat, file);
            boolean isCompatible = isFormatCompatible(actualFormat);

            // 显示结果
            JOptionPane.showMessageDialog(null, report,
                isCompatible ? "✅ 格式兼容" : "❌ 格式不兼容",
                isCompatible ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);

        } catch (UnsupportedAudioFileException e) {
            showError("⚠️ 不支持的音频格式", file, "文件扩展名不是有效的WAV格式");
        } catch (Exception e) {
            showError("⛔ 文件读取失败", file, e.getMessage());
        }
    }

    /**
     * 生成格式对比报告（严格匹配图片中的错误样式）
     */
    private static String buildFormatReport(AudioFormat actual, File file) {
        return String.format(
            "文件名称: %s\n"
            + "文件路径: %s\n\n"
            + "=== 当前格式 ===\n"
            + "编码类型: %s\n"
            + "位深: %d位\n"
            + "采样率: %.1fHz\n"
            + "声道数: %d\n\n"
            + "=== 要求格式 ===\n"
            + "编码类型: %s\n"
            + "位深: %d位\n"
            + "采样率: %.1fHz\n"
            + "声道数: %d",
            file.getName(),
            file.getAbsolutePath(),
            actual.getEncoding(),
            actual.getSampleSizeInBits(),
            actual.getSampleRate(),
            actual.getChannels(),
            REQUIRED_FORMAT.getEncoding(),
            REQUIRED_FORMAT.getSampleSizeInBits(),
            REQUIRED_FORMAT.getSampleRate(),
            REQUIRED_FORMAT.getChannels()
        );
    }

    /**
     * 格式兼容性验证（严格匹配图片中的错误类型）
     */
    private static boolean isFormatCompatible(AudioFormat format) {
        return format.getEncoding().equals(REQUIRED_FORMAT.getEncoding())
            && format.getSampleSizeInBits() == REQUIRED_FORMAT.getSampleSizeInBits()
            && Math.abs(format.getSampleRate() - REQUIRED_FORMAT.getSampleRate()) < 0.01
            && format.getChannels() == REQUIRED_FORMAT.getChannels();
    }

    /**
     * 错误提示（完全复现图片中的UI样式）
     */
    private static void showError(String title, File file, String reason) {
        String message = String.format(
            "%s\n\n"
            + "文件名称: %s\n"
            + "文件路径: %s\n\n"
            + "错误原因: %s",
            title, file.getName(), file.getAbsolutePath(), reason
        );

        JOptionPane.showMessageDialog(null, message,
            "资源错误", JOptionPane.ERROR_MESSAGE);
    }
}