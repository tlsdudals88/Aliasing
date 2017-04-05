
//import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;


public class Mypart1 {


    public static void main(String[] args) {

        // int args1 = 16;
        // double args2 = 1.5;
        // int args3 = 1;

        int args1 = Integer.parseInt(args[0]);
        double args2 = Double.parseDouble(args[1]);
        int args3 = Integer.parseInt(args[2]);

        int width = 512;
        int height = 512;

        int copy_width = (int)Math.round(512/args2);
        int copy_height = (int)Math.round(512/args2);

        int original_img_pixel[][] = new int[width][height];
        int original_img_pixel_anti_aliasing[][] = new int[width][height];

        int dark_pix = 0xff000000 | ((0 & 0xff) << 16) | ((0 & 0xff) << 8) | (0 & 0xff);
        int white_pix = 0xff000000 | (((byte) 255 & 0xff) << 16) | (((byte) 255 & 0xff) << 8) | ((byte) 255 & 0xff); // r,g,b

        BufferedImage original_img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage copy_img = new BufferedImage(copy_width, copy_height, BufferedImage.TYPE_INT_RGB);


        double input_angle = 0;
        double plus_angle = (double)360/args1;

        // draw background (White)
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                original_img.setRGB(x, y, white_pix);
            }
        }


        // draw Rectangle (Black)
        for (int x = 0; x < width; x++) {
            original_img.setRGB(x, 0, dark_pix);
        }
        for (int x = 0; x < width; x++) {
            original_img.setRGB(x, height-1, dark_pix);
        }
        for (int y = 0; y < height; y++) {
            original_img.setRGB(0, y, dark_pix);
        }
        for (int y = 0; y < height; y++) {
            original_img.setRGB(width-1, y, dark_pix);
        }


        // draw lines
        for(int i=0; i<args1; i++) {

            double hypotenuse_angle;
            double angle = input_angle;

            // System.out.println("input angle:"+input_angle);


            int x1 = width/2; // middle x (start point)
            int y1 = height/2; // middle y (start point)


            if (input_angle == 45 || input_angle == 135 || input_angle == 225 || input_angle == 315) {
                hypotenuse_angle = 45;
                // System.out.println("hypotenuse_angle :" + hypotenuse_angle);
            }

            else if (input_angle == 90 || input_angle == 180 || input_angle == 270 || input_angle == 360) {
                hypotenuse_angle = 0;
                // System.out.println("hypotenuse_angle :" + hypotenuse_angle);
            }

            else if (input_angle < 45 || (input_angle > 90 && input_angle < 135) || (input_angle > 180 && input_angle < 225)
                    || (input_angle > 270 && input_angle < 315)) {
                hypotenuse_angle = input_angle % 45;
                // System.out.println("hypotenuse_angle :" + hypotenuse_angle);
            } else {
                hypotenuse_angle = 45 - (input_angle % 45);
                // System.out.println("hypotenuse_angle :" + hypotenuse_angle);
            }


            // System.out.println("tan :" + Math.tan(Math.toRadians(hypotenuse_angle)));
            // System.out.println("len :" + 200 * Math.tan(Math.toRadians(hypotenuse_angle)));

            double hypotenuse_length = Math.sqrt(Math.pow((width/2-1) * Math.tan(Math.toRadians(hypotenuse_angle)), 2) + Math.pow((width/2-1), 2));
            // System.out.println("hypotenuse_length :" + hypotenuse_length);

            // System.out.println("sin: " + Math.cos(Math.toRadians(angle)) + ",cos: " + Math.sin(Math.toRadians(angle)));

            int x2 = (int)Math.round(x1 + Math.cos(Math.toRadians(angle)) * hypotenuse_length);
            int y2 = (int)Math.round(y1 + Math.sin(Math.toRadians(angle)) * hypotenuse_length);

            // System.out.println("(" + x1 + "," + y1 + "),  (" + x2 + "," + y2+")");

            int start_x = x1, start_y = y1, end_x = x2, end_y = y2;
            int dx = x2 - x1;
            int dy = y2 - y1;
            double slope = 0;

            boolean anchor_x;
            if (dx != 0) slope = dy / (double) dx;

            if (Math.abs(slope) <= 1 && dx != 0) {
                anchor_x = true;
                if (dx < 0) {
                    start_x = x2;
                    start_y = y2;
                    end_x = x1;
                    end_y = y1;
                }
                slope = (end_y - start_y) / (double) (end_x - start_x);

            } else {
                anchor_x = false;
                if (dy < 0) {
                    start_x = x2;
                    start_y = y2;
                    end_x = x1;
                    end_y = y1;
                }
                if (dx == 0) {
                    slope = 0;
                } else {
                    slope = (end_x - start_x) / (double) (end_y - start_y);
                }
            }



            //String fileName = args[0];
            //int width = Integer.parseInt(args[0]);
            //int height = Integer.parseInt(args[1]);



            original_img.setRGB(start_x, start_y, dark_pix);

            if (anchor_x) {
                double y = start_y + 0.5;
                for (int x = start_x + 1; x <= end_x; x++) {
                    y = y + slope;
                    original_img.setRGB(x, (int) Math.floor(y), dark_pix);

                    // System.out.println("(" + x + " ," + (int) Math.floor(y) + ")--");
                    // System.out.println(x);
                    // System.out.println((int)Math.floor(y));
                }
            } else {
                double x = start_x + 0.5;
                for (int y = start_y + 1; y <= end_y; y++) {
                    x = x + slope;
                    original_img.setRGB((int) Math.floor(x), y, dark_pix);

                    // System.out.println("(" + x + " ," + (int) Math.floor(y) + ")++");
                    // System.out.println((int)Math.floor(x));
                    // System.out.println(y);
                }
            }

            input_angle = input_angle + plus_angle;
            // System.out.println();
        }




        //save image to png format so that you can check
        try{
            BufferedImage bi = original_img;
            File f = new File("MyLine.png");
            ImageIO.write(bi, "PNG", f);
        }
        catch(Exception e){
            e.printStackTrace();
        }


        // get pixel from original image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                original_img_pixel[x][y] = original_img.getRGB(x,y);
            }
        }

        // get anti_aliasing pixel from original image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                if(x == 0 && y == 0) { // (0,0) => 4 sample
                    double weight = 0;

                    if(original_img.getRGB(x,y) == white_pix) {
                        weight = weight + (double)1/4;
                    }
                    if(original_img.getRGB(x+1,y) == white_pix) {
                        weight = weight + (double)1/4;
                    }
                    if(original_img.getRGB(x,y+1) == white_pix) {
                        weight = weight + (double)1/4;
                    }
                    if(original_img.getRGB(x+1,y+1) == white_pix) {
                        weight = weight + (double)1/4;
                    }

                    int color = 0xff000000 | (((byte)(255*weight) & 0xff) << 16) | (((byte)(255*weight)& 0xff) << 8) | ((byte)(255*weight) & 0xff);
                    original_img_pixel_anti_aliasing[x][y] = color;
                    // System.out.println("color : "+color);
                }

                else if(x == 0 && y == height-1) { // (0,511) => 4 sample
                    double weight = 0;

                    if(original_img.getRGB(x,y) == white_pix) {
                        weight = weight + (double)1/4;
                    }
                    if(original_img.getRGB(x+1,y) == white_pix) {
                        weight = weight + (double)1/4;
                    }
                    if(original_img.getRGB(x,y-1) == white_pix) {
                        weight = weight + (double)1/4;
                    }
                    if(original_img.getRGB(x+1,y-1) == white_pix) {
                        weight = weight + (double)1/4;
                    }

                    int color = 0xff000000 | (((byte)(255*weight) & 0xff) << 16) | (((byte)(255*weight)& 0xff) << 8) | ((byte)(255*weight) & 0xff);
                    original_img_pixel_anti_aliasing[x][y] = color;
                    // System.out.println("color : "+color);
                }
                else if(x == width-1 && y == 0) { // (511,0) => 4 sample
                    double weight = 0;

                    if(original_img.getRGB(x,y) == white_pix) {
                        weight = weight + (double)1/4;
                    }
                    if(original_img.getRGB(x-1,y) == white_pix) {
                        weight = weight + (double)1/4;
                    }
                    if(original_img.getRGB(x,y+1) == white_pix) {
                        weight = weight + (double)1/4;
                    }
                    if(original_img.getRGB(x-1,y+1) == white_pix) {
                        weight = weight + (double)1/4;
                    }

                    int color = 0xff000000 | (((byte)(255*weight) & 0xff) << 16) | (((byte)(255*weight)& 0xff) << 8) | ((byte)(255*weight) & 0xff);
                    original_img_pixel_anti_aliasing[x][y] = color;
                    // System.out.println("color : "+color);
                }
                else if(x == width-1 && y == height-1) { // (511,511) => 4 sample
                    double weight = 0;

                    if(original_img.getRGB(x,y) == white_pix) {
                        weight = weight + (double)1/4;
                    }
                    if(original_img.getRGB(x-1,y) == white_pix) {
                        weight = weight + (double)1/4;
                    }
                    if(original_img.getRGB(x,y-1) == white_pix) {
                        weight = weight + (double)1/4;
                    }
                    if(original_img.getRGB(x-1,y-1) == white_pix) {
                        weight = weight + (double)1/4;
                    }

                    int color = 0xff000000 | (((byte)(255*weight) & 0xff) << 16) | (((byte)(255*weight)& 0xff) << 8) | ((byte)(255*weight) & 0xff);
                    original_img_pixel_anti_aliasing[x][y] = color;
                    // System.out.println("color : "+color);
                }
                else if(y == 0) { // (1,0) ~ (510,0) => 6 sample
                    double weight = 0;

                    if(original_img.getRGB(x,y) == white_pix) {
                        weight = weight + (double)1/6;
                    }
                    if(original_img.getRGB(x-1,y) == white_pix) {
                        weight = weight + (double)1/6;
                    }
                    if(original_img.getRGB(x+1,y) == white_pix) {
                        weight = weight + (double)1/6;
                    }
                    if(original_img.getRGB(x,y+1) == white_pix) {
                        weight = weight + (double)1/6;
                    }
                    if(original_img.getRGB(x-1,y+1) == white_pix) {
                        weight = weight + (double)1/6;
                    }
                    if(original_img.getRGB(x+1,y+1) == white_pix) {
                        weight = weight + (double)1/6;
                    }

                    int color = 0xff000000 | (((byte)(255*weight) & 0xff) << 16) | (((byte)(255*weight)& 0xff) << 8) | ((byte)(255*weight) & 0xff);
                    original_img_pixel_anti_aliasing[x][y] = color;
                    // System.out.println("color : "+color);
                }
                else if(x == 0) { // (0,1) ~ (0,510) => 6 sample
                    double weight = 0;

                    if(original_img.getRGB(x,y) == white_pix) {
                        weight = weight + (double)1/6;
                    }
                    if(original_img.getRGB(x+1,y) == white_pix) {
                        weight = weight + (double)1/6;
                    }
                    if(original_img.getRGB(x,y-1) == white_pix) {
                        weight = weight + (double)1/6;
                    }
                    if(original_img.getRGB(x+1,y-1) == white_pix) {
                        weight = weight + (double)1/6;
                    }
                    if(original_img.getRGB(x,y+1) == white_pix) {
                        weight = weight + (double)1/6;
                    }
                    if(original_img.getRGB(x+1,y+1) == white_pix) {
                        weight = weight + (double)1/6;
                    }

                    int color = 0xff000000 | (((byte)(255*weight) & 0xff) << 16) | (((byte)(255*weight)& 0xff) << 8) | ((byte)(255*weight) & 0xff);
                    original_img_pixel_anti_aliasing[x][y] = color;
                    // System.out.println("color : "+color+"   "+x+"    "+y);
                }
                else if(x == width-1) { // (511,1) ~ (511,510) => 6 sample
                    double weight = 0;

                    if(original_img.getRGB(x,y) == white_pix) {
                        weight = weight + (double)1/6;
                    }
                    if(original_img.getRGB(x-1,y) == white_pix) {
                        weight = weight + (double)1/6;
                    }
                    if(original_img.getRGB(x,y-1) == white_pix) {
                        weight = weight + (double)1/6;
                    }
                    if(original_img.getRGB(x-1,y-1) == white_pix) {
                        weight = weight + (double)1/6;
                    }
                    if(original_img.getRGB(x,y+1) == white_pix) {
                        weight = weight + (double)1/6;
                    }
                    if(original_img.getRGB(x-1,y+1) == white_pix) {
                        weight = weight + (double)1/6;
                    }

                    int color = 0xff000000 | (((byte)(255*weight) & 0xff) << 16) | (((byte)(255*weight)& 0xff) << 8) | ((byte)(255*weight) & 0xff);
                    original_img_pixel_anti_aliasing[x][y] = color;
                    // System.out.println("color : "+color+"   "+x+"    "+y);
                }
                else if(y == height-1) { // (1,511) ~ (510,511) => 6 sample
                    double weight = 0;

                    if(original_img.getRGB(x,y) == white_pix) {
                        weight = weight + (double)1/6;
                    }
                    if(original_img.getRGB(x,y-1) == white_pix) {
                        weight = weight + (double)1/6;
                    }
                    if(original_img.getRGB(x-1,y) == white_pix) {
                        weight = weight + (double)1/6;
                    }
                    if(original_img.getRGB(x-1,y-1) == white_pix) {
                        weight = weight + (double)1/6;
                    }
                    if(original_img.getRGB(x+1,y) == white_pix) {
                        weight = weight + (double)1/6;
                    }
                    if(original_img.getRGB(x+1,y-1) == white_pix) {
                        weight = weight + (double)1/6;
                    }

                    int color = 0xff000000 | (((byte)(255*weight) & 0xff) << 16) | (((byte)(255*weight)& 0xff) << 8) | ((byte)(255*weight) & 0xff);
                    original_img_pixel_anti_aliasing[x][y] = color;
                    // System.out.println("color : "+color+"   "+x+"    "+y);
                }
                else { // 9 sample
                    double weight = 0;

                    if(original_img.getRGB(x-1,y-1) == white_pix) {
                        weight = weight + (double)1/9;
                    }
                    if(original_img.getRGB(x-1,y) == white_pix) {
                        weight = weight + (double)1/9;
                    }
                    if(original_img.getRGB(x-1,y+1) == white_pix) {
                        weight = weight + (double)1/9;
                    }
                    if(original_img.getRGB(x,y-1) == white_pix) {
                        weight = weight + (double)1/9;
                    }
                    if(original_img.getRGB(x,y) == white_pix) {
                        weight = weight + (double)1/9;
                    }
                    if(original_img.getRGB(x,y+1) == white_pix) {
                        weight = weight + (double)1/9;
                    }
                    if(original_img.getRGB(x+1,y-1) == white_pix) {
                        weight = weight + (double)1/9;
                    }
                    if(original_img.getRGB(x+1,y) == white_pix) {
                        weight = weight + (double)1/9;
                    }
                    if(original_img.getRGB(x+1,y+1) == white_pix) {
                        weight = weight + (double)1/9;
                    }

                    int color = 0xff000000 | (((byte)(255*weight) & 0xff) << 16) | (((byte)(255*weight)& 0xff) << 8) | ((byte)(255*weight) & 0xff);
                    original_img_pixel_anti_aliasing[x][y] = color;
                    // System.out.println("color : "+color+"   "+x+"    "+y);
                }

            }
        }

        // Down-scaling image Using original image pixel or anti-aliasing
        double scale_x=0;
        double scale_y=0;
        int round_scale_y=0;
        int round_scale_x=0;
        for (int y = 0; y < copy_height; y++) {
            scale_x = 0;
            round_scale_y = (int)Math.round(scale_y);
            for (int x = 0; x < copy_width; x++) {

                round_scale_x = (int)Math.round(scale_x);
                if(args3 == 0 || args2 == 1.0) // aliasing
                    copy_img.setRGB(x,y,original_img_pixel[round_scale_x][round_scale_y]); // down scaling
                else // anti-aliasing
                    copy_img.setRGB(x,y,original_img_pixel_anti_aliasing[round_scale_x][round_scale_y]); // down scaling

                // System.out.println("("+scale_x+", "+scale_y+"), ("+round_scale_x+", "+round_scale_y+")");

                scale_x = scale_x + args2;

                if(args3 == 0 || args2 == 1.0) {
                    if (y == copy_height - 1) { // boundary
                        copy_img.setRGB(copy_height - 1, x, original_img_pixel[511][511]);
                    }
                    if (x == copy_height - 1) { // boundary
                        for (int i = 0; i < copy_width; i++)
                            copy_img.setRGB(i, copy_width - 1, original_img_pixel[511][511]);
                    }
                }
                else {
                    /*
                    if (y == copy_height - 1) { // boundary
                        copy_img.setRGB(copy_height - 1, x, original_img_pixel_anti_aliasing[511][511]);
                    }
                    if (x == copy_height - 1) { // boundary
                        for (int i = 0; i < copy_width; i++)
                            copy_img.setRGB(i, copy_width - 1, original_img_pixel_anti_aliasing[511][511]);
                    }
                    */
                }

            }
            scale_y = scale_y + args2;
        }

        // Use a panel and label to display the image
        JPanel  panel = new JPanel ();
        panel.add (new JLabel (new ImageIcon (original_img)));
        panel.add (new JLabel (new ImageIcon (copy_img)));

        JFrame frame = new JFrame("Display images");

        frame.getContentPane().add (panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}