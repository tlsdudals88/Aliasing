
//import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.TimerTask;
import java.util.Timer;
import javax.imageio.ImageIO;
import javax.swing.*;



public class Mypart2 {

    static double input_angle = 0.0;
    static double change_angle = 0.0;

    static double input_angle1 = 0.0;
    static double change_angle1 = 0.0;

    static int args1 = 8; // args1
    static double speed_of_rotation = 0.03; // args2 (How many rotation per second)
    static double fps = 1; // args3 ( How many frames per second )

    // original
    static double rotation_per_one_frame_time = 0;
    static int one_frame_per_time = 100; // original timer second (10fps)

    // output
    static double rotation_per_one_frame_time1 = 0;
    static int one_frame_per_time1 = 0;

    static JLabel label;
    static JLabel label1;

    static JPanel panel;
    static JPanel panel1;

    static JFrame frame;
    static JFrame frame1;

    public static void main(String[] args) {

        args1 = Integer.parseInt(args[0]);
        speed_of_rotation = Double.parseDouble(args[1]);
        fps = Double.parseDouble(args[2]);

        Timer timer = new Timer();
        // final JPanel panel = new JPanel ();
        // final JFrame frame = new JFrame("Display images");
        label = new JLabel();
        label1 = new JLabel();
        panel = new JPanel();
        panel1 = new JPanel();
        frame = new JFrame("Input_video");
        frame1 = new JFrame("Output_video");

        // original
        rotation_per_one_frame_time = speed_of_rotation/1000 * one_frame_per_time; // s/fps

        // output
        one_frame_per_time1 = (int)(Math.round(1000/fps)); // ms
        rotation_per_one_frame_time1 = speed_of_rotation/1000 * one_frame_per_time1; // s/fps

        // System.out.println("display time "+one_frame_per_time+"ms");
        // System.out.println("number of rotation: "+rotation_per_one_frame_time);

        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {

                int ip;
                double fp;

                ip = (int) rotation_per_one_frame_time;
                fp = rotation_per_one_frame_time - (int) rotation_per_one_frame_time;

                // System.out.println("change angle :"+change_angle);

                int width = 512;
                int height = 512;
                int radius = 255;

                input_angle = (input_angle + change_angle)%360;

                change_angle = 360*ip + 360*fp;


                int dark_pix = 0xff000000 | ((0 & 0xff) << 16) | ((0 & 0xff) << 8) | (0 & 0xff);
                int white_pix = 0xff000000 | (((byte) 255 & 0xff) << 16) | (((byte) 255 & 0xff) << 8) | ((byte) 255 & 0xff); // r,g,b

                BufferedImage original_img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

                // double input_angle = 0.0;
                double plus_angle = (double)360/args1;

                // draw background (White)
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        original_img.setRGB(x, y, white_pix);
                    }
                }


                // Draw Circle (Black)
                int dot_number = 2000;
                for(int i=0; i<dot_number; i++) {
                    double t = 2 * Math.PI * i / dot_number;
                    int x = (int) Math.round(width/2 + radius * Math.cos(t));
                    int y = (int) Math.round(height/2 + radius * Math.sin(t));
                    original_img.setRGB(x, y, dark_pix);
                }


                // System.out.println("input angle:"+input_angle);
                // draw lines
                for(int i=0; i<args1; i++) {

                    double angle = input_angle;

                    int x1 = width/2;
                    int y1 = height/2;

                    int x2 = (int)Math.round(x1 + Math.cos(Math.toRadians(input_angle)) * radius);
                    int y2 = (int)Math.round(y1 + Math.sin(Math.toRadians(input_angle)) * radius);

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

                    if(i==1)
                        original_img.setRGB(start_x, start_y, -90000);
                    else
                        original_img.setRGB(start_x, start_y, dark_pix);

                    if (anchor_x) {
                        double y = start_y + 0.5;
                        for (int x = start_x + 1; x <= end_x; x++) {
                            y = y + slope;
                            if(i==1)
                                original_img.setRGB(x, (int) Math.floor(y), -90000);
                            else
                                original_img.setRGB(x, (int) Math.floor(y), dark_pix);
                        }
                    } else {
                        double x = start_x + 0.5;
                        for (int y = start_y + 1; y <= end_y; y++) {
                            x = x + slope;
                            if(i==1)
                                original_img.setRGB((int) Math.floor(x), y, -90000);
                            else
                                original_img.setRGB((int) Math.floor(x), y, dark_pix);
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

                // Use a panel and label to display the image
                // JPanel  panel = new JPanel ();

                // panel.revalidate();
                // panel.repaint();
                // panel.removeAll();
                panel.remove(label);
                // panel.remove(label1);
                label = new JLabel (new ImageIcon (original_img));
                // label1 = new JLabel (new ImageIcon (original_img));

                panel.add (label);
                // panel.add (label1);
                //panel.add (new JLabel (new ImageIcon (original_img)));

                // JFrame frame = new JFrame("Display images");
                frame.getContentPane().add (panel);
                frame.pack();
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


            }
        }, 0, one_frame_per_time);



