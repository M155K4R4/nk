import oscP5.*;
import netP5.*;

import controlP5.*;
import java.awt.Frame;

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

yarnRender myYarnRender;

//http://bazaar.launchpad.net/~philho/+junk/Processing/files/head:/_SmallPrograms/Email/ 
//------------------------------------------------------------------------------------
void setup() {
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

void keyPressed() {
 
}

//------------------------------------------------------------------------------------

void draw() {
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
