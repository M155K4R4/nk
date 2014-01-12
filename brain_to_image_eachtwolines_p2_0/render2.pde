// Engancement(cognitive load), meditation(relaxation), extiment
//------------------------------------------------------------------------------------
void render2() {
  
  if(cFAr.length < waveAr.length){
    rows = cFAr.length;
  }else{
    rows = waveAr.length;
  }
  
  for(int i = 0; i < rows; i++) {
    int cI = int(map(cFAr[i], 0, 1, 0, 80));
    
    // set by default
    for (int j=0;j<cols;j++) pixelArray[i][j] = 0;
    
    // set black
    for (int j=0;j<cI;j++) {
      if(j%2==0 ){
          pixelArray[i][j] = 255;
      }
    }
    if(i<waveAr.length){
      int wI = int(map(waveAr[i], 0, 1, 0, 40));
      for (int j=0;j<wI;j++) {
        if(i%2==0){
          if(binEAr[i]<= 0.4){
            pixelArray[i][119-j] = 1;
          }else if(binEAr[i]> 0.4 && binEAr[i]< 0.6){
            pixelArray[i][119-j]  = 2;
          }else{
            pixelArray[i][119-j]  = 3;
          }
        }
      }
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
    }else if (pixelArray[y][x]==1) {
      img.pixels[i] = color(255, 0, 0);
      //println(Integer.toString(i)+": green pixel");
    }else if (pixelArray[y][x]==2) {
      img.pixels[i] = color(0, 0, 255);
      //println(Integer.toString(i)+": blau pixel");
    }else {
      img.pixels[i] = color(0, 0, 0);
    }
  }
  img.updatePixels();
  img.save(genericFile+"img.png");
  
  myYarnRender = new yarnRender();
  myYarnRender.loadPattern(img);
}
//------------------------------------------------------------------------------------