        Timer timer1 = new Timer();
        timer1.scheduleAtFixedRate(new TimerTask() {

            public void run() {

                int ip;
                double fp;

                ip = (int) rotation_per_one_frame_time1;
                fp = rotation_per_one_frame_time1 - (int) rotation_per_one_frame_time1;

                int width = 512;
                int height = 512;
                int radius = 255;

                input_angle1 = (input_angle1 + change_angle1)%360;
                change_angle1 = 360*ip + 360*fp;


                int dark_pix = 0xff000000 | ((0 & 0xff) << 16) | ((0 & 0xff) << 8) | (0 & 0xff);
                int white_pix = 0xff000000 | (((byte) 255 & 0xff) << 16) | (((byte) 255 & 0xff) << 8) | ((byte) 255 & 0xff); // r,g,b

                BufferedImage original_img1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

                // double input_angle = 0.0;
                double plus_angle = (double)360/args1;

                // draw background (White)
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        original_img1.setRGB(x, y, white_pix);
                    }
                }


                // Draw Circle (Black)
                int dot_number = 2000;
                for(int i=0; i<dot_number; i++) {
                    double t = 2 * Math.PI * i / dot_number;
                    int x = (int) Math.round(width/2 + radius * Math.cos(t));
                    int y = (int) Math.round(height/2 + radius * Math.sin(t));
                    original_img1.setRGB(x, y, dark_pix);
                }


                // System.out.println("input angle1:"+input_angle1);
                // draw lines
                for(int i=0; i<args1; i++) {

                    double angle = input_angle1;

                    int x1 = width/2;
                    int y1 = height/2;

                    int x2 = (int)Math.round(x1 + Math.cos(Math.toRadians(input_angle1)) * radius);
                    int y2 = (int)Math.round(y1 + Math.sin(Math.toRadians(input_angle1)) * radius);

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

                    if(i==1)
                        original_img1.setRGB(start_x, start_y, -90000);
                    else
                        original_img1.setRGB(start_x, start_y, dark_pix);

                    if (anchor_x) {
                        double y = start_y + 0.5;
                        for (int x = start_x + 1; x <= end_x; x++) {
                            y = y + slope;
                            if(i==1)
                                original_img1.setRGB(x, (int) Math.floor(y), -90000);
                            else
                                original_img1.setRGB(x, (int) Math.floor(y), dark_pix);
                        }
                    } else {
                        double x = start_x + 0.5;
                        for (int y = start_y + 1; y <= end_y; y++) {
                            x = x + slope;
                            if(i==1)
                                original_img1.setRGB((int) Math.floor(x), y, -90000);
                            else
                                original_img1.setRGB((int) Math.floor(x), y, dark_pix);
                        }
                    }

                    input_angle1 = input_angle1 + plus_angle;
                }


                //save image to png format so that you can check
                try{
                    BufferedImage bi = original_img1;
                    File f = new File("MyLine1.png");
                    ImageIO.write(bi, "PNG", f);
                }
                catch(Exception e){
                    e.printStackTrace();
                }

                panel1.remove(label1);
                label1 = new JLabel (new ImageIcon (original_img1));
                panel1.add (label1);

                frame1.getContentPane().add (panel1);
                frame1.pack();
                frame1.setVisible(true);

                frame1.setLocation(frame.getX()+frame.getWidth(), frame.getY());
                frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


            }
        }, 0, one_frame_per_time1);

    }
}