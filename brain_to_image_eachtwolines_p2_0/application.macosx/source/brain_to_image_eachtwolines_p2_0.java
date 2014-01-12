import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import oscP5.*; 
import netP5.*; 
import controlP5.*; 
import java.awt.Frame; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class brain_to_image_eachtwolines_p2_0 extends PApplet {







ControlP5 cp5;
OscP5 oscP5;
NetAddress myRemoteLocation;

PImage img;
int rows;
int[][] pixelArray;
PFont font;
String txt_file;
String mp3_file;
int renderID;
boolean openFileSelection = false;
int cols = 120;

float[] cFAr;
float[] rFAr;
float[] eFAr;
float[] binEAr;
float[] waveAr;

String genericFile = "";

//http://bazaar.launchpad.net/~philho/+junk/Processing/files/head:/_SmallPrograms/Email/ 
//------------------------------------------------------------------------------------
public void setup() {
  size(600, 900);
  img = createImage(0, 0, RGB);
  // 
  gui();
  // Receive from port 12000
  oscP5 = new OscP5(this,12000);
  // Send from port 12001
  myRemoteLocation = new NetAddress("127.0.0.1",12001);
  // Screen settings
  frame.removeNotify(); 
  frame.setUndecorated(true); 
  frame.setAlwaysOnTop(true); 
  frame.addNotify(); 
 
  font = loadFont("Quantico-Regular-20.vlw");
  
  renderID = 0;
  txt_file = "";
  // load default data
  
}

//------------------------------------------------------------------------------------

public void keyPressed() {
 
}

//------------------------------------------------------------------------------------

public void draw() {
  frame.setLocation(displayWidth-width,20);
  background(30);
  // draw gui
  fill(255);
  textFont(font, 20);
  text("Neuroknitting visualization", 200, 30);
  text("Render", 200, 380);
  // draw pattern
  fill(255);
  rect(0,0,120,height);
  image(img, 0, 0);
  
  try{
    text("Total lines wave:"+Integer.toString(waveAr.length), 200,620);
    text("Total lines egg:"+Integer.toString(cFAr.length), 200, 650);
  }catch(Exception e){}
}

//------------------------------------------------------------------------------------
public void gui() {
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

public void controlEvent(ControlEvent theEvent) {
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

public void loadWaveTxt(String filePath) {
  String[] lines = loadStrings(filePath);
  int rows = lines.length;
  println(rows);
  waveAr = new float[rows];
   for (int i = 0; i < lines.length; i++) {
    waveAr[i] = Float.parseFloat(lines[i].substring(0,lines[i].length()-1));
  }
}

//------------------------------------------------------------------------------------

public void loadEEGTxt(String filePath) {
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
    int startLine = PApplet.parseInt(PApplet.parseInt(i/sample)*sample);
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

public void renderSelector(){
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

public void openknittingPattern() {  
  selectInput("Select a file to process:", "fileSelected");  // Opens file chooser
}

//------------------------------------------------------------------------------------

public void fileSelected(File selection) {
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
//------------------------------------------------------------------------------------

/* incoming osc message are forwarded to the oscEvent method. */
public void oscEvent(OscMessage theOscMessage) {
  /* print the address pattern and the typetag of the received OscMessage */
  print("### received an osc message.");
  print(" addrpattern: "+theOscMessage.addrPattern());
  println(" typetag: "+theOscMessage.typetag());
  
  //txt_file
}

//------------------------------------------------------------------------------------

public void sendMessageToRecordBrain() {
  /* in the following different ways of creating osc messages are shown by example */
  OscMessage myMessage = new OscMessage("/bang");
  myMessage.add(1); /* add an int to the osc message */
  /* send the message */
  oscP5.send(myMessage, myRemoteLocation); 
}

//------------------------------------------------------------------------------------
// Engancement(cognitive load), meditation(relaxation), extiment
//------------------------------------------------------------------------------------
public void render0() {
  for(int i = 0; i < rows; i++) {
    int cI = PApplet.parseInt(map(cFAr[i], 0, 1, 0, 40));
    int rI = PApplet.parseInt(map(rFAr[i], 0, 1, 0, 40));
    int eI = PApplet.parseInt(map(eFAr[i], 0, 1, 0, 40));
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
    int x = PApplet.parseInt(i%cols);
    int y = PApplet.parseInt(i/cols);
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
// Engancement(cognitive load), meditation(relaxation), extiment
//------------------------------------------------------------------------------------
public void render1() {
  
  if(cFAr.length < waveAr.length){
    rows = cFAr.length;
  }else{
    rows = waveAr.length;
  }
  
  for(int i = 0; i < rows; i++) {
    int cI = PApplet.parseInt(map(cFAr[i], 0, 1, 0, 80));
    
    // set by default
    for (int j=0;j<cols;j++) pixelArray[i][j] = 0;
    
    // set black
    for (int j=0;j<cI;j++) {
      if(j%2==0 && i%2==0){
        if(binEAr[i]< 0.3f){
          pixelArray[i][j] = 1;
        }else if(binEAr[i]> 0.3f && binEAr[i]< 0.6f){
          pixelArray[i][j] = 2;
        }else{
          pixelArray[i][j] = 3;
        }
      }
    }
    if(i<waveAr.length){
      int wI = PApplet.parseInt(map(waveAr[i], 0, 1, 0, 40));
      for (int j=0;j<wI;j++) {
        if(i%2==0){
          pixelArray[i][119-j] = 255;
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
    int x = PApplet.parseInt(i%cols);
    int y = PApplet.parseInt(i/cols);
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
}
//------------------------------------------------------------------------------------
// Engancement(cognitive load), meditation(relaxation), extiment
//------------------------------------------------------------------------------------
public void render2() {
  
  if(cFAr.length < waveAr.length){
    rows = cFAr.length;
  }else{
    rows = waveAr.length;
  }
  
  for(int i = 0; i < rows; i++) {
    int cI = PApplet.parseInt(map(cFAr[i], 0, 1, 0, 80));
    
    // set by default
    for (int j=0;j<cols;j++) pixelArray[i][j] = 0;
    
    // set black
    for (int j=0;j<cI;j++) {
      if(j%2==0 ){
          pixelArray[i][j] = 255;
      }
    }
    if(i<waveAr.length){
      int wI = PApplet.parseInt(map(waveAr[i], 0, 1, 0, 40));
      for (int j=0;j<wI;j++) {
        if(i%2==0){
          if(binEAr[i]<= 0.4f){
            pixelArray[i][119-j] = 1;
          }else if(binEAr[i]> 0.4f && binEAr[i]< 0.6f){
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
    int x = PApplet.parseInt(i%cols);
    int y = PApplet.parseInt(i/cols);
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
}
//------------------------------------------------------------------------------------
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "brain_to_image_eachtwolines_p2_0" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
