import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class NeoNavMapGUI extends JPanel {

    Image mapImage;
    ArrayList<Point> route = new ArrayList<>();
    int step = 0;

    // Map bounds (India geographic coordinates)
    private static final double NORTH_LAT = 35.0;
    private static final double SOUTH_LAT = 8.0;
    private static final double WEST_LON = 68.0;
    private static final double EAST_LON = 97.0;

    // Map image size
    private static final int MAP_WIDTH = 800;
    private static final int MAP_HEIGHT = 600;

    // City coordinates (latitude, longitude)
    private static final HashMap<String, double[]> CITY_COORDS = new HashMap<String, double[]>() {{
        put("Delhi", new double[]{28.7041, 77.1025});
        put("Jaipur", new double[]{26.9124, 75.7873});
        put("Mumbai", new double[]{19.0760, 72.8777});
        put("Bengaluru", new double[]{12.9716, 77.5946});
    }};

    public NeoNavMapGUI() {

        mapImage = new ImageIcon("gui/india_map.png").getImage();

        // Convert city coordinates to pixel positions
        route.add(geoToPixel(28.7041, 77.1025)); // Delhi
        route.add(geoToPixel(26.9124, 75.7873)); // Jaipur
        route.add(geoToPixel(19.0760, 72.8777)); // Mumbai
        route.add(geoToPixel(12.9716, 77.5946)); // Bengaluru

        Timer timer = new Timer(500, e -> {
            if(step < route.size())
                step++;
            repaint();
        });

        timer.start();
    }

    // Convert geographic coordinates (lat, lon) to pixel coordinates on map
    private Point geoToPixel(double latitude, double longitude) {
        // Linear projection: map geographic bounds to pixel bounds
        double latRange = NORTH_LAT - SOUTH_LAT;
        double lonRange = EAST_LON - WEST_LON;

        // Normalize to 0-1
        double normLat = (NORTH_LAT - latitude) / latRange;  // Invert latitude (north is top)
        double normLon = (longitude - WEST_LON) / lonRange;

        // Scale to map dimensions
        int pixelX = (int)(normLon * MAP_WIDTH);
        int pixelY = (int)(normLat * MAP_HEIGHT);

        return new Point(pixelX, pixelY);
    }

    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        g.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);

        g.setColor(Color.RED);

        for(int i = 0; i < step - 1; i++)
        {
            Point p1 = route.get(i);
            Point p2 = route.get(i + 1);

            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        g.setColor(Color.BLUE);

        for(Point p : route)
        {
            g.fillOval(p.x - 5, p.y - 5, 10, 10);
        }
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("NeoNav India Route Navigator");

        NeoNavMapGUI panel = new NeoNavMapGUI();

        frame.add(panel);

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
