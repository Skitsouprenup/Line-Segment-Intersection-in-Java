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

public class Lineline {
	
	//mouse-controlled line endpoints
	Point2D ln1_endpoint1,ln1_endpoint2;
	//stationary line endpoints
	Point2D ln2_endpoint1,ln2_endpoint2;
	//point of intersection
	Ellipse2D intersectionPoint;
	
	boolean isColliding;
	
	public static void main(String[] args) {
		new Lineline();
	}
	
	public Lineline() {
		
		ln1_endpoint1 = new Point2D.Float(0f,0f);
		ln1_endpoint2 = new Point2D.Float();
		
		ln2_endpoint1 = new Point2D.Float(100f,150f);
		ln2_endpoint2 = new Point2D.Float(260f,180f);
		
		intersectionPoint = new Ellipse2D.Float();
		
		EventQueue.invokeLater(new Runnable(){
			
			@Override
			public void run() {
				JFrame jf = new JFrame("Lineline");
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
	
	void updateData(){
		//System.out.println("updating...");
	}
	
	void drawObjects(Graphics2D g2d){
		//System.out.println("drawing objects...");
		if(!isColliding) g2d.setPaint(Color.GREEN);
		else g2d.setPaint(Color.RED);
		
		g2d.drawLine((int)ln1_endpoint1.getX(),(int)ln1_endpoint1.getY(),
					(int)ln1_endpoint2.getX(),(int)ln1_endpoint2.getY());	
		g2d.setPaint(Color.GREEN);
		g2d.drawLine((int)ln2_endpoint1.getX(),(int)ln2_endpoint1.getY(),
					(int)ln2_endpoint2.getX(),(int)ln2_endpoint2.getY());
		
		if(isColliding){
			g2d.setPaint(Color.YELLOW);
			g2d.fill(intersectionPoint);
		}
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
			g2d.drawString("Mouse-controlled line will turn red if there's a collision", 60f, 20f);
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

