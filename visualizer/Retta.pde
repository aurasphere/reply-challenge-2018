
public class Retta {
  //private double m;
  //private double q;
  private float a;
  private float b;
  private float c;
  private float m;
  private float q;
  
  public Retta(float p1, float p2, float p3) {
    a = p1;
    b = p2;
    c = p3;
  
  }
  public Retta() {
    a=-1;
    b=1;
    c=0;
  
  }
  
  public float getA() {
    return a;
  }
  
  public float getB() {
    return b;
  }
  
  public float getC() {
   return c;
  }
  
  public void setA(float a) {
   this.a = a;
  }
  
  public void setB(float b) {
   this.b = b;
  }
  
  public void setC(float c) {
   this.c = c;
  }
  /**Retta parallela alla retta passante per il punto P ritorna la retta
  */ 
  public Retta Parallela(Punto p) {
  
    Retta r=new Retta();
    r.setA(this.a);
    r.setB(this.b);
    r.setC((-this.b*p.getY()-this.a*p.getX())); //y - y1 = m(x - x1)
    return r; 
  }
  /**Retta perpendicolare alla retta passante per il punto P ritorna la retta
  */
  public Retta Perpendicolare(Punto p) {
    Retta r=new Retta();
    r.setA(-this.a);
    r.setB(this.b);
    r.setC(-this.a*p.getX()+this.b*p.getY());
    return r;
  }
  /**Intersezione tra 2 rette viene ritornato il Punto di intersezione
  */
  public Punto Intersezione(Retta r) throws IOException {
    Punto p=new Punto();
    
    if((-this.a/this.b)!=(a/b)) {
    
    p.setY((int)(-((this.a*c)/(a+this.c))/(this.b-(this.a*b)/a)));
    p.setX((int)((c-(b*p.getY()))/a));
    
    }else {
    System.out.println("Rette parallele impossibile calcolare punto di intersezione");
    }
    
    return p;
  }
  /**Intersezione con l'asse X
  */
  public Punto IntersezioneAsseX() {
    Punto p=new Punto();
    p.setX((int)(-this.c/this.a));
    p.setY(0);
    return p;
  }
  /**Intersezione con l'asse Y
  */
  public Punto IntersezioneAsseY() {
    Punto p=new Punto();
    p.setX(0);
    p.setY((int)(-this.c/this.b));
    return p;
  }

}


/*
Richiamo il metodo intersezione dal main attraverso il seguente frammento:

Tastiera t5=new Tastiera();
Retta r8=new Retta();
Punto p13=new Punto();
System.out.println("Inserisci A: ");
r8.setA(t5.inputFloat());
System.out.println("Inserisci B: ");
r8.setB(t5.inputFloat());
System.out.println("Inserisci C: ");
r8.setC(t5.inputFloat());
p13=r8.IntersezioneAsseY();
System.out.println(p13.getX());
System.out.println(p13.getY());

*/
