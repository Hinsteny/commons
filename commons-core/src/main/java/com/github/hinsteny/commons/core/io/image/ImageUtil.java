package com.github.hinsteny.commons.core.io.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 图片裁剪, 变形, 加水印工具类
 *
 * @author Hinsteny
 * @version ImageUtil: ImageUtil 2019-05-09 15:12 All rights reserved.$
 */
public class ImageUtil {

    private static final Logger LOGGER = LogManager.getLogger(ImageUtil.class);

    /**
     * 缩放图像至 指定的高度和宽度
     *
     * @param srcImageFile 源图像文件地址
     * @param targetImageFile 缩放后的图像地址
     * @param height 缩放后的高度
     * @param width 缩放后的宽度
     * @return result
     */
    public static boolean scale(String srcImageFile, String targetImageFile, int width, int height) {
        boolean result = false;
        try {
            // 读入文件
            BufferedImage src = ImageIO.read(getImageFile(srcImageFile));
            Image image = src.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            // 绘制缩小后的图
            g.drawImage(image, 0, 0, null);
            g.dispose();
            // 输出到文件流
            ImageIO.write(tag, ImageType.JPG.getName(), new File(targetImageFile));
            result = true;
        } catch (IOException e) {
            LOGGER.error(String.format("ImageUtil scale from %s to %s failed!", srcImageFile, targetImageFile), e);
        }
        return result;
    }

    /**
     * 缩放图像（按比例缩放）,改变图像分辨率
     *
     * @param srcImageFile 源图像文件地址
     * @param targetImageFile 缩放后的图像地址
     * @param scale 缩放比例
     * @param flag 缩放选择:true 放大; false 缩小;
     * @return result
     */
    public static boolean scaleByRatio(String srcImageFile, String targetImageFile, int scale, boolean flag) {
        boolean result = false;
        try {
            // 读入文件
            BufferedImage src = ImageIO.read(getImageFile(srcImageFile));
            // 得到源图宽
            int width = src.getWidth();
            // 得到源图长
            int height = src.getHeight();
            //放大或者缩小
            width = flag ? width * scale : width / scale;
            height = flag ? height * scale : height / scale;
            Image image = src.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            // 绘制缩小后的图
            g.drawImage(image, 0, 0, null);
            g.dispose();
            // 输出到文件流
            ImageIO.write(tag, ImageType.JPG.getName(), new File(targetImageFile));
            result = true;
        } catch (IOException e) {
            LOGGER.error(String.format("ImageUtil scaleByRatio from %s to %s failed!", srcImageFile, targetImageFile), e);
        }
        return result;
    }

    /**
     * 按原图高宽比缩放图像至 指定高度和宽度(优先满足所给width/height 更贴近原图比例的一方)
     *
     * @param srcImageFile 源图像文件地址
     * @param targetImageFile 缩放后的图像地址
     * @param height 缩放后的高度
     * @param width 缩放后的宽度
     * @param bb 宽高比与原图不同时是否需要补白：true为补白; false为不补白;
     * @return result
     */
    public static boolean scaleBySize(String srcImageFile, String targetImageFile, int width, int height, boolean bb) {
        boolean result = false;
        try {
            if (width == 0 || height == 0) {
                throw new IllegalArgumentException("Width (" + width + ") and height (" + height + ") cannot be = 0");
            }
            BufferedImage bi = ImageIO.read(getImageFile(srcImageFile));
            // 计算比例
            double ratio, ratiox, ratioy; // 缩放比例
            ratiox = (Integer.valueOf(width)).doubleValue() / bi.getWidth();
            ratioy = (Integer.valueOf(height)).doubleValue() / bi.getHeight();

            ratio = (ratiox > ratioy) ? ratiox : ratioy;
            AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
            Image itemp = op.filter(bi, null);
            if (bb) {
                itemp = pdddingImage(itemp, width, height);
            }
            ImageIO.write((BufferedImage) itemp, ImageType.JPG.getName(), new File(targetImageFile));
            result = true;
        } catch (IOException e) {
            LOGGER.error(String.format("ImageUtil scaleBySize from %s to %s failed!", srcImageFile, targetImageFile), e);
        }
        return result;
    }

    /**
     * 图像切割(按指定起点坐标和宽高切割)
     *
     * @param srcImageFile 源图像地址
     * @param targetImageFile 切片后的图像地址
     * @param incise 目标切片的起始坐标和长宽大小
     * @return result
     */
    public static boolean cut(String srcImageFile, String targetImageFile, Incise incise) {
        boolean result = false;
        try {
            // 读取源图像
            BufferedImage bi = ImageIO.read(getImageFile(srcImageFile));
            // 源图宽度
            int srcWidth = bi.getHeight();
            // 源图高度
            int srcHeight = bi.getWidth();
            if (srcWidth > 0 && srcHeight > 0) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                // 四个参数分别为图像起点坐标和宽高
                ImageFilter cropFilter = new CropImageFilter(incise.getX(), incise.getY(), incise.getWidth(), incise.getHeight());
                Image img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
                BufferedImage tag = new BufferedImage(incise.getWidth(), incise.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                // 绘制切割后的图
                g.drawImage(img, 0, 0, incise.getWidth(), incise.getHeight(), null);
                g.dispose();
                // 输出为文件
                ImageIO.write(tag, ImageType.JPG.getName(), new File(targetImageFile));
                result = true;
            }
        } catch (Exception e) {
            LOGGER.error(String.format("ImageUtil cut from %s to %s failed!", srcImageFile, targetImageFile), e);
        }
        return result;
    }

