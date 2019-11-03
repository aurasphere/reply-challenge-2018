public class Segmento{
 private Punto p1;
 private Punto p2;
   
   public int ccw (Punto p0,
           Punto p1,
           Punto p2)
  {
     int dx1,dx2,dy1,dy2;
  
     dx1 = p1.getX()-p0.getX(); 
     dy1 = p1.getY()-p0.getY();
     dx2 = p2.getX()-p0.getX(); 
     dy2 = p2.getY()-p0.getY();
  
     if (dx1*dy2 > dy1*dx2) return +1;
     if (dx1*dy2 < dy1*dx2) return -1;
     if ((dx1*dx2 < 0) || (dy1*dy2 < 0)) return -1; 
     if ((dx1*dx1+dy1*dy1) < (dx2*dx2+dy2*dy2)) return +1;
     return 0;   
  }
  
  
  
  public boolean interseca(Segmento l1, Segmento l2)
  {
     return ((ccw(l1.p1,l1.p2,l2.p1)*ccw(l1.p1,l1.p2,l2.p2)) <=0) &&
            ((ccw(l2.p1,l2.p2,l1.p1)*ccw(l2.p1,l2.p2,l1.p2)) <=0);
  }
  
}
