package Backend;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

import Backend.Tiles.*;

/**
 * Manages sprite loading and mapping for different tile types
 */
public class SpriteManager {
    private static SpriteManager instance;
    private BufferedImage spritesheet;
    private Map<Class<?>, BufferedImage> tileSprites;
    private int spriteSize = 20; // Each sprite is 20x20 pixels
    
    private SpriteManager() {
        tileSprites = new HashMap<>();
        loadSpritesheet();
        initializeSpriteMappings();
    }
    
    public static synchronized SpriteManager getInstance() {
        if (instance == null) {
            instance = new SpriteManager();
        }
        return instance;
    }
    
    private void loadSpritesheet() {
        try {
            // Load the spritesheet from the root directory
            spritesheet = ImageIO.read(getClass().getClassLoader().getResourceAsStream("spritesheet.png"));
            if (spritesheet == null) {
                // Try loading from file system
                spritesheet = ImageIO.read(new java.io.File("spritesheet.png"));
            }
        } catch (IOException e) {
            System.err.println("Failed to load spritesheet: " + e.getMessage());
            // Create a fallback spritesheet with colored squares
            createFallbackSpritesheet();
        }
    }
    
    private void createFallbackSpritesheet() {
        spritesheet = new BufferedImage(160, 20, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g2d = spritesheet.createGraphics();
        
        // Create colored squares as fallback sprites
        java.awt.Color[] colors = {
            new java.awt.Color(50, 150, 50),   // Ground - Green
            new java.awt.Color(255, 255, 0),  // Player - Yellow
            new java.awt.Color(255, 0, 0),     // Mob - Red
            new java.awt.Color(255, 215, 0),   // Relic - Gold
            new java.awt.Color(150, 50, 50),   // Trap - Dark Red
            new java.awt.Color(100, 100, 200), // NPC - Blue
            new java.awt.Color(50, 50, 50),    // Empty - Dark Gray
            new java.awt.Color(200, 200, 200)  // Default - Light Gray
        };
        
        for (int i = 0; i < colors.length; i++) {
            g2d.setColor(colors[i]);
            g2d.fillRect(i * spriteSize, 0, spriteSize, spriteSize);
            g2d.setColor(colors[i].darker());
            g2d.drawRect(i * spriteSize, 0, spriteSize, spriteSize);
        }
        g2d.dispose();
    }
    
    private void initializeSpriteMappings() {
        if (spritesheet == null) return;
        
        // Map tile classes to sprite positions on the spritesheet
        mapSpriteToTile(Ground.class, 7); 
        mapSpriteToTile(Player.class, 1);   
        mapSpriteToTile(Mob.class, 4);     
        mapSpriteToTile(RelicTile.class, 5); 
        mapSpriteToTile(TrapTile.class, 6);
        mapSpriteToTile(NPC.class, 3);        
        mapSpriteToTile(EmptyTile.class, 7);  
    }
    
    private void mapSpriteToTile(Class<?> tileClass, int spriteIndex) {
        if (spritesheet != null && spriteIndex >= 0) {
            int y = spriteIndex * spriteSize;  // Use Y coordinate for vertical layout
            if (y + spriteSize <= spritesheet.getHeight()) {  // Check height instead of width
                BufferedImage sprite = spritesheet.getSubimage(0, y, spriteSize, spriteSize);
                tileSprites.put(tileClass, sprite);
            }
        }
    }
    
    public BufferedImage getSpriteForTile(Tile tile, RealmSpace realmSpace) {
        if (tile == null) {
            return getDefaultSprite();
        }
        
        // Special handling for Player 1 vs Player 2
        if (tile instanceof Player) {
            return getPlayerSprite((Player) tile, realmSpace);
        }
        
        // Try to get sprite for exact class
        BufferedImage sprite = tileSprites.get(tile.getClass());
        
        if (sprite != null) {
            return sprite;
        }
        
        // Try parent classes if exact match not found
        Class<?> superClass = tile.getClass().getSuperclass();
        while (superClass != null && superClass != Object.class) {
            sprite = tileSprites.get(superClass);
            if (sprite != null) {
                return sprite;
            }
            superClass = superClass.getSuperclass();
        }
        
        // Return default sprite if no match found
        return getDefaultSprite();
    }
    
    public BufferedImage getSpriteForTile(Tile tile) {
        return getSpriteForTile(tile, null);
    }
    
    private BufferedImage getPlayerSprite(Player player, RealmSpace realmSpace) {
        if (realmSpace != null) {
            // Check if this is player1 or player2 in the realm space
            Player player1 = realmSpace.getPlayerOne();
            Player player2 = realmSpace.getPlayerTwo();
            
            if (player == player1) {
                return tileSprites.get(Player.class); // Player 1 gets default player sprite (index 1)
            } else if (player == player2) {
                return getScaledSpriteFromIndex(2); // Player 2 gets different sprite (index 2)
            }
        }
        
        // Fallback to position-based detection if realm space is not available
        if (player.getX() <= 0) {
            return tileSprites.get(Player.class); // Player 1 gets default player sprite
        } else {
            return getScaledSpriteFromIndex(2); // Player 2 gets different sprite
        }
    }
    
    private BufferedImage getScaledSpriteFromIndex(int index) {
        if (spritesheet != null && index >= 0) {
            int y = index * spriteSize;
            if (y + spriteSize <= spritesheet.getHeight()) {
                return spritesheet.getSubimage(0, y, spriteSize, spriteSize);
            }
        }
        return getDefaultSprite();
    }
    
    /**
     * Get a scaled sprite from a specific index on the spritesheet
     */
    public BufferedImage getScaledSpriteFromIndex(int index, int targetSize) {
        BufferedImage original = getScaledSpriteFromIndex(index);
        if (original == null) {
            return null;
        }
        
        BufferedImage scaled = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g2d = scaled.createGraphics();
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, 
                           java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(original, 0, 0, targetSize, targetSize, null);
        g2d.dispose();
        
        return scaled;
    }
    
    private BufferedImage getDefaultSprite() {
        // Return the last sprite as default, or create one if needed
        BufferedImage defaultSprite = tileSprites.get(Object.class);
        if (defaultSprite == null && spritesheet != null) {
            // Use the last sprite in the sheet as default
            int lastIndex = (spritesheet.getWidth() / spriteSize) - 1;
            if (lastIndex >= 0) {
                defaultSprite = spritesheet.getSubimage(lastIndex * spriteSize, 0, spriteSize, spriteSize);
                tileSprites.put(Object.class, defaultSprite);
            }
        }
        return defaultSprite;
    }
    
    public int getSpriteSize() {
        return spriteSize;
    }
    
    /**
     * Reload spritesheet - useful for dynamic sprite updates
     */
    public void reloadSpritesheet() {
        tileSprites.clear();
        loadSpritesheet();
        initializeSpriteMappings();
    }
    
    /**
     * Get a scaled version of a sprite for different tile sizes
     */
    public BufferedImage getScaledSprite(Tile tile, int targetSize) {
        return getScaledSprite(tile, targetSize, null);
    }
    
    /**
     * Get a scaled version of a sprite for different tile sizes with realm space context
     */
    public BufferedImage getScaledSprite(Tile tile, int targetSize, RealmSpace realmSpace) {
        BufferedImage original = getSpriteForTile(tile, realmSpace);
        if (original == null) {
            return null;
        }
        
        BufferedImage scaled = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g2d = scaled.createGraphics();
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, 
                           java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(original, 0, 0, targetSize, targetSize, null);
        g2d.dispose();
        
        return scaled;
    }
}
