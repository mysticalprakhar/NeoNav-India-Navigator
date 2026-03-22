import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashSet;
import javax.swing.Timer;

public class NeoNavSmartGUI extends JPanel {

    Image mapImage;

    HashMap<String, Point> cities = new HashMap<>();
    HashMap<String, ArrayList<String>> graph = new HashMap<>();
    HashMap<String, double[]> cityCoordinates = new HashMap<>(); // lat, lon

    String source = null;
    String destination = null;

    java.util.List<Point> route = new ArrayList<>();

    int step = 0;
    Timer timer;

    double totalDistance = 0;

    // Zoom + Pan
    double scale = 1.0;
    int offsetX = 0, offsetY = 0;
    int lastX, lastY;

    // Map bounds (India geographic coordinates - adjusted to match map aspect ratio)
    private static final double NORTH_LAT = 35.5;
    private static final double SOUTH_LAT = 7.5;
    private static final double WEST_LON = 67.0;
    private static final double EAST_LON = 98.0;

    // Map image size (900x650 = 1.385 aspect ratio)
    private static final int MAP_WIDTH = 900;
    private static final int MAP_HEIGHT = 650;

    public NeoNavSmartGUI() {

        mapImage = new ImageIcon("gui/india_map.png").getImage();

        // 🗺 Cities with geographic coordinates (latitude, longitude)
        // Verified coordinates for major Indian cities
        cityCoordinates.put("Delhi", new double[]{28.70, 77.10});
        cityCoordinates.put("Jaipur", new double[]{26.91, 75.79});
        cityCoordinates.put("Bhopal", new double[]{23.18, 79.99});
        cityCoordinates.put("Indore", new double[]{22.72, 75.86});
        cityCoordinates.put("Mumbai", new double[]{19.08, 72.88});
        cityCoordinates.put("Pune", new double[]{18.52, 73.86});
        cityCoordinates.put("Ahmedabad", new double[]{23.03, 72.57});
        cityCoordinates.put("Nagpur", new double[]{21.15, 79.09});
        cityCoordinates.put("Kolkata", new double[]{22.57, 88.36});
        cityCoordinates.put("Lucknow", new double[]{26.85, 80.95});
        cityCoordinates.put("Patna", new double[]{25.59, 85.14});
        cityCoordinates.put("Hyderabad", new double[]{17.39, 78.49});
        cityCoordinates.put("Bengaluru", new double[]{12.97, 77.59});
        cityCoordinates.put("Chennai", new double[]{13.08, 80.27});
        cityCoordinates.put("Kochi", new double[]{9.93, 76.27});
        cityCoordinates.put("Guwahati", new double[]{26.14, 91.74});

        // Convert geographic coordinates to pixel coordinates
        for(String city : cityCoordinates.keySet()) {
            double[] coord = cityCoordinates.get(city);
            Point pixelPoint = geoToPixel(coord[0], coord[1]);
            cities.put(city, pixelPoint);
        }

        // Graph
        graph.put("Delhi", new ArrayList<>(Arrays.asList("Jaipur","Lucknow")));
        graph.put("Jaipur", new ArrayList<>(Arrays.asList("Delhi","Ahmedabad","Bhopal")));
        graph.put("Ahmedabad", new ArrayList<>(Arrays.asList("Jaipur","Mumbai")));
        graph.put("Mumbai", new ArrayList<>(Arrays.asList("Ahmedabad","Pune","Nagpur")));
        graph.put("Pune", new ArrayList<>(Arrays.asList("Mumbai","Hyderabad")));
        graph.put("Bhopal", new ArrayList<>(Arrays.asList("Jaipur","Indore","Nagpur")));
        graph.put("Indore", new ArrayList<>(Arrays.asList("Bhopal","Mumbai")));
        graph.put("Nagpur", new ArrayList<>(Arrays.asList("Bhopal","Mumbai","Kolkata")));
        graph.put("Lucknow", new ArrayList<>(Arrays.asList("Delhi","Patna")));
        graph.put("Patna", new ArrayList<>(Arrays.asList("Lucknow","Kolkata")));
        graph.put("Kolkata", new ArrayList<>(Arrays.asList("Patna","Nagpur","Guwahati")));
        graph.put("Hyderabad", new ArrayList<>(Arrays.asList("Nagpur","Bengaluru","Chennai")));
        graph.put("Bengaluru", new ArrayList<>(Arrays.asList("Hyderabad","Chennai","Kochi")));
        graph.put("Chennai", new ArrayList<>(Arrays.asList("Hyderabad","Bengaluru")));
        graph.put("Kochi", new ArrayList<>(Arrays.asList("Bengaluru")));
        graph.put("Guwahati", new ArrayList<>(Arrays.asList("Kolkata")));
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                lastX = e.getX();
                lastY = e.getY();
            }

