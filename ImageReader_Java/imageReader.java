
//import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;


public class imageReader {

  
   public static void main(String[] args) {
   	
	// two coordinates to connect a line, x is for height, y is for row
    int x1 = 200, y1 = 400;
    int x2 = 10, y2 = 200;
    // fixed width and height
    int width = 512, height = 512;
    
    int start_x = x1, start_y = y1, end_x = x2, end_y = y2;
    int dx = x2 - x1;
    int dy = y2 - y1;
    double slope = 0;
    int dark_pix = 0xff000000 | ((0 & 0xff) << 16) | ((0 & 0xff) << 8) | (0 & 0xff);
    
    boolean anchor_x;
    if(dx != 0) slope = dy/(double)dx;
    
    if(Math.abs(slope) <= 1 && dx !=0) {
    	anchor_x = true;
    	if(dx < 0) {
    		start_x = x2;
    		start_y = y2;
    		end_x = x1;
    		end_y = y1;
    	}
    	slope = (end_y - start_y)/(double)(end_x - start_x);
    }
    else {
    	anchor_x = false;
    	if(dy < 0) {
    		start_x = x2;
    		start_y = y2;
    		end_x = x1;
    		end_y = y1;
    	}
    	if(dx == 0) {slope = 0;}
    	else {slope = (end_x - start_x)/(double)(end_y - start_y);}
    }

    
	//String fileName = args[0];
    //int width = Integer.parseInt(args[0]);
    //int height = Integer.parseInt(args[1]);
    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	for(int y = 0; y < height; y++){
		for(int x = 0; x < width; x++){
			byte r = (byte)20;
			byte g = (byte)255;
			byte b = (byte)255;
			int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
			img.setRGB(x,y,pix);
		}
	}
	
	img.setRGB(start_x, start_y, dark_pix);
	
	if(anchor_x) {
		double y = start_y + 0.5;
		for(int x = start_x + 1; x <= end_x; x++) {
			y = y + slope;
			img.setRGB(x, (int)Math.floor(y), dark_pix);
			//System.out.println(x);
			//System.out.println((int)Math.floor(y));
		}
	}
	else {
		double x = start_x + 0.5;
		for(int y = start_y + 1; y <= end_y; y++) {
			x = x + slope;
			img.setRGB((int)Math.floor(x), y, dark_pix);
			//System.out.println((int)Math.floor(x));
			//System.out.println(y);
		}
	}
		
	//save image to png format so that you can check   
    try{
        BufferedImage bi = img;
        File f = new File("MyLine.png");
        ImageIO.write(bi, "PNG", f);
    }
    catch(Exception e){
        e.printStackTrace();
    }
    
    // Use a panel and label to display the image
    JPanel  panel = new JPanel ();
    panel.add (new JLabel (new ImageIcon (img)));
    panel.add (new JLabel (new ImageIcon (img)));
    
    JFrame frame = new JFrame("Display images");
    
    frame.getContentPane().add (panel);
    frame.pack();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
   }
}