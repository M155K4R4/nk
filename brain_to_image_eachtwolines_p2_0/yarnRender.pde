class yarnRender {

  yarnStitch[] myYarn;
  int widthYarn;
  PImage img;

  void yarnRender() {
  }

  void loadPattern(PImage _img) { 
    img = _img;
    img.loadPixels();
    int totalStitch = img.height*img.width;
    widthYarn = img.width;
    myYarn = new yarnStitch[totalStitch];
    for (int i=0; i<myYarn.length;i++) {
      myYarn[i] = new yarnStitch(7);
    }
  }

  void draw() {
    img.loadPixels();
    for (int y = 0; y < img.height; y++) {
      for (int x = 0; x < img.width; x++) {
        int loc = x + y*img.width;
        
        // posicion in garment
        Boolean lastRow = false;
        Boolean firstRow = false;
        if ((loc/widthYarn)==0) firstRow = true;
        if (ceil(loc/widthYarn)==ceil((myYarn.length-1)/widthYarn)) lastRow = true;

        // The functions red(), green(), and blue() pull out the 3 color components from a pixel.
        float r1 = red(img.pixels[loc]);
        float g1 = green(img.pixels[loc]);
        float b1 = blue(img.pixels[loc]);
        float a1 = 1;
        
        float r2 =0; 
        float g2 =0;
        float b2 =0;
        float a2 =0;
          
        if(!lastRow){
          r2 = red(img.pixels[loc+widthYarn]);
          g2 = green(img.pixels[loc+widthYarn]);
          b2 = blue(img.pixels[loc+widthYarn]);
          a2 = 1;
        }
        color c1 = color (r1, g1, b1);
        color c2 = color (r2, g2, b2);
        //color c1 = color (255.0,0.0,0.0);
        //color c2 = color (255.0,0.0,0.0);
        if(firstRow){
          myYarn[loc].drawPieceModelUnit(loc%widthYarn, loc/widthYarn, c1, c1, true, lastRow);
          myYarn[loc].drawPieceModelUnit((loc%widthYarn), (loc/widthYarn)+1, c1, c2, false, lastRow);
        }else{
          myYarn[loc].drawPieceModelUnit((loc%widthYarn), (loc/widthYarn)+1, c1, c2, firstRow, lastRow);
        }
      }
    }
  }
  
}
