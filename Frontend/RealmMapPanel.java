package gmae;

import backend.RealmSpace;
import backend.Tile;
import backend.TileGroup;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class RealmMapPanel extends JPanel {
    private RealmSpace realmSpace;
    private int tileSize = 30;
    private int mapWidth = 20;
    private int mapHeight = 15;
    private Point selectedTile;
    private Map<Class<?>, Color> tileColors;
    
    public RealmMapPanel(RealmSpace realmSpace) {
        this.realmSpace = realmSpace;
        this.tileColors = new HashMap<>();
        initializeColors();
        setupMouseListener();
        setPreferredSize(new Dimension(mapWidth * tileSize, mapHeight * tileSize));
        setBackground(new Color(20, 20, 40));
        setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2));
    }
    
    private void initializeColors() {
        // Different colors for different tile types
        tileColors.put(Object.class, new Color(100, 100, 150)); // Default
        tileColors.put(String.class, new Color(50, 150, 50)); // Will be overridden by actual tile classes
    }
    
    private void setupMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / tileSize;
                int y = e.getY() / tileSize;
                selectedTile = new Point(x, y);
                repaint();
                
                Tile tile = realmSpace.getTopAt(selectedTile);
                String tileInfo = getTileInfo(tile, x, y);
                
                JOptionPane.showMessageDialog(RealmMapPanel.this, tileInfo, 
                    "Tile Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
    
    private String getTileInfo(Tile tile, int x, int y) {
        if (tile == null) {
            return "Empty tile at (" + x + ", " + y + ")";
        }
        
        String info = "Tile at (" + x + ", " + y + "):\n";
        info += "Type: " + tile.getClass().getSimpleName() + "\n";
        info += "Position: (" + tile.getX() + ", " + tile.getY() + ")";
        
        if (tile instanceof TileGroup) {
            TileGroup<?> group = (TileGroup<?>) tile;
            info += "\nTileGroup - covers area from (" + group.getX() + ", " + group.getY() + ")";
            info += "\nRepresentative: " + group.getRepresentative(new Point(x, y)).getClass().getSimpleName();
        }
        
        return info;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw grid
        drawGrid(g2d);
        
        // Draw tiles
        drawTiles(g2d);
        
        // Draw selection highlight
        if (selectedTile != null) {
            drawSelection(g2d);
        }
        
        g2d.dispose();
    }
    
    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(new Color(60, 60, 80));
        g2d.setStroke(new BasicStroke(1));
        
        for (int x = 0; x <= mapWidth; x++) {
            g2d.drawLine(x * tileSize, 0, x * tileSize, mapHeight * tileSize);
        }
        
        for (int y = 0; y <= mapHeight; y++) {
            g2d.drawLine(0, y * tileSize, mapWidth * tileSize, y * tileSize);
        }
    }
    
    private void drawTiles(Graphics2D g2d) {
        // Create a map of which positions are already covered
        Map<Point, Boolean> covered = new HashMap<>();
        
        // Draw tiles from the realm
        for (Tile tile : realmSpace.getAllTiles()) {
            if (tile instanceof TileGroup) {
                drawTileGroup(g2d, (TileGroup<?>) tile, covered);
            } else {
                drawSingleTile(g2d, tile, covered);
            }
        }
        
        // Fill remaining empty tiles with sample terrain for demonstration
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                Point p = new Point(x, y);
                if (!covered.containsKey(p)) {
                    if (Math.random() < 0.2) { // 20% chance of terrain
                        Color terrainColor = getRandomTerrainColor();
                        drawTileAt(g2d, x, y, terrainColor);
                    }
                }
            }
        }
    }
    
    private void drawTileGroup(Graphics2D g2d, TileGroup<?> group, Map<Point, Boolean> covered) {
        Color groupColor = getTileColor(group.getRepresentative(new Point(group.getX(), group.getY())));
        
        // Use the new helper methods to get bounds
        int minX = group.getMinX();
        int maxX = group.getMaxX();
        int minY = group.getMinY();
        int maxY = group.getMaxY();
        
        // Draw the entire area
        for (int x = minX; x <= maxX && x < mapWidth; x++) {
            for (int y = minY; y <= maxY && y < mapHeight; y++) {
                Point p = new Point(x, y);
                drawTileAt(g2d, x, y, groupColor);
                covered.put(p, true);
                
                // Draw edge highlight for TileGroups
                if (group.onEdge(p)) {
                    drawEdgeHighlight(g2d, x, y);
                }
            }
        }
    }
    
    private void drawSingleTile(Graphics2D g2d, Tile tile, Map<Point, Boolean> covered) {
        Point p = new Point(tile.getX(), tile.getY());
        if (tile.getX() < mapWidth && tile.getY() < mapHeight) {
            drawTileAt(g2d, tile.getX(), tile.getY(), getTileColor(tile));
            covered.put(p, true);
        }
    }
    
    private void drawTileAt(Graphics2D g2d, int x, int y, Color color) {
        int px = x * tileSize;
        int py = y * tileSize;
        
        // Fill tile
        g2d.setColor(color);
        g2d.fillRect(px + 1, py + 1, tileSize - 2, tileSize - 2);
        
        // Add some texture/pattern
        g2d.setColor(color.darker());
        g2d.drawRect(px + 2, py + 2, tileSize - 4, tileSize - 4);
    }
    
    private void drawEdgeHighlight(Graphics2D g2d, int x, int y) {
        int px = x * tileSize;
        int py = y * tileSize;
        
        g2d.setColor(new Color(255, 255, 255, 50)); // Semi-transparent white
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(px + 3, py + 3, tileSize - 6, tileSize - 6);
    }
    
    private void drawSelection(Graphics2D g2d) {
        int x = selectedTile.x * tileSize;
        int y = selectedTile.y * tileSize;
        
        g2d.setColor(new Color(255, 255, 0));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(x + 1, y + 1, tileSize - 2, tileSize - 2);
    }
    
    private Color getTileColor(Tile tile) {
        // Different colors for different tile types
        String className = tile.getClass().getSimpleName();
        switch (className) {
            case "Ground": return new Color(50, 150, 50); // Green terrain
            case "Player": return new Color(255, 255, 0); // Yellow players
            case "Mob": return new Color(255, 0, 0); // Red enemies
            case "GrassTile": return new Color(50, 150, 50);
            case "MountainTile": return new Color(100, 50, 50);
            case "WaterTile": return new Color(50, 50, 150);
            case "SandTile": return new Color(150, 150, 50);
            case "SwampTile": return new Color(100, 50, 100);
            case "ForestTile": return new Color(25, 100, 25);
            case "DesertTile": return new Color(200, 180, 100);
            case "SnowTile": return new Color(220, 220, 255);
            default: return new Color(100, 100, 150);
        }
    }
    
    private Color getRandomTerrainColor() {
        Color[] colors = {
            new Color(50, 150, 50),  // Grass
            new Color(100, 50, 50), // Mountain
            new Color(50, 50, 150), // Water
            new Color(150, 150, 50), // Sand
            new Color(100, 50, 100), // Swamp
            new Color(25, 100, 25), // Forest
            new Color(200, 180, 100), // Desert
            new Color(220, 220, 255) // Snow
        };
        return colors[(int) (Math.random() * colors.length)];
    }
    
    public void setRealmSpace(RealmSpace realmSpace) {
        this.realmSpace = realmSpace;
        repaint();
    }
    
    public Point getSelectedTile() {
        return selectedTile;
    }
}
