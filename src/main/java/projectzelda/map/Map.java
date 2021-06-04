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
import projectzelda.engine.ImageRef;
import projectzelda.engine.ImageRefTo;
import projectzelda.engine.WorldInfo;
import projectzelda.engine.ScreenInfo;
import projectzelda.gfx.MediaInfo;
import projectzelda.Const;

public class Map implements MediaInfo, WorldInfo {
    public List<Layer> layers = new LinkedList<Layer>();
    public List<Tileset> tilesets = new LinkedList<Tileset>();
    public List<ObjectGroup> objectgroups = new LinkedList<ObjectGroup>();
    public List<MapObject> mapobjects = new LinkedList<MapObject>();
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

                NodeList nodeObjects = document.getElementsByTagName("object");
                for (int j = 0; j < nodeObjects.getLength(); j++) {

                    Node object = nodeObjects.item(j);
                    // Read attributes about the objects in objectgroups
                    NamedNodeMap attrs_o = object.getAttributes();
                    int id_o = Integer.parseInt(attrs_o.getNamedItem("id").getTextContent());
                    float x = Float.parseFloat(attrs_o.getNamedItem("x").getTextContent());
                    float y = Float.parseFloat(attrs_o.getNamedItem("y").getTextContent());

                    /*NodeList nodePolygon = document.getElementsByTagName("polygon");
                    for (int z = 0; z < nodePolygon.getLength(); z++) {
                        Node pol = nodePolygon.item(z);
                        NamedNodeMap attrs_p = pol.getAttributes();
                        String points = attrs_p.getNamedItem("points").getTextContent();
                        polygon = new Polygon(points);

                    }*/
                    if(id == 12){
                        if(id_o == 3){
                            mapobjects.add(new MapObject(id_o, x, y, polygon));
                        }
                    }
                    else if(id == 6){
                        if(id_o == 1){
                            mapobjects.add(new MapObject(id_o, x, y, polygon));
                        }
                    }
                    else if(id == 7){
                        if(id_o == 2){
                            mapobjects.add(new MapObject(id_o, x, y, polygon));
                        }
                    }
                    else if(id == 17){
                        if(id_o == 13 || id_o == 14 || id_o == 15 || id_o == 16 || id_o == 17 || id_o == 18){
                            mapobjects.add(new MapObject(id_o, x, y, polygon));
                        }
                    }
                    else if(id == 16){
                        if(id_o == 6 || id_o == 7 || id_o == 8 || id_o == 9 || id_o == 10 || id_o == 12){
                            mapobjects.add(new MapObject(id_o, x, y, polygon));
                        }
                    }
                    else if(id == 15){
                        if(id_o == 5){
                            mapobjects.add(new MapObject(id_o, x, y, polygon));
                        }
                    }
                    else if(id == 14){
                        if(id_o == 4){
                            mapobjects.add(new MapObject(id_o, x, y, polygon));
                        }
                    }
                }
                objectgroups.add(new ObjectGroup(id, name, visible, mapobjects));
                mapobjects = new LinkedList<MapObject>();
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
                // if (!l.name.equals("Lava")) { continue; }
                // System.out.println("Layer: " + l.name);
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

    public List<ImageRefTo> getBackgroundTiles() {
        ArrayList<ImageRefTo> tiles = new ArrayList<ImageRefTo>();
        for (Layer l : layers) {
            //if (!l.name.equals("Lava")) { continue; }
            //System.out.println("Layer: " + l.name);
            for (int i = 0; i < l.data.size(); i++) {
                ImageRefTo imRef = getTile(l, i, l.data.get(i));
                if (imRef == null) { continue; }
                tiles.add(imRef);
            }
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
}
