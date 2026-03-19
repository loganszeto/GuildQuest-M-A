package Frontend;

import javax.swing.*;

import Backend.RealmSpace;
import Backend.Tiles.Tile;
import Backend.Tiles.TileGroup;
import Backend.Tiles.Ground;
import Backend.SpriteManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class RealmMapPanel extends JPanel {
    private RealmSpace realmSpace;
    private int tileSize = 30;
    private int mapWidth = 20;
    private int mapHeight = 15;
    private Point selectedTile;
    private SpriteManager spriteManager;
    private int offsetX = 0;  // Offset for handling negative coordinates
    private int offsetY = 0;
    
    public RealmMapPanel(RealmSpace realmSpace) {
        this.realmSpace = realmSpace;
        this.spriteManager = SpriteManager.getInstance();
        calculateOffsets();
        setupMouseListener();
        updatePreferredSize();
        setBackground(new Color(20, 20, 40));
        setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2));
    }
    
    private void calculateOffsets() {
        // Calculate offsets to center the realm and handle negative coordinates
        int realmMinX = realmSpace.getAllTiles().stream()
            .mapToInt(tile -> Math.min(tile.getX(), tile instanceof TileGroup ? ((TileGroup<?>) tile).getMinX() : tile.getX()))
            .min().orElse(0);
        int realmMinY = realmSpace.getAllTiles().stream()
            .mapToInt(tile -> Math.min(tile.getY(), tile instanceof TileGroup ? ((TileGroup<?>) tile).getMinY() : tile.getY()))
            .min().orElse(0);
        
        // Set offsets to make the minimum coordinate 0,0 for display
        offsetX = -realmMinX;
        offsetY = -realmMinY;
    }
    
    private void updatePreferredSize() {
        // Calculate the actual realm bounds
        int realmMinX = realmSpace.getAllTiles().stream()
            .mapToInt(tile -> Math.min(tile.getX(), tile instanceof TileGroup ? ((TileGroup<?>) tile).getMinX() : tile.getX()))
            .min().orElse(0);
        int realmMaxX = realmSpace.getAllTiles().stream()
            .mapToInt(tile -> Math.max(tile.getX(), tile instanceof TileGroup ? ((TileGroup<?>) tile).getMaxX() : tile.getX()))
            .max().orElse(0);
        int realmMinY = realmSpace.getAllTiles().stream()
            .mapToInt(tile -> Math.min(tile.getY(), tile instanceof TileGroup ? ((TileGroup<?>) tile).getMinY() : tile.getY()))
            .min().orElse(0);
        int realmMaxY = realmSpace.getAllTiles().stream()
            .mapToInt(tile -> Math.max(tile.getY(), tile instanceof TileGroup ? ((TileGroup<?>) tile).getMaxY() : tile.getY()))
            .max().orElse(0);
        
        // Update map dimensions to fit the entire realm
        mapWidth = realmMaxX - realmMinX + 1;
        mapHeight = realmMaxY - realmMinY + 1;
        
        setPreferredSize(new Dimension(mapWidth * tileSize, mapHeight * tileSize));
    }
    
    private void setupMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / tileSize - offsetX;  // Apply offset to get world coordinates
                int y = e.getY() / tileSize - offsetY;  // Apply offset to get world coordinates
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
                        // Use a default ground sprite for random terrain
                        BufferedImage groundSprite = spriteManager.getScaledSprite(new Ground(p), tileSize);
                        drawSpriteAt(g2d, x, y, groundSprite);
                    }
                }
            }
        }
    }
    
    private void drawTileGroup(Graphics2D g2d, TileGroup<?> group, Map<Point, Boolean> covered) {
        // Get the representative tile to determine which sprite to use
        Tile representative = group.getRepresentative(new Point(group.getX(), group.getY()));
        BufferedImage sprite = spriteManager.getScaledSprite(representative, tileSize, realmSpace);
        
        // Use the new helper methods to get bounds
        int minX = group.getMinX();
        int maxX = group.getMaxX();
        int minY = group.getMinY();
        int maxY = group.getMaxY();
        
        // Draw the entire area
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                Point p = new Point(x, y);
                // Apply offsets for display coordinates
                int displayX = x + offsetX;
                int displayY = y + offsetY;
                drawSpriteAt(g2d, displayX, displayY, sprite);
                covered.put(p, true);
                
                // Draw edge highlight for TileGroups
                if (group.onEdge(p)) {
                    drawEdgeHighlight(g2d, displayX, displayY);
                }
            }
        }
    }
    
    private void drawSingleTile(Graphics2D g2d, Tile tile, Map<Point, Boolean> covered) {
        Point p = new Point(tile.getX(), tile.getY());
        // Apply offsets for display coordinates
        int displayX = tile.getX() + offsetX;
        int displayY = tile.getY() + offsetY;
        BufferedImage scaledSprite = spriteManager.getScaledSprite(tile, tileSize, realmSpace);
        drawSpriteAt(g2d, displayX, displayY, scaledSprite);
        covered.put(p, true);
    }
    
    private void drawSpriteAt(Graphics2D g2d, int x, int y, BufferedImage sprite) {
        int px = x * tileSize;
        int py = y * tileSize;
        
        if (sprite != null) {
            g2d.drawImage(sprite, px + 1, py + 1, tileSize - 2, tileSize - 2, null);
        } else {
            // Fallback to colored rectangle if sprite is null
            g2d.setColor(new Color(100, 100, 150));
            g2d.fillRect(px + 1, py + 1, tileSize - 2, tileSize - 2);
            g2d.setColor(new Color(80, 80, 120));
            g2d.drawRect(px + 2, py + 2, tileSize - 4, tileSize - 4);
        }
    }
    
    private void drawEdgeHighlight(Graphics2D g2d, int x, int y) {
        int px = x * tileSize;
        int py = y * tileSize;
        
        g2d.setColor(new Color(255, 255, 255, 50)); // Semi-transparent white
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(px + 3, py + 3, tileSize - 6, tileSize - 6);
    }
    
    private void drawSelection(Graphics2D g2d) {
        if (selectedTile != null) {
            // Apply offsets to convert world coordinates to display coordinates
            int x = (selectedTile.x + offsetX) * tileSize;
            int y = (selectedTile.y + offsetY) * tileSize;
            
            g2d.setColor(new Color(255, 255, 0));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(x + 1, y + 1, tileSize - 2, tileSize - 2);
        }
    }
    
    public void setRealmSpace(RealmSpace realmSpace) {
        this.realmSpace = realmSpace;
        repaint();
    }
    
    public Point getSelectedTile() {
        return selectedTile;
    }
}
