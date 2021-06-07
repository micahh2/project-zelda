package projectzelda.map;

import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.net.URL;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import projectzelda.engine.*;
import projectzelda.gfx.MediaInfo;
import projectzelda.Const;

public class Map implements MediaInfo, WorldInfo {
    public List<Layer> layers = new LinkedList<Layer>();
    public List<Tileset> tilesets = new LinkedList<Tileset>();
    public List<ObjectGroup> objectgroups = new LinkedList<ObjectGroup>();
    public Polygon polygon;

    public int width;
    public int height;

    public int tileWidth;
    public int tileHeight;

    public int pixelWidth;
    public int pixelHeight;

    public static final int FLIPPED_HORIZONTALLY_FLAG = 0x80000000;
    public static final int FLIPPED_VERTICALLY_FLAG = 0x40000000;
    public static final int FLIPPED_DIAGONALLY_FLAG = 0x20000000;

    public final HashMap<Integer, TileAnimation> animationFrames = new HashMap<Integer, TileAnimation>();
    public final HashMap<Integer, List<ImageRefTo>> animationTiles = new HashMap<Integer, List<ImageRefTo>>();

    public ScreenInfo screenInfo;

    public Map(String src, ScreenInfo screenInfo) {

        this.screenInfo = screenInfo;

        try {

            URL resourceUri = getClass().getResource(src);
            File file = new File(resourceUri.getPath());

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            // Read basic map info
            Node mapNode = document.getElementsByTagName("map").item(0);
            NamedNodeMap mapAttrs = mapNode.getAttributes();
            width = Integer.parseInt(mapAttrs.getNamedItem("width").getTextContent());
            height = Integer.parseInt(mapAttrs.getNamedItem("height").getTextContent());
            tileWidth = Integer.parseInt(mapAttrs.getNamedItem("tilewidth").getTextContent());
            tileHeight = Integer.parseInt(mapAttrs.getNamedItem("tileheight").getTextContent());
            pixelWidth = width * tileWidth;
            pixelHeight = height * tileHeight;

            // Read the layers
            NodeList nodeLayers = document.getElementsByTagName("layer");
            for (int i = 0; i < nodeLayers.getLength(); i++) {
                Node layer = nodeLayers.item(i);

                // Read attributes about the layer
                NamedNodeMap attrs = layer.getAttributes();
                String name = attrs.getNamedItem("name").getTextContent();
                int id = Integer.parseInt(attrs.getNamedItem("id").getTextContent());
                int width = Integer.parseInt(attrs.getNamedItem("width").getTextContent());
                int height = Integer.parseInt(attrs.getNamedItem("height").getTextContent());
                // Hacky - assumes that the data node is the only content
                String[] strData = layer.getTextContent().split(",");
                LinkedList<Integer> data = new LinkedList<Integer>();
                for (String j : strData) {
                    data.add(Integer.parseInt(j.trim()));
                }
                // Create a new layer and add it to our collection
                layers.add(new Layer(id, name, width, height, data));
            }

            // Read the tilesets
            NodeList nodeTilesets = document.getElementsByTagName("tileset");
            for (int i = 0; i < nodeTilesets.getLength(); i++) {
                Node tileset = nodeTilesets.item(i);
                NamedNodeMap attrs = tileset.getAttributes();
                int firstgid = Integer.parseInt(attrs.getNamedItem("firstgid").getTextContent());
                String source = attrs.getNamedItem("source").getTextContent();

                tilesets.add(new Tileset(firstgid, src.replace(file.getName(), source)));
            }
            Collections.sort(tilesets); // Important, sorts in desc order

            // Read the objectgroup
            NodeList nodeGroupObjects = document.getElementsByTagName("objectgroup");
            for (int i = 0; i < nodeGroupObjects.getLength(); i++) {
                Node objectgroup = nodeGroupObjects.item(i);
                // Read attributes about the objectgroup
                NamedNodeMap attrs = objectgroup.getAttributes();
                String name = attrs.getNamedItem("name").getTextContent();
                int id = Integer.parseInt(attrs.getNamedItem("id").getTextContent());
                int visible = Integer.parseInt(attrs.getNamedItem("visible").getTextContent());

                NodeList nodeObjects = objectgroup.getChildNodes();
                List<MapObject> mapobjects = new LinkedList<MapObject>();
                for (int j = 0; j < nodeObjects.getLength(); j++) {
                    Node object = nodeObjects.item(j);
                    // Important to filter out empty text-nodes (or other childnodes that aren't objects)
                    if (!object.getNodeName().equals("object")) { continue; }
                    // Read attributes about the objects in objectgroups
                    NamedNodeMap attrs_o = object.getAttributes();
                    int id_o = Integer.parseInt(attrs_o.getNamedItem("id").getTextContent());
                    float offsetx = Float.parseFloat(attrs_o.getNamedItem("x").getTextContent());
                    float offsety = Float.parseFloat(attrs_o.getNamedItem("y").getTextContent());

                    NodeList objectChildren = object.getChildNodes();
                    Node objectPolygon = null;
                    // Just interate through until you find a child with name "polygon"
                    for (int k = 0; k < objectChildren.getLength() && objectPolygon == null; k++) {
                        Node objChild = objectChildren.item(k);
                        if (!objChild.getNodeName().equals("polygon")) { continue; }
                        objectPolygon = objectChildren.item(k);
                    }
                    if (objectPolygon == null) { continue; } // No valid children
                    NamedNodeMap attrs_p = objectPolygon.getAttributes();
                    String[] points = attrs_p.getNamedItem("points").getTextContent().split(" ");

                    // Form a square out of the max and min coordinates 
                    float x1 = 0; float y1 = 0; float x2 = 0; float y2 = 0;
                    for (int z = 0; z < points.length; z++) {
                        String[] coord = points[z].split(",");
                        float x = Float.parseFloat(coord[0]);
                        float y = Float.parseFloat(coord[1]);
                        
                        // Set min
                        if (x < x1) { x1 = x; }
                        if (y < y1) { y1 = y; }

                        // Set max
                        if (x > x2) { x2 = x; }
                        if (y > y2) { y2 = y; }
                    }

                    // Because we only really draw square images, we can just use an image ref instead of a polygon
                    ImageRef startingBounds = new ImageRef(
                            name + id_o, // Object-Layer / Layer name
                            Math.round(x1+offsetx), 
                            Math.round(y1+offsety), 
                            Math.round(x2+offsetx), 
                            Math.round(y2+offsety)
                    );

                    // This is a normalized version of above because when we draw the virtual image, that's what we'll have
                    ImageRef imageRef = new ImageRef(
                            name + id_o, // Object-Layer / Layer name
                            0,
                            0,
                            Math.round(startingBounds.x2 - startingBounds.x1), 
                            Math.round(startingBounds.y2 - startingBounds.y1)
                    );

                    mapobjects.add(new MapObject(id_o, offsetx, offsety, startingBounds, imageRef));
                }
                objectgroups.add(new ObjectGroup(id, name, visible, mapobjects));
            }


            // Setup animation map
            for (Tileset t : tilesets) {
                if (t.animations == null) { continue; }
                for (TileAnimation ta : t.animations) {
                    if (ta.totalDuration <= 0) { continue; }
                    animationFrames.put(ta.tileId, ta);
                }
            }

            // Setup animation tile map
            for (Layer l : layers) {
                for (int i = 0; i < l.data.size(); i++) {
                    int gid = filterFlipFlags(l.data.get(i));
                    // Filter out not animated
                    if (gid == 0 || !animationFrames.containsKey(gid)) { continue; }
                    List<ImageRefTo> gidTiles = animationTiles.get(gid);
                    if (gidTiles == null) {
                        gidTiles = new ArrayList<ImageRefTo>();
                    }
                    gidTiles.add(getTile(l, i, l.data.get(i)));
                    animationTiles.put(gid, gidTiles);
                }
            }

        } catch(Exception e) {
            System.out.println("Map Exception! " + e);
            e.printStackTrace();
        }
    }