            public void mouseClicked(MouseEvent e) {

                if(source != null && destination != null) {
                    source = null;
                    destination = null;
                    route.clear();
                }

                int mx = (int)((e.getX() - offsetX)/scale);
                int my = (int)((e.getY() - offsetY)/scale);

                for(String city : cities.keySet()) {
                    Point p = cities.get(city);

                    if(Math.abs(mx - p.x) < 10 && Math.abs(my - p.y) < 10) {
                        if(source == null) source = city;
                        else {
                            destination = city;
                            predictRoute();
                        }
                        repaint();
                    }
                }
            }
        });

        // Drag (Pan)
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                offsetX += e.getX() - lastX;
                offsetY += e.getY() - lastY;
                lastX = e.getX();
                lastY = e.getY();
                repaint();
            }
        });

        // Zoom (scroll)
        addMouseWheelListener(e -> {
            if(e.getWheelRotation() < 0) scale *= 1.1;
            else scale /= 1.1;
            repaint();
        });

        // Animation
        timer = new Timer(300, e -> {
            if(step < route.size()) step++;
            repaint();
        });
        timer.start();
    }

    // Convert geographic coordinates (lat, lon) to pixel coordinates on map
    private Point geoToPixel(double latitude, double longitude) {
        // Calculate ranges
        double latRange = NORTH_LAT - SOUTH_LAT;
        double lonRange = EAST_LON - WEST_LON;

        // Normalize to 0-1 range (inverted for latitude since map top = north)
        double normLat = (NORTH_LAT - latitude) / latRange;
        double normLon = (longitude - WEST_LON) / lonRange;

        // Clamp to valid bounds
        normLat = Math.max(0, Math.min(1, normLat));
        normLon = Math.max(0, Math.min(1, normLon));

        // Scale to pixel coordinates
        int pixelX = (int)(normLon * MAP_WIDTH);
        int pixelY = (int)(normLat * MAP_HEIGHT);

        return new Point(pixelX, pixelY);
    }

    double getDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x-p2.x,2)+Math.pow(p1.y-p2.y,2))*2;
    }

    ArrayList<String> findRoute(String src,String dest) {

        Queue<ArrayList<String>> q = new LinkedList<>();
        HashSet<String> visited = new HashSet<>();

        ArrayList<String> path = new ArrayList<>();
        path.add(src);
        q.add(path);
        visited.add(src);

        while(!q.isEmpty()) {
            ArrayList<String> cur = q.poll();
            String last = cur.get(cur.size()-1);

            if(last.equals(dest)) return cur;

            for(String n: graph.getOrDefault(last,new ArrayList<>())) {
                if(!visited.contains(n)) {
                    visited.add(n);
                    ArrayList<String> newPath = new ArrayList<>(cur);
                    newPath.add(n);
                    q.add(newPath);
                }
            }
        }
        return new ArrayList<>();
    }

    void predictRoute() {

        route.clear();
        step = 0;

        ArrayList<String> path = findRoute(source,destination);

        for(String c:path) route.add(cities.get(c));

        totalDistance = 0;
        for(int i=0;i<route.size()-1;i++)
            totalDistance += getDistance(route.get(i),route.get(i+1));
    }

    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.translate(offsetX, offsetY);
        g2.scale(scale, scale);

        g.drawImage(mapImage,0,0,900,650,this);

        g.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g.setColor(Color.BLACK);

        if(source!=null && destination!=null){
            g.drawString("Route: "+source+" → "+destination,20,30);
            g.drawString("Distance: "+(int)totalDistance+" km",20,55);
        }

        g.setColor(Color.BLUE);
        for(String city:cities.keySet()){
            Point p=cities.get(city);
            g.fillOval(p.x-6,p.y-6,12,12);
            g.drawString(city,p.x+8,p.y-4);
        }

        g2.setColor(new Color(255,0,0,80));
        g2.setStroke(new BasicStroke(8));

        for(int i=0;i<step-1;i++){
            Point p1=route.get(i);
            Point p2=route.get(i+1);
            g2.drawLine(p1.x,p1.y,p2.x,p2.y);
        }

        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(4,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));

        for(int i=0;i<step-1;i++){
            Point p1=route.get(i);
            Point p2=route.get(i+1);
            g2.drawLine(p1.x,p1.y,p2.x,p2.y);
        }

        if(source!=null){
            Point s=cities.get(source);
            g.setColor(Color.GREEN);
            g.fillOval(s.x-8,s.y-8,16,16);
        }

        if(destination!=null){
            Point d=cities.get(destination);
            g.setColor(Color.RED);
            g.fillOval(d.x-8,d.y-8,16,16);
        }
    }

    public static void main(String[] args) {

        JFrame frame=new JFrame("NeoNav Ultimate Navigator");
        NeoNavSmartGUI panel=new NeoNavSmartGUI();

        frame.setLayout(new BorderLayout());

        JPanel top=new JPanel();
        JTextField src=new JTextField(10);
        JTextField dest=new JTextField(10);
        JButton btn=new JButton("Find Route");

        top.add(new JLabel("From:"));
        top.add(src);
        top.add(new JLabel("To:"));
        top.add(dest);
        top.add(btn);

        frame.add(top,BorderLayout.NORTH);
        frame.add(panel,BorderLayout.CENTER);

        btn.addActionListener(e->{
            panel.source=src.getText();
            panel.destination=dest.getText();
            panel.predictRoute();
            panel.repaint();
        });

        frame.setSize(900,650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}