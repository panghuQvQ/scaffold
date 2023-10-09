package com.wang.scaffold.files.image;

import com.wang.scaffold.files.imgscalr.Scalr;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class Watermark {

    /**
     * @param pathname   图片文件路径
     * @param watermark  水印文字
     * @param fontSize   水印字体大小
     * @param color      水印颜色
     * @param targetSize 输出图片大小(固定宽度，自动高度)
     * @param saveTo     保存到文件，为null则替换原文件
     */
    public static void writeTextCenter(String pathname, String watermark, int fontSize, Color color, int targetSize, String saveTo)
            throws IOException {
        BufferedImage img = null;
        File file = null;
        String extension = null;
        try {
            file = new File(pathname);
            extension = FilenameUtils.getExtension(pathname);
            img = ImageIO.read(file);
        } catch (IOException e) {
            log.error("水印失败，读取文件失败: File:" + pathname);
            throw e;
        }

        // resize to targetSize
        BufferedImage temp = Scalr.resize(img, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, targetSize);
        // Create graphics object and add original image to it
        Graphics2D graphics = (Graphics2D) temp.getGraphics();
        graphics.drawImage(temp, 0, 0, null);

//        Font font = new Font(Font.SANS_SERIF, Font.BOLD, fontSize);
        Font font = loadWaterMarkFont().deriveFont(Font.BOLD, fontSize);
        // Set font for the watermark text
        graphics.setFont(font);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(color == null ? new Color(222, 66, 66, 120) : color);

        // get metrics from the graphics
        FontMetrics metrics = graphics.getFontMetrics(font);
        // get the height of a line of text in this font and render context
        int hgt = metrics.getHeight();
        // get the advance of my text in this font and render context
        int adv = metrics.stringWidth(watermark);
        // calculate the size of a box to hold the
        // text with some padding.
        Dimension size = new Dimension(adv + 2, hgt + 2);
        double x = temp.getWidth() / 2.0 - size.getWidth() / 2.0;
        double y = temp.getHeight() / 2.0;
        // Rotate the watermark text
        double degree = -20.00;
        double theta = degree / 180 * Math.PI;
        graphics.rotate(theta, temp.getWidth() / 2.0, temp.getHeight() / 2.0);
        graphics.drawString(watermark, (int) x, (int) y);

        // releases any system resources that it is using
        graphics.dispose();

        // JPEG 无法处理 ALPHA channel
        if (temp.getColorModel().hasAlpha() &&
                (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg"))
        ) {
            extension = "png";
        }
        ImageIO.write(temp, extension, saveTo != null ? new File(saveTo) : file);
    }

    private static Font loadWaterMarkFont() {
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT,
                    Objects.requireNonNull(Watermark.class.getResourceAsStream("/fonts/HongLeiXingShuJianTi-2.otf")));
        } catch (Exception e) {
            log.error("加载字体失败");
            throw new RuntimeException(e);
        }
        return font;
    }

//    public static void main(String[] args) {
//        try {
//            Watermark.writeTextCenter(
//                    "/Users/weizhou/devfiles/testFiles/id.jpeg",
//                    "博宇鑫人力资源专用", 42, null, 440,
//                    "/Users/weizhou/devfiles/testFiles/id_watermarked.jpeg");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
