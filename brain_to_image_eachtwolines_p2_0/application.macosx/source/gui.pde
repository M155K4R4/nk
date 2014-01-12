void gui() {
  cp5 = new ControlP5(this);

  cp5.addButton("Open Knitting Pattern")
     .setPosition(200,180)
     .setSize(120,30)
     //.setBackgroundColor(color(255))
     ;
     
  cp5.addRadioButton("render")
     .setPosition(200,400)
     .setItemWidth(20)
     .setItemHeight(20)
     .addItem("Render three bars", 0)
     .addItem("Render 2", 1)
     .addItem("Render 3", 2)
     .setColorLabel(color(255))
     .activate(0);
}

void controlEvent(ControlEvent theEvent) {
  try{
  println("name:"+theEvent.name());
  println("group:"+theEvent.group().value());
  }catch(Exception e){
  
  }
  
  if( theEvent.name().equals("render")){
    println("change render");
    renderID = (int)theEvent.group().value();
    renderSelector();
  }else if(theEvent.name().equals("Open Knitting Pattern")){
    openknittingPattern();
  }
}

//------------------------------------------------------------------------------------

void loadWaveTxt(String filePath) {
  String[] lines = loadStrings(filePath);
  int rows = lines.length;
  println(rows);
  waveAr = new float[rows];
   for (int i = 0; i < lines.length; i++) {
    waveAr[i] = Float.parseFloat(lines[i].substring(0,lines[i].length()-1));
  }
}

//------------------------------------------------------------------------------------

void loadEEGTxt(String filePath) {
  String[] lines = loadStrings(filePath);
  rows = lines.length;
  print("rows:");
  println(rows);
  print("cols:");
  println(cols);
  pixelArray = new int[rows][cols];
  
  cFAr = new float[rows];
  rFAr = new float[rows];
  eFAr = new float[rows];
  binEAr = new float[rows];
  
  for (int i = 0; i < lines.length; i++) {
    String[] tokens = splitTokens(lines[i], " ");
    float cF = 0;
    float rF = 0;
    float eF = 0;
    
    // Neurosky
    if(tokens.length==2){
      try{
        cF = Float.parseFloat(tokens[0]);
      }catch(Exception e){}
    
      try{
        eF = Float.parseFloat(tokens[1].substring(0, tokens[1].length()-1) );
      }catch(Exception e){}
    }
    // EPOC
    if(tokens.length==3){
      try{
        cF = Float.parseFloat(tokens[0]);
      }catch(Exception e){}
      
      try{
        rF = Float.parseFloat(tokens[1]);
      }catch(Exception e){}
    
      try{
        eF = Float.parseFloat(tokens[2].substring(0, tokens[2].length()-1) );
      }catch(Exception e){}
    }
    cFAr[i] = cF;
    rFAr[i] = rF;
    eFAr[i] = eF;
  } 
  int sample = 50;
  // Bin excitement
  
  for (int i = 0; i < lines.length; i++) {
    int startLine = int(int(i/sample)*sample);
    float sum = 0;
    int totalSamples = 0;
    for(int il=startLine;il<(startLine+sample);il++){
      if(il<=(eFAr.length-1)){
        sum += eFAr[il];
        totalSamples += 1;
      }
    }
    binEAr[i] = (sum/totalSamples);
    println("startline: :"+Float.toString(startLine)+" i:"+Integer.toString(i)+" total bin:"+Float.toString(binEAr[i]) );
  }
  
  renderSelector();
}

//------------------------------------------------------------------------------------

void renderSelector(){
  if(renderID==0){
      if(txt_file!="")render0();
  }
  if(renderID==1){
      if(txt_file!="")render1();
  }
  if(renderID==2){
      if(txt_file!="")render2();
  }
}

//------------------------------------------------------------------------------------

void openknittingPattern() {  
  selectInput("Select a file to process:", "fileSelected");  // Opens file chooser
}

//------------------------------------------------------------------------------------

void fileSelected(File selection) {
  if (selection != null) {
    txt_file = selection.getAbsolutePath();
    String ext = txt_file.substring(txt_file.length()-7,txt_file.length());
    genericFile = "";
    println("|"+ext+"|");
    if(ext.equals("EEG.txt")){
      genericFile = txt_file.substring(0,txt_file.length()-7);
    }
    if(ext.equals("sic.txt")){
      genericFile = txt_file.substring(0,txt_file.length()-9);
    }
    println(genericFile);
    loadWaveTxt(genericFile+"music.txt");
    loadEEGTxt(genericFile+"EEG.txt");
    renderSelector();
  }
}