    /**
     * 图像类型转换：GIF-》JPG、GIF-》PNG、PNG-》JPG、PNG-》GIF(X)、BMP-》PNG
     *
     * @param srcImageFile 源图像地址
     * @param srcType 源图片格式, 包含格式非正式名称的 String：如JPG、JPEG、GIF等
     * @param destImageFile 目标图像地址
     * @return result
     */
    public final static boolean convert(String srcImageFile, ImageType srcType, String destImageFile) {
        boolean result = false;
        try {
            BufferedImage src = ImageIO.read(getImageFile(srcImageFile));
            ImageIO.write(src, srcType.getName(), new File(destImageFile));
            result = true;
        } catch (Exception e) {
            LOGGER.error(String.format("ImageUtil convert from %s to %s failed!", srcImageFile, destImageFile), e);
        }
        return result;
    }

    /**
     * 彩色转为黑白
     *
     * @param srcImageFile 源图像地址
     * @param srcType 源图像类型
     * @param destImageFile 目标图像地址
     * @return result
     */
    public final static boolean gray(String srcImageFile, ImageType srcType, String destImageFile) {
        boolean result = false;
        try {
            BufferedImage src = ImageIO.read(getImageFile(srcImageFile));
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            ColorConvertOp op = new ColorConvertOp(cs, null);
            src = op.filter(src, null);
            ImageIO.write(src, srcType.getName(), new File(destImageFile));
            result = true;
        } catch (IOException e) {
            LOGGER.error(String.format("ImageUtil gray from %s to %s failed!", srcImageFile, destImageFile), e);
        }
        return result;
    }

