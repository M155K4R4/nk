// Engancement(cognitive load), meditation(relaxation), extiment
//------------------------------------------------------------------------------------
void render0() {
  for(int i = 0; i < rows; i++) {
    int cI = int(map(cFAr[i], 0, 1, 0, 40));
    int rI = int(map(rFAr[i], 0, 1, 0, 40));
    int eI = int(map(eFAr[i], 0, 1, 0, 40));
    //println(Integer.toString(i)+" - "+Integer.toString(cI)+","+Integer.toString(rI)+","+Integer.toString(eI));
    // set by default
    for (int j=0;j<cols;j++) pixelArray[i][j] = 0;
    // set black
    for (int j=0;j<cI;j++) {
      pixelArray[i][(20-(cI/2))+j] = 255;
    }
    for (int j=0;j<rI;j++) {
      pixelArray[i][40+(20-(rI/2))+j] = 255;
    }
    for (int j=0;j<eI;j++) {
      pixelArray[i][80+(20-(eI/2))+j] = 255;
    }
  }
  img = createImage(cols, rows, RGB);
  for (int i = 0; i < img.pixels.length; i++) {
    img.pixels[i] = color(255, 255, 255);
  }
  img.loadPixels();
  //println(Integer.toString(img.pixels.length/cols));
  //println(pixelArray.length);
  for (int i = 0; i < img.pixels.length; i++) {
    int x = int(i%cols);
    int y = int(i/cols);
    //
    if(x>cols) println("Error to big X");
    if(y>rows) println("Error to big Y");
    if (pixelArray[y][x]==0) {
      img.pixels[i] = color(255, 255, 255);
    }
    else {
      img.pixels[i] = color(0, 0, 0);
    }
  }
  img.updatePixels();
  img.save(genericFile+"img.png");
}
//------------------------------------------------------------------------------------
