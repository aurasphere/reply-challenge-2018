
int x;
int y;
float outsideRadius = 150;
float insideRadius = 100;
String[] lines;
String[] linesPercorso;
int maxX = -1000000000;
int minX = 1000000000;
int maxY = -1000000000;
int minY = 1000000000;

  static int screenX = 1000,
      screenY = 1000;
void setup() {
  
  //Determina la grandezza dell canvase generato
  size(1000, 1000);
  background(204);
  x = width/2;
  y = height/2;
  // File delle coordinate di input e gli ostacoli
  lines = loadStrings("Ostacoli/input_1.txt");
  //File delle coordinate del percorso
  linesPercorso = loadStrings("Percorsi/out_path_1.txt");
  
  getMinMax();
  println("MinX : "+minX+" MaxX : "+maxX+" MinY : "+minY+" MaxY : "+maxY);
  float bigX = (-1*minX+maxX);
  float fattoreScala = screenX/bigX;
  println("MOD minX+maxX : "+bigX);
  println("Fattore scala: "+fattoreScala);
  //Fattore di scala dele figure rapportate alla largezza massimadello schermo 1000
  scale(fattoreScala);
  //scale(fattoreScala*2);
  //minX=-1000;
  //minY=-1000;
  background(204);
  
  //Disegno i triangoli degli ostacoli
  for (int i = 2 ; i < lines.length; i++) {
    //println(lines[i]);
    String[] coordinates = lines[i].split(" ");
    triangle(-1*minX+Integer.parseInt(coordinates[0]) , -1*minY+Integer.parseInt(coordinates[1]) , -1*minX+Integer.parseInt(coordinates[2]), -1*minY+Integer.parseInt(coordinates[3]), -1*minX+Integer.parseInt(coordinates[4]), -1*minY+Integer.parseInt(coordinates[5]));
  }
  println("FINITO");

  
  String[] StartEnd = lines[0].split(" ");    
  println("StartEnd lenght " +lines.length);
  // Scrivo Start e End
  textSize(100);
  fill(255,0,0);
  println("Start "+(-1*minX)+Integer.parseInt(StartEnd[0])+" "+(-1*minY)+Integer.parseInt(StartEnd[1]));
  point((-1*minX)+Integer.parseInt(StartEnd[0]), (-1*minY)+Integer.parseInt(StartEnd[1]));
  text("Start", (-1*minX)+Integer.parseInt(StartEnd[0]), (-1*minY)+Integer.parseInt(StartEnd[1])+20);
  
  textSize(100);
  fill(255,0,0);
  println("End "+(-1*minX)+Integer.parseInt(StartEnd[2])+" "+(-1*minY)+Integer.parseInt(StartEnd[3]));
  point((-1*minX)+Integer.parseInt(StartEnd[2]), (-1*minY)+Integer.parseInt(StartEnd[3]));
  text("End", (-1*minX)+Integer.parseInt(StartEnd[2]), (-1*minY)+Integer.parseInt(StartEnd[3]));
  drawPercorso(linesPercorso);
}

void draw() {
}

void drawPercorso(String[] linesPercorso){
  int pointAX = 0,
      pointAY = 0,
      pointBX = 0,
      pointBY = 0;
    fill(255,0,0);
  for (int i = 0 ; i < linesPercorso.length-1; i++) {
    String[] pointA = linesPercorso[i].split(" ");
    String[] pointB = linesPercorso[i+1].split(" ");
    pointAX = -1*minX+Integer.parseInt(pointA[0]);
    pointAY = -1*minY+Integer.parseInt(pointA[1]);
    pointBX = -1*minX+Integer.parseInt(pointB[0]);
    pointBY = -1*minY+Integer.parseInt(pointB[1]);
    fill(0,255,0);
    line(pointAX, pointAY, pointBX, pointBY);
  }
}

  void getMinMax(){
    for (int i = 2 ; i < lines.length; i++) {
      //println(lines[i]);
      String[] coordinates = lines[i].split(" ");
      
      // Find MAX X
      
      if(Integer.parseInt(coordinates[0])> maxX){
          maxX = Integer.parseInt(coordinates[0]);
      }
      if(Integer.parseInt(coordinates[2])> maxX){
          maxX = Integer.parseInt(coordinates[2]);
      }
      if(Integer.parseInt(coordinates[4])> maxX){
          maxX = Integer.parseInt(coordinates[4]);
      }
      
      
      // Find MIN X
      
      if(Integer.parseInt(coordinates[0]) < minX){
          minX = Integer.parseInt(coordinates[0]);
      }
      if(Integer.parseInt(coordinates[2]) < minX){
          minX = Integer.parseInt(coordinates[2]);
      }
      if(Integer.parseInt(coordinates[4]) < minX){
          minX = Integer.parseInt(coordinates[4]);
      }
      
      
      // Find Max Y
      if(Integer.parseInt(coordinates[1]) > maxY){
          maxY = Integer.parseInt(coordinates[1]);
      }
      if(Integer.parseInt(coordinates[3]) > maxY){
          maxY = Integer.parseInt(coordinates[3]);
      }
      if(Integer.parseInt(coordinates[5]) > maxY){
          maxY = Integer.parseInt(coordinates[5]);
      }
      
      
      // Find MIN Y
      if(Integer.parseInt(coordinates[1]) < minY){
          minY = Integer.parseInt(coordinates[1]);
      }
      if(Integer.parseInt(coordinates[3]) < minY){
          minY = Integer.parseInt(coordinates[3]);
      }
      if(Integer.parseInt(coordinates[5]) < minY){
          minY = Integer.parseInt(coordinates[5]);
      }
      
    }
  }
