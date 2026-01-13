import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ImageEditorPanel extends JPanel implements KeyListener {

    Color[][] pixels;
    
    public ImageEditorPanel() {
        BufferedImage imageIn = null;
        try {
            // the image should be in the main project folder, not in \src or \bin
            imageIn = ImageIO.read(new File("MichaelJordan.jpg"));
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
        pixels = makeColorArray(imageIn);
        setPreferredSize(new Dimension(pixels[0].length, pixels.length));
        setBackground(Color.BLACK);
        addKeyListener(this);
    }

    public void paintComponent(Graphics g) {
        // paints the array pixels onto the screen
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[0].length; col++) {
                g.setColor(pixels[row][col]);
                g.fillRect(col, row, 1, 1);
            }
        }


    }

    public void run() {
        repaint();
    }


    public Color[][] greyScale(Color[][] pixel) {
        int width = pixel.length;
        int height = pixel[0].length;
        Color[][] greyColors = new Color[height][width];
        
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Color c = pixel[row][col];
                int r = c.getRed();
                int g = c.getGreen();
                int b = c.getBlue();
                int rgb = ((299*r)/1000) + ((587*g)/1000) +((114*b)/1000);
                Color grey= new Color(rgb, rgb, rgb);
                greyColors[row][col] = grey;
            }
        }
        return greyColors;
    }

    public Color[][] flipHorizontal(Color[][] pixel) {
        Color mod[][] = new Color[800][800];
        for (int r = 0; r < pixels.length; r++) {
            for (int c = 0; c < pixels[0].length; c++) {
                mod[r][c] = pixels[r][799-c];
            }
        }
        for (int i = 0; i < mod.length; i++) {
            for (int index = 0; index < mod[0].length; index++) {
                pixels[i][index] = mod[i][index];
            }
        }
        return mod;
    }

    public Color[][] flipVertical(Color[][] pixel) {
        Color mod[][] = new Color[800][800];
        for (int r = 0; r < pixels.length; r++) {
            for (int c = 0; c < pixels[0].length; c++) {
                mod[r][c] = pixels[799-r][c];
            }
        }
        for (int i = 0; i < mod.length; i++) {
            for (int index = 0; index < mod[0].length; index++) {
                pixels[i][index] = mod[i][index];
            }
        }
        return mod;
    }


    public Color[][] blur(Color[][] pixel) {
        int pixelCounter = 0;
        for (int row = 1; row < pixel.length-1 ; row++) {
            for (int col = 1; col < pixel[0].length-1 ; col++) {
                pixelCounter = 0;
                Color c = pixel[row][col];
                int r = 0;
                int g = 0;
                int b = 0;
                for (int i = row-1; i <= row+1; i++) {
                    for (int j = col-1; j <= col+1; j++) {
                        // add up all the pixels of red, green, and blue
                        if (i >=0 && j>=0 && i< pixels.length && j< pixels[0].length) {
                            c = pixel[i][j];
                            r = c.getRed()+r;
                            g = c.getGreen()+g;
                            b = c.getBlue()+b;
                            pixelCounter++;
                        }
                    }
                }
                r = r/pixelCounter;
                g = g/pixelCounter;
                b = b/pixelCounter;
                Color blurredPixel= new Color(r, g, b);
                pixel[row][col] = blurredPixel;
            }
        }
        return pixel;
    }

    public Color[][]posterize(Color[][] pixel) {
        
        
        Color[] palette = {
            new Color(130,60,40),
            new Color(180,0,0),
            new Color(120,180,180),
            new Color(80,210,210),
            new Color(35,40,40),
        };
        //look through all of the pixels and test which one they are closest to
        for (int row = 0; row < pixel.length; row++) {
            for (int col = 0; col < pixel.length; col++) {
                Color c = pixel[row][col];
                int r = c.getRed();
                int g = c.getGreen();
                int b = c.getBlue();

                Color closest = palette[0];
                int minDist = Integer.MAX_VALUE;

                for (int i = 0; i < palette.length; i++) {
                    int dr = r - palette[i].getRed();
                    int dg = g - palette[i].getGreen();
                    int db = b - palette[i].getBlue();

                    int dist = dr * dr + dg * dg + db * db;

                    if (dist < minDist) {
                        minDist = dist;
                        closest = palette[i];
                    }
                    
                }

                pixel[row][col] = closest;
            

            }
        }
        return pixel;
    }


    public static Color[][] darken(Color[][] pixels) {
        final int AMOUNT = 30; 

        int height = pixels.length;
        int width = pixels[0].length;

        Color[][] result = new Color[height][width];

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                Color color = pixels[r][c];

                int red   = Math.max(0, color.getRed() - AMOUNT);
                int green = Math.max(0, color.getGreen() - AMOUNT);
                int blue  = Math.max(0, color.getBlue() - AMOUNT);

                result[r][c] = new Color(red, green, blue);
            }
        }
        return result;
    }

    public static Color[][] brighten(Color[][] pixels) {
        final int AMOUNT = 30; 

        int height = pixels.length;
        int width = pixels[0].length;

        Color[][] result = new Color[height][width];

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                Color color = pixels[r][c];

                int red   = Math.min(255, color.getRed() + AMOUNT);
                int green = Math.min(255, color.getGreen() + AMOUNT);
                int blue  = Math.min(255, color.getBlue() + AMOUNT);

                result[r][c] = new Color(red, green, blue);
            }
        }
        return result;
    }

    
    public static Color[][] sepia(Color[][] pixel) {
        int height = pixel.length;
        int width = pixel[0].length;

        Color[][] result = new Color[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                Color c = pixel[i][j];

                int r = c.getRed();
                int g = c.getGreen();
                int b = c.getBlue();

                int newR = (int)(0.393 * r + 0.769 * g + 0.189 * b);
                int newG = (int)(0.349 * r + 0.686 * g + 0.168 * b);
                int newB = (int)(0.272 * r + 0.534 * g + 0.131 * b);

                
                newR = Math.min(255, newR);
                newG = Math.min(255, newG);
                newB = Math.min(255, newB);

                result[i][j] = new Color(newR, newG, newB);
            }
        }

        return result;
    }

    public Color[][] makeColorArray(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        Color[][] result = new Color[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Color c = new Color(image.getRGB(col, row), true);
                result[row][col] = c;
            }
        }
        return result;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pixels = flipVertical(pixels);
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pixels = flipHorizontal(pixels);
        }
        repaint();
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            // call event handling methods
        }
    }

    public void keyTyped(KeyEvent e) {

        if (e.getKeyChar() == 'g') {
            pixels = greyScale(pixels);
        }  else if (e.getKeyChar() == 'f') {
            pixels = blur(pixels);
        } else if (e.getKeyChar() == 'd') {
            pixels = posterize(pixels);
        } else if (e.getKeyChar() == 'z') {
            pixels = darken(pixels);
        } else if (e.getKeyChar() == 'x') {
            pixels = brighten(pixels);
        } else if (e.getKeyChar() == 'h') {
            pixels = sepia(pixels);
        } else {
            repaint();
        }
    }
}
