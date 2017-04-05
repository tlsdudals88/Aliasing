# Start

  1. Mypart1.java
  
  The first parameter n is the number of lines to create an image with radial pattern of n black lines starting from the center of the image towards the boundaries. 
  The image has a white background. Each consecutive line is separated by 360/n degrees. 
  The idea here is by increasing n, you can increase the frequency content in an image.
  The second parameter s will be scaling value that scales the input image down by a factor. 
  This is a floating point number eg s=2.0 will scale the image down to 256x256. Note s need not be a complete integer.
  The third parameter will be a boolean value (0 or 1) suggesting whether or not you want to deal with aliasing. 
  A 0 signifies do nothing (aliasing will remain in your output) which means you need copy the direct mapped value from input to output. 
  A value 1 signifies that anti-aliasing should be performed – which means that instead of the direct mapped value you need to copy a low pass filtered value to the output. 
   ex) javac Mypart1.java
       java Mypart1 16 2.0 0
   
   2. Mypart2.java
   The first parameter n is the number of lines to create an image with radial pattern of n black lines starting from the center of the image towards the boundaries. 
   The image has a white background. Each consecutive line is separated by 360/n degrees. The idea here is by increasing n, you can increase the frequency content in an image.
   The second parameter s will be a speed of rotations in terms of rotations per second. 
   This is a floating point number eg s=2.0 indicates that the wheel is making two full rotations in a second, s=7.5 indicates that the wheel is making seven and a half rotations in a second. 
   Remember this is the original input video signal with a very high display rate.
   The third parameter will be an fps value suggesting that not all frames of the input video are displayed, but only a specific frames per second are displayed.
    ex) javac Mypart2.java
        java Mypart2 64 4.0 10.0
