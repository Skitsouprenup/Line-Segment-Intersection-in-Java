import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.EventQueue;

public class LinePoly {
	
	//mouse-controlled line endpoints
	Point2D ln1_endpoint1,ln1_endpoint2;
	//The LineSegment datatype might confuse people.
	//To put it simply, this declaration means(that)
	//the rect/penta variable is a shape that is composed by
	//line segments.
	LineSegment[] rect;
	LineSegment[] penta;
	
	boolean isColliding;
	
	public static void main(String[] args) {
		new LinePoly();
	}
	
	public LinePoly() {
		
		ln1_endpoint1 = new Point2D.Float(0f,0f);
		ln1_endpoint2 = new Point2D.Float();
		
		rect = new LineSegment[]{
			new LineSegment(new Point2D.Float(200f,150f),new Point2D.Float(260f,150f)),
			new LineSegment(new Point2D.Float(260f,150f),new Point2D.Float(260f,210f)),
			new LineSegment(new Point2D.Float(260f,210f),new Point2D.Float(200f,210f)),
			new LineSegment(new Point2D.Float(200f,210f),new Point2D.Float(200f,150f))
		};
		
		penta = new LineSegment[]{
			new LineSegment(new Point2D.Float(240f,250f),new Point2D.Float(280f,220f)),
			new LineSegment(new Point2D.Float(280f,220f),new Point2D.Float(310f,250f)),
			new LineSegment(new Point2D.Float(310f,250f),new Point2D.Float(270f,270f)),
			new LineSegment(new Point2D.Float(270f,270f),new Point2D.Float(240f,230f)),
			new LineSegment(new Point2D.Float(240f,230f),new Point2D.Float(240f,250f))
		};
		
		
		EventQueue.invokeLater(new Runnable(){
			
			@Override
			public void run() {
				JFrame jf = new JFrame("LinePoly");
				Panel pnl = new Panel();
				pnl.addMouseMotionListener(new MouseMotion());
				jf.add(pnl);
				jf.pack();
				jf.setResizable(false);
				jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				jf.setLocationRelativeTo(null);
				jf.setVisible(true);
			}
			
		});
		
	}
	
	void checkIntersection(LineSegment shape){
		//ln1
		float x1 = (float)ln1_endpoint1.getX();
		float y1 = (float)ln1_endpoint1.getY();
		float x2 = (float)ln1_endpoint2.getX();
		float y2 = (float)ln1_endpoint2.getY();
		
		//ln2
		float x3 = (float)shape.getEndPoint1().getX();
		float y3 = (float)shape.getEndPoint1().getY();
		float x4 = (float)shape.getEndPoint2().getX();
		float y4 = (float)shape.getEndPoint2().getY();
		
		//get the ratio of the slope of ln1 endpoints
		float snum = 0f; float sdenom = 0f;
		snum = y2 - y1;
		//reverse the sign of sdenom
		sdenom = x1 - x2;
		//A and B coefficients
		float a1 = snum;
		float b1 = sdenom;
		
		//get the ratio of the slope of ln2 endpoints
		snum = 0f; sdenom = 0f;
		snum = y4 - y3;
		sdenom = x3 - x4;
		float a2 = snum;
		float b2 = sdenom;
		
		//find C by using the standard form of linear equation
		//and using one of the given points
		float c1 = a1*x1 + b1*y1;
		float c2 = a2*x3 + b2*y3;
		
		//We need to use the cramer's rule to solve two lines that have a solution,
		//no solution, or infinitely many solutions. There are many ways to find
		//a solution for systems of linear equations like elimination or substitution
		//but cramer's rule is simpler to implement in a script. To do cramer's rule,
		//Our line equation should be in standard form.
		
		//Steps for cramer's rule:
		//#1 find the determinant of the coefficients of the standard line equation(A and B)
		float cDet = a1*b2 - a2*b1;
		
		//check if the determinant of the coefficients are zero. If so, then the lines are either
		//parallel or the same. Otherwise, there's a solution.
		if(cDet != 0) {
			
		  //#2 substitute C's coefficients to X's coefficient to find x determinant and substitute
		  //C's coefficients to Y's coefficient to find y determinant.
		  float xDet = (c1*b2 - c2*b1)/cDet;
		  float yDet = (a1*c2 - a2*c1)/cDet;
		  
		  if( (xDet >= Math.min(x1,x2) && xDet <= Math.max(x1,x2)) &&
		      (xDet >= Math.min(x3,x4) && xDet <= Math.max(x3,x4)) &&
			  (yDet >= Math.min(y1,y2) && yDet <= Math.max(y1,y2)) &&
		      (yDet >= Math.min(y3,y4) && yDet <= Math.max(y3,y4)) ){
			  shape.getIntersectionPoint().setFrame(xDet - 3f, yDet - 3f, 6f, 6f);
		      shape.setIsIntersecting(true);
			}
		  else shape.setIsIntersecting(false);
		}
		
	}
	