    /**
     * 给图片添加文字水印
     *
     * @param srcImageFile 源图像地址
     * @param destImageFile 目标图像地址
     * @param text 水印文字及属性
     * @param posi 文字坐标及透明度
     * @return result
     */
    public final static boolean pressText(String srcImageFile, String destImageFile, Text text, MaskPosi posi) {
        boolean result = false;
        try {
            Image src = ImageIO.read(getImageFile(srcImageFile));
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setColor(text.getColor());
            g.setFont(text.getFont());
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, posi.getAlpha()));
            // 在指定坐标绘制水印文字
            g.drawString(text.getText(), posi.getX_posi(), posi.getY_posi());
            g.dispose();
            // 输出到文件流
            ImageIO.write(image, ImageType.JPG.getName(), new File(destImageFile));
            result = true;
        } catch (Exception e) {
            LOGGER.error(String.format("ImageUtil pressText from %s to %s failed!", srcImageFile, destImageFile), e);
        }
        return result;
    }

    /**
     * 给图片添加图片水印
     *
     * @param maskImg 水印图片
     * @param srcImageFile 源图像地址
     * @param destImageFile 目标图像地址
     * @param posi 目标图像坐标及透明度
     * @return result
     */
    public final static boolean pressImage(String maskImg, String srcImageFile, String destImageFile, MaskPosi posi) {
        boolean result = false;
        try {
            Image src = ImageIO.read(getImageFile(srcImageFile));
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);
            // 水印文件
            Image mask = ImageIO.read(new File(maskImg));
            int wideth_x = mask.getWidth(null);
            int height_y = mask.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, posi.getAlpha()));
            g.drawImage(mask, posi.getX_posi(), posi.getY_posi(), wideth_x, height_y, null);
            // 水印文件结束
            g.dispose();
            ImageIO.write(image, ImageType.JPG.getName(), new File(destImageFile));
            result = true;
        } catch (Exception e) {
            LOGGER.error(String.format("ImageUtil pressImage from %s to %s failed!", srcImageFile, destImageFile), e);
        }
        return result;
    }

    /**
     * 创建图片缩略图(等比缩放 无失真缩放)
     *
     * @param srcImageFile 源图片文件完整路径
     * @param destImageFile 目标图片文件完整路径
     * @param width 缩放的宽度
     * @param height 缩放的高度
     * @param flag true 按照实际长宽输出  如果 false 按照比例进行无失真压缩
     * @return result
     */
    public static boolean createThumbnail(String srcImageFile, String destImageFile, float width, float height, boolean flag) {
        boolean result = false;
        try {
            BufferedImage image = ImageIO.read(getImageFile(srcImageFile));
            // 获得缩放的比例
            double ratio = 1.0;
            // 判断如果高、宽都不大于设定值，则不处理
            if (image.getHeight() > height || image.getWidth() > width) {
                if (image.getHeight() > image.getWidth()) {
                    ratio = height / image.getHeight();
                } else {
                    ratio = width / image.getWidth();
                }
            }
            int newWidth = flag ? (int) width : (int) (image.getWidth() * ratio);
            int newHeight = flag ? (int) height : (int) (image.getHeight() * ratio);
            BufferedImage bfImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            result = bfImage.getGraphics().drawImage(image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
            ImageIO.write(bfImage, ImageType.JPG.getName(), new File(destImageFile));
        } catch (Exception e) {
            LOGGER.error(String.format("ImageUtil createThumbnail from %s to %s failed!", srcImageFile, destImageFile), e);
        }
        return result;
    }

    /**
     * 获取图片信息
     *
     * @param srcImageFile 需要读取的文件
     * @return 图片对象
     */
    public static ImageInfo readImgInfo(String srcImageFile) {
        ImageInfo imageInfo = null;
        try {
            Image src = ImageIO.read(getImageFile(srcImageFile));
            imageInfo = new ImageInfo();
            imageInfo.setWidth(src.getWidth(null));
            imageInfo.setHeight(src.getHeight(null));
        } catch (Exception e) {
            LOGGER.error(String.format("ImageUtil readImgInfo from %s failed!", srcImageFile), e);
        }
        return imageInfo;
    }

    /**
     * 顺时针旋转图片指定角度
     *
     * @param srcImageFile 源文件
     * @param destImageFile 目标文件
     * @param angel 角度
     * @return result
     */
    public static boolean rotate(String srcImageFile, String destImageFile, int angel) {
        boolean result = false;
        try {
            BufferedImage src = ImageIO.read(getImageFile(srcImageFile));
            int src_width = src.getWidth(null);
            int src_height = src.getHeight(null);
            // calculate the new image size
            Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);

            BufferedImage res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = res.createGraphics();
            // transform
            g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
            g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
            g2.drawImage(src, null, null);
            ImageIO.write(res, ImageType.JPG.getName(), new File(destImageFile));

            result = true;
        } catch (IOException e) {
            LOGGER.error(String.format("ImageUtil rotate from %s to %s failed!", srcImageFile, destImageFile), e);
        }
        return result;
    }

    /**
     * 判断图片原始文件是否有效
     *
     * @param srcImageFile 源文件
     * @return 文件对象
     */
    private static File getImageFile(String srcImageFile) throws FileNotFoundException {
        File f = new File(srcImageFile);
        if (!f.exists()) {
            throw new FileNotFoundException("srcImageFile:" + srcImageFile + "not fund exception!");
        }
        f.canRead();
        f.canWrite();
        return f;
    }

    /**
     * 获取图片旋转角度后的height和width
     *
     * @param src 源
     * @param angel 角度
     * @return result
     */
    public static Rectangle calcRotatedSize(Rectangle src, int angel) {
        // if angel is greater than 90 degree, we need to do some conversion
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }

        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);

        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new Rectangle(new Dimension(des_width, des_height));
    }

    /**
     * 给图片补空白
     *
     * @param itemp 图片对象
     * @param width 宽度
     * @param height 高度
     * @return result
     */
    private static Image pdddingImage(Image itemp, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        if (width == itemp.getWidth(null)) {
            g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
                itemp.getWidth(null), itemp.getHeight(null),
                Color.white, null);
        } else {
            g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
                itemp.getWidth(null), itemp.getHeight(null),
                Color.white, null);
        }
        g.dispose();
        return image;
    }

    /**
     * 图片文字水印参数体
     */
    public static class Text {

        /**
         * 水印文字
         */
        private String text;

        /**
         * 文字颜色
         */
        private Color color;

        /**
         * 文字字体
         */
        private Font font;

        public Text(String text) {
            this(text, Color.white);
        }

        public Text(String text, Color color) {
            this(text, color, new Font(null, 1, 14));
        }

        public Text(String text, Font font) {
            this(text, Color.white, font);
        }

        public Text(String text, Color color, Font font) {
            this.text = text;
            this.color = color;
            this.font = font;
        }

        public String getText() {
            return text;
        }

        public Color getColor() {
            return color;
        }

        public Font getFont() {
            return font;
        }
    }

    /**
     * 图片水印定位及透明度参数体(定位以图片左上角为0,0)
     */
    public static class MaskPosi {

        /**
         * 文字起始x坐标
         */
        private int x_posi;

        /**
         * 文字起始x坐标
         */
        private int y_posi;

        /**
         * 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
         */
        private float alpha;

        public MaskPosi(int x_posi, int y_posi) {
            this(x_posi, y_posi, 0.7F);
        }

        public MaskPosi(int x_posi, int y_posi, float alpha) {
            this.x_posi = x_posi;
            this.y_posi = y_posi;
            this.alpha = alpha;
        }

        public int getX_posi() {
            return x_posi;
        }

        public int getY_posi() {
            return y_posi;
        }

        public float getAlpha() {
            return alpha;
        }
    }

    /**
     * 图片切割的四要素: 切割起点坐标x,y和切割的长和宽
     */
    public static class Incise {

        /**
         * 切割点的起始x坐标
         */
        private int x;

        /**
         * 切割点的起始y坐标
         */
        private int y;

        /**
         * 切割图片宽度
         */
        private int width;

        /**
         * 切割图片高度
         */
        private int height;

        public Incise(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

}