    Tileset getTilesetFromId(int id) {
        if (id == 0) { return null; }
        // Tilesets must be sorted by firstgid in desc order
        for (Tileset t : tilesets) {
            if (t.firstgid <= id) { return t; }
        }
        System.out.println("Tileset not found for gid: " + id);
        for (Tileset t : tilesets) {
            System.out.println("Tileset " + t.name + ", firstgid " + t.firstgid);
        }
        return null;
    }

    public List<String> getImageSources() {
        ArrayList<String> sources = new ArrayList<String>();
        for (Tileset t : tilesets) {
            sources.add(t.imageSource);
        }
        return sources;
    }

    public List<VirtualImage> getVirtualImages() {
        List<VirtualImage> vImages = new ArrayList<VirtualImage>();
        HashMap<String, List<ImageRefTo>> imageTiles = getTilesByLayer();
        List<ImageRefTo> nonBackgroundTiles = getNonBackgroundTiles();
        for (ObjectGroup og : objectgroups) {
            List<ImageRefTo> layer = imageTiles.get(og.name);
            if (layer == null) { 
                System.out.println("Warning: object-layer reference to tile-layer that doesn't exist: " + og.name);
                // If there's no matching layer, just use the "non background tiles"
                layer = nonBackgroundTiles;
            }

            for (MapObject mo : og.objects) {
                if (mo.startingBounds == null) { continue; }
                vImages.add(VirtualImage.createFrom(mo.startingBounds, layer));
            }
        }
        return vImages;
    }

    // These should be things that wont move
    final List<String> backgroundLayerNames = List.of("Bottom", "Trees", "Furnitures", "Water", "Lava", "Rocks", "Houses");

