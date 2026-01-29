package org.springblade.modules.sp.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

/**
 * 验证码工具类
 * 
 * @author Yi
 * @since 2024-11-12
 */
public class CaptchaUtil {
    
    // 验证码字符集（数字+大小写字母，去掉易混淆的0OoIl1）
    private static final String CHAR_SET = "23456789ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz";
    
    // 验证码长度
    private static final int CODE_LENGTH = 4;
    
    // 图片宽度
    private static final int WIDTH = 120;
    
    // 图片高度
    private static final int HEIGHT = 40;
    
    // 干扰线数量
    private static final int LINE_COUNT = 3;
    
    // 噪点数量
    private static final int NOISE_COUNT = 50;
    
    private static final Random random = new Random();
    
    /**
     * 生成随机验证码文本
     */
    public static String generateCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHAR_SET.charAt(random.nextInt(CHAR_SET.length())));
        }
        return code.toString();
    }
    
    /**
     * 生成验证码图片并转换为Base64
     * 
     * @param code 验证码文本
     * @return Base64编码的图片字符串
     */
    public static String generateImageBase64(String code) {
        try {
            BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            
            // 设置抗锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // 填充背景色
            g.setColor(new Color(240, 240, 240));
            g.fillRect(0, 0, WIDTH, HEIGHT);
            
            // 绘制边框
            g.setColor(new Color(200, 200, 200));
            g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);

            
            // 绘制验证码文本
            drawCode(g, code);
            
            g.dispose();
            
            // 转换为Base64
            return imageToBase64(image);
            
        } catch (Exception e) {
            throw new RuntimeException("生成验证码图片失败", e);
        }
    }
    
    /**
     * 绘制验证码文本
     */
    private static void drawCode(Graphics2D g, String code) {
        // 字体列表
        String[] fontNames = {"Arial", "Times New Roman", "Courier New"};
        
        int x = 15;
        for (int i = 0; i < code.length(); i++) {
            // 随机字体
            String fontName = fontNames[random.nextInt(fontNames.length)];
            // 随机样式（普通、粗体、斜体）
            int fontStyle = random.nextInt(3);
            // 字体大小
            int fontSize = 24 + random.nextInt(6);
            
            Font font = new Font(fontName, fontStyle, fontSize);
            g.setFont(font);
            
            // 随机颜色（深色）
            g.setColor(new Color(random.nextInt(100), random.nextInt(100), random.nextInt(100)));
            
            // 随机旋转角度（-15度到15度）
            double angle = (random.nextDouble() - 0.5) * 0.5;
            
            // 计算字符位置
            int y = HEIGHT / 2 + fontSize / 3 + random.nextInt(8) - 4;
            
            g.rotate(angle, x, y);
            g.drawString(String.valueOf(code.charAt(i)), x, y);
            g.rotate(-angle, x, y);
            
            x += (WIDTH - 30) / CODE_LENGTH;
        }
    }
    
    /**
     * 绘制干扰线
     */
    private static void drawInterferenceLines(Graphics2D g) {
        for (int i = 0; i < LINE_COUNT; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            
            // 随机颜色（浅色）
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g.setStroke(new BasicStroke(1.5f));
            g.drawLine(x1, y1, x2, y2);
        }
    }
    
    /**
     * 绘制噪点
     */
    private static void drawNoise(Graphics2D g) {
        for (int i = 0; i < NOISE_COUNT; i++) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            
            // 随机颜色
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g.fillRect(x, y, 2, 2);
        }
    }
    
    /**
     * 将BufferedImage转换为Base64字符串
     */
    private static String imageToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] bytes = baos.toByteArray();
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
    }
}