	void updateIntersection(LineSegment[] shape){
		
		for(int i = 0; i < shape.length; i++)
			checkIntersection(shape[i]);
		
	}
	
	void drawLines(LineSegment[] shape,Graphics2D g2d){
		for(int i = 0; i < shape.length; i++){
			
			if(shape[i].getIsIntersecting()){
				g2d.setPaint(Color.YELLOW);
				g2d.fill(shape[i].getIntersectionPoint());
				g2d.setPaint(Color.RED);
			}
			else g2d.setPaint(Color.GREEN);
			
			g2d.drawLine((int)shape[i].getEndPoint1().getX(),(int)shape[i].getEndPoint1().getY(),
					     (int)shape[i].getEndPoint2().getX(),(int)shape[i].getEndPoint2().getY());	
			
		}
	}
	
	void updateData(){
		//System.out.println("updating...");
		updateIntersection(rect);
		updateIntersection(penta);
	}
	
	void drawObjects(Graphics2D g2d){
		//System.out.println("drawing objects...");
		g2d.setPaint(Color.GREEN);
		g2d.drawLine((int)ln1_endpoint1.getX(),(int)ln1_endpoint1.getY(),
					(int)ln1_endpoint2.getX(),(int)ln1_endpoint2.getY());
		
		drawLines(rect,g2d);
		drawLines(penta,g2d);
	}
	
	class Panel extends JPanel {
		
		Panel(){
			Timer timer = new Timer(16, new ActionListener(){
				
				@Override
				public void actionPerformed(ActionEvent e){
					updateData();
					repaint();
				}
			});
			timer.start();
		}
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(400,400);
		}
		
		@Override
		protected void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setPaint(Color.BLACK);
			g2d.fillRect(0,0,getWidth(),getHeight());
			drawObjects(g2d);
			g2d.setPaint(Color.WHITE);
			g2d.drawString("a portion of a shape will turn red if there's an intersection", 60f, 20f);
			g2d.dispose();
		}
	}
	
	class MouseMotion implements MouseMotionListener {
	
		@Override
		public void mouseDragged(MouseEvent e){}
	
		@Override
		public void mouseMoved(MouseEvent e){
			ln1_endpoint2.setLocation(e.getX(),e.getY());
		}
	}
	
}

class LineSegment {
	Point2D endPoint1;
	Point2D endPoint2;
	Ellipse2D intersectionPoint;
	boolean isIntersecting;
	
	LineSegment(Point2D endPoint1, Point2D endPoint2){
		this.endPoint1 = endPoint1;
		this.endPoint2 = endPoint2;
		
		intersectionPoint = new Ellipse2D.Float();
	}
	
	Point2D getEndPoint1(){return endPoint1;}
	Point2D getEndPoint2(){return endPoint2;}
    Ellipse2D getIntersectionPoint(){return intersectionPoint;}
	boolean getIsIntersecting(){return isIntersecting;}
	
	void setIsIntersecting(boolean isIntersecting){
		this.isIntersecting = isIntersecting;
	}
	
	
}

