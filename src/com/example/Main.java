package com.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.*;

public class Main {

    public static void main(String[] args) {
	// write your code here

        //String fileName = "C:\\Users\\yceri\\Desktop\\Penguins.jpg";
        //String outPutImage = "C:\\Users\\yceri\\Desktop\\WatermarkedImage.jpg";

        String fileName = "ImageTest\\T51b_Van_Buren.jpeg";
        String outPutImage = "ImageTest\\T51b_Van_BurenWaterMarked.jpeg";

        System.out.println("TEST !!!!!");

        //createWatermarkImage(fileName, outPutImage);
        createWaterMark(fileName, outPutImage);
        //test(fileName, outPutImage);
    }


    public static void createWatermarkImage(String fileName, String outputImage){
        File origFile = new File(fileName);
        ImageIcon icon = new ImageIcon(origFile.getPath());

        // create BufferedImage object of same width and height as of original image
        BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(),
                icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);

        // create graphics object and add original image to it
        Graphics graphics = bufferedImage.getGraphics();
        graphics.drawImage(icon.getImage(), 0, 0, null);

        // set font for the watermark text
        graphics.setFont(new Font("Arial", Font.BOLD, 60));

        //unicode characters for (c) is \u00a9
        String watermark = "\u00a9 JAVAXP.com";

        // add the watermark text
        graphics.setColor(Color.CYAN);
        graphics.drawString(watermark, icon.getIconWidth()/2, icon.getIconHeight() - 100);
        graphics.dispose();

        File newFile = new File(outputImage);
        try {
            ImageIO.write(bufferedImage, "jpg", newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(newFile.getPath() + " created successfully!");
    }

    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }


    public static void createWaterMark(String filename, String outputImage) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("Can't find file " + filename);
                return;
            }

            // We want to draw our watermark diagonally rotated across the center from
            //bottom to top like Word processors do
            String watermark = "S M A R T N I F";
            ImageIcon photo = new ImageIcon(filename);
            //Image photo = new ImageIcon(filename).getImage();

            BufferedImage bufferedImage = new BufferedImage(photo.getIconWidth(),
                    photo.getIconHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = (Graphics2D)bufferedImage.getGraphics();
            g2d.drawImage(photo.getImage(), 0, 0, null);


            ///////////////////////////////////////////////////////////////////////////////

            // set font for the watermark text
            //g2d.setFont(new Font("Serif", Font.BOLD, 60));
            try {
                //create the font to use. Specify the size!
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts\\yazitipleri_net_AGENTORANGE.ttf")).deriveFont(50f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                //register the font
                ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts\\yazitipleri_net_AGENTORANGE.ttf")));

                g2d.setFont(customFont);
            } catch (IOException e) {
                e.printStackTrace();
            } catch(FontFormatException e) {
                e.printStackTrace();
            }



            //unicode characters for (c) is \u00a9
            String watermark1 = "\u00a9 JAVAXP.com";

            // add the watermark text
            g2d.setColor(Color.CYAN);
            g2d.drawString(watermark1, photo.getIconWidth()/2, photo.getIconHeight()/2);

            //Free graphic resources
            //g2d.dispose();
            ///////////////////////////////////////////////////////////////////////////////


            //Create an alpha composite of 50%
            AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
            g2d.setComposite(alpha);

            g2d.setColor(Color.white);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            Font f1 = new Font("Arial", Font.BOLD, 60);
            g2d.setFont(f1);

            FontMetrics fontMetrics = g2d.getFontMetrics();
            Rectangle2D rect = fontMetrics.getStringBounds(watermark, g2d);

            // Move our origin to the center
            g2d.translate(photo.getIconWidth() / 2.0f, photo.getIconHeight() / 2.0f);

            // Create a rotation transform to rotate the text based on the diagonal
            // angle of the picture aspect ratio
            AffineTransform at2 = new AffineTransform();
            double opad = photo.getIconHeight() / (double)photo.getIconWidth();
            double angle = Math.toDegrees(Math.atan(opad));
            //////////////////
            double idegrees = 1 * angle;  // Rotate text
            /////////////////
            double theta = (2 * Math.PI * idegrees) / 360;
            at2.rotate(theta);
            g2d.transform(at2);

            //Now reposition our coordinates based on size (same as we would normally
            //do to center on straight line but based on starting at center
            float x1 = (int)rect.getWidth() / 2.0f *-1;
            float y1 = (int)rect.getHeight() / 2.0f;
            g2d.translate(x1, y1);

            //finally let's draw the string
            g2d.drawString(watermark, 0.0f, 0.0f);
            //Free graphic resources
            //g2d.dispose();

            //Write the image as a jpg
            FileOutputStream fos = new FileOutputStream(new File(outputImage));
            OutputStream out = new BufferedOutputStream(fos);
            ImageIO.write(bufferedImage, "jpg", out);
            out.flush();
            out.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void test(String filename, String outputImage){
        try {
            ImageIcon photo = new ImageIcon(filename);
            BufferedImage bufferedImage = new BufferedImage(photo.getIconWidth(),
                    photo.getIconHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();

            g2d.drawImage(photo.getImage(), 0, 0, null);
            //Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            String s = "dasdasdasd1";

            Font font = new Font("Courier", Font.PLAIN, 12);
            g2d.translate(20, 20);

            FontRenderContext frc = g2d.getFontRenderContext();

            GlyphVector gv = font.createGlyphVector(frc, s);
            int length = gv.getNumGlyphs();
            //Rectangle2D barra = new Rectangle2D.Double(0, 0, 700, 500);  // DRAW Rectangle
            //g2d.draw(barra);
            for (int i = 0; i < length; i++) {
                Point2D p = gv.getGlyphPosition(i);
                AffineTransform at = AffineTransform.getTranslateInstance(p.getX(), p.getY());
                at.rotate((double) i / (double) (length - 1) * Math.PI / 3);

                Shape glyph = gv.getGlyphOutline(i);
                Shape transformedGlyph = at.createTransformedShape(glyph);
                g2d.fill(transformedGlyph);
            }

            g2d.dispose();

            //Write the image as a jpg
            FileOutputStream fos = new FileOutputStream(new File(outputImage));
            OutputStream out = new BufferedOutputStream(fos);
            ImageIO.write(bufferedImage, "jpg", out);
            out.flush();
            out.close();
        } catch (IOException ioe) {
                ioe.printStackTrace();
        }
    }
}