    public List<ImageRefTo> getNonBackgroundTiles() {
        ArrayList<ImageRefTo> tiles = new ArrayList<ImageRefTo>();
        java.util.Map<String, List<ImageRefTo>> tilesByLayer = getTilesByLayer();
        for (Layer l : layers) {
            if (backgroundLayerNames.contains(l.name)) { continue; }
            tiles.addAll(tilesByLayer.get(l.name));
        }
        return tiles;
    }

    public List<ImageRefTo> getBackgroundTiles() {
        ArrayList<ImageRefTo> tiles = new ArrayList<ImageRefTo>();
        java.util.Map<String, List<ImageRefTo>> tilesByLayer = getTilesByLayer();
        for (Layer l : layers) {
            if (!backgroundLayerNames.contains(l.name)) { continue; }
            tiles.addAll(tilesByLayer.get(l.name));
        }
        return tiles;
    }

    public HashMap<String, List<ImageRefTo>> getTilesByLayer() {
        HashMap<String, List<ImageRefTo>> tiles = new HashMap<String, List<ImageRefTo>>();
        for (Layer l : layers) {
            List<ImageRefTo> tileList = new ArrayList<ImageRefTo>();
            for (int i = 0; i < l.data.size(); i++) {
                ImageRefTo imRef = getTile(l, i, l.data.get(i));
                if (imRef == null) { continue; }
                tileList.add(imRef);
            }
            tiles.put(l.name, tileList);
        }
        return tiles;
    }

    public List<ImageRefTo> getAnimationTiles(long tick) {
        ArrayList<ImageRefTo> animTiles = new ArrayList<ImageRefTo>();

        for (java.util.Map.Entry<Integer, List<ImageRefTo>> gidImageRef : animationTiles.entrySet()) {
            int gid = gidImageRef.getKey();
            TileAnimation ta = animationFrames.get(gidImageRef.getKey());
            Iterator<TileAnimationFrame> iter = ta.frames.iterator();
            TileAnimationFrame currentFrame = null;
            long relTime = tick % ta.totalDuration;
            while(relTime >= 0) {
                currentFrame = iter.next();
                relTime -= currentFrame.duration;
            }

            Tileset t = getTilesetFromId(gid);
            ImageRef ir = t.getImageRef(currentFrame.tileId);
            for (ImageRefTo imageRefTo : gidImageRef.getValue()) {
                imageRefTo.x1 = ir.x1;
                imageRefTo.x2 = ir.x2;
                imageRefTo.y1 = ir.y1;
                imageRefTo.y2 = ir.y2;

                animTiles.add(imageRefTo);
            }
        }
        return animTiles;
    }

    public int filterFlipFlags(int val) {
        return val & ~(FLIPPED_HORIZONTALLY_FLAG | FLIPPED_VERTICALLY_FLAG | FLIPPED_DIAGONALLY_FLAG);
    }

    public ImageRefTo getTile(Layer l, int i, int val) {
        if (val <= 0) { return null; }

        boolean horizontally = (val & FLIPPED_HORIZONTALLY_FLAG) == 0;
        boolean vertically = (val & FLIPPED_VERTICALLY_FLAG) == 0;
        boolean diagonally = (val & FLIPPED_DIAGONALLY_FLAG) == 0;
        // Bitwise operators to remove the flags
        val = filterFlipFlags(val);

        Tileset t = getTilesetFromId(val);
        ImageRef ir = t.getImageRef(val);

        int toX = (i % l.height) * t.tilewidth;
        int toY = (int)Math.floor(i / l.height)* t.tileheight;

        return new ImageRefTo(ir, toX, toY, toX+t.tilewidth, toY+t.tileheight, horizontally, vertically, diagonally);
    }

    public String toString() {
        return "Map {"
        + "\n\t width=" + width
        + "\n\t height=" + height
        + "\n\t layers=" + layers.toString()
        + "\n\t tilesets=" + tilesets.toString()
        + "\n\t objectgroups=" + objectgroups.toString()
        + "\n}";
    }

    public int getWidth() {
        return pixelWidth;
    }
    public int getHeight() {
        return pixelHeight;
    }

    public int getPartWidth() {
        return Math.min(pixelWidth, screenInfo.getWidth());
    }
    public int getPartHeight() {
        return Math.min(pixelHeight, screenInfo.getHeight());
    }

    public int getScrollBounds() {
        return (int)(Math.min(getPartWidth(), getPartHeight())*2/5);
    }

    public MapObject getFirstObject(String groupName) {
        List<MapObject> mapObjects = getAllObjects(groupName);
        if (mapObjects == null) { return null; }
        return mapObjects.get(0);
    }

    public List<MapObject> getAllObjects(String groupName) {
        for (ObjectGroup og : objectgroups) {
            if (!og.name.equals(groupName)) { continue; }
            return og.objects;
        }
        return null; // Not found
    }
}
