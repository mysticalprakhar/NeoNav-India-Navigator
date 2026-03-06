import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class NeoNavMapGUI extends JPanel {

    Image mapImage;

    ArrayList<Point> route = new ArrayList<>();

    int step = 0;

    public NeoNavMapGUI() {

        mapImage = new ImageIcon("gui/india_map.png").getImage();

        route.add(new Point(350,120)); // Delhi
        route.add(new Point(320,200)); // Jaipur
        route.add(new Point(300,300)); // Mumbai
        route.add(new Point(360,400)); // Bengaluru

        Timer timer = new Timer(500,e->{
            if(step < route.size())
                step++;

            repaint();
        });

        timer.start();
    }

    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        g.drawImage(mapImage,0,0,getWidth(),getHeight(),this);

        g.setColor(Color.RED);

        for(int i=0;i<step-1;i++)
        {
            Point p1 = route.get(i);
            Point p2 = route.get(i+1);

            g.drawLine(p1.x,p1.y,p2.x,p2.y);
        }

        g.setColor(Color.BLUE);

        for(Point p:route)
        {
            g.fillOval(p.x-5,p.y-5,10,10);
        }
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("NeoNav India Route Navigator");

        NeoNavMapGUI panel = new NeoNavMapGUI();

        frame.add(panel);

        frame.setSize(800,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
