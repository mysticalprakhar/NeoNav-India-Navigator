import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class NeoNavSmartGUI extends JPanel {

    Image mapImage;

    HashMap<String, Point> cities = new HashMap<>();

    String source = null;
    String destination = null;

    java.util.List<Point> route = new ArrayList<>();

    public NeoNavSmartGUI() {

        mapImage = new ImageIcon("gui/india_map.png").getImage();

        cities.put("Delhi", new Point(350,120));
        cities.put("Jaipur", new Point(320,180));
        cities.put("Mumbai", new Point(300,300));
        cities.put("Bhopal", new Point(360,240));
        cities.put("Bengaluru", new Point(380,420));

        addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {

                for(String city : cities.keySet()) {

                    Point p = cities.get(city);

                    if(e.getX() >= p.x-10 && e.getX() <= p.x+10 &&
                       e.getY() >= p.y-10 && e.getY() <= p.y+10)
                    {
                        if(source == null) {

                            source = city;
                            System.out.println("Source Selected: "+city);

                        }
                        else if(destination == null) {

                            destination = city;
                            System.out.println("Destination Selected: "+city);

                            predictRoute();
                        }

                        repaint();
                    }
                }
            }
        });
    }

    void predictRoute() {

        System.out.println("\nAI Route Prediction:");

        route.clear();

        if(source.equals("Delhi") && destination.equals("Mumbai")) {

            route.add(cities.get("Delhi"));
            route.add(cities.get("Jaipur"));
            route.add(cities.get("Mumbai"));
        }

        else if(source.equals("Delhi") && destination.equals("Bengaluru")) {

            route.add(cities.get("Delhi"));
            route.add(cities.get("Bhopal"));
            route.add(cities.get("Bengaluru"));
        }

        else {

            route.add(cities.get(source));
            route.add(cities.get(destination));
        }

        System.out.println("AI Suggested Route: "+source+" → "+destination);
    }

    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        g.drawImage(mapImage,0,0,getWidth(),getHeight(),this);

        g.setColor(Color.BLUE);

        for(String city:cities.keySet()) {

            Point p = cities.get(city);

            g.fillOval(p.x-5,p.y-5,10,10);
            g.drawString(city,p.x+5,p.y);
        }

        g.setColor(Color.RED);

        for(int i=0;i<route.size()-1;i++) {

            Point p1 = route.get(i);
            Point p2 = route.get(i+1);

            g.drawLine(p1.x,p1.y,p2.x,p2.y);
        }
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("NeoNav AI Smart Navigator");

        NeoNavSmartGUI panel = new NeoNavSmartGUI();

        frame.add(panel);

        frame.setSize(800,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
