package projectzelda.map;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;
import java.net.URL;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import projectzelda.engine.ImageRef;
import projectzelda.engine.ImageRefTo;
import projectzelda.engine.WorldInfo;
import projectzelda.gfx.MediaInfo;
import projectzelda.Const;

public class Map implements MediaInfo, WorldInfo {
    public List<Layer> layers = new LinkedList<Layer>();
    public List<Tileset> tilesets = new LinkedList<Tileset>();

    public int width;
    public int height;

    public int tileWidth;
    public int tileHeight;

    public int pixelWidth;
    public int pixelHeight;

    public static final int FLIPPED_HORIZONTALLY_FLAG = 0x80000000;
    public static final int FLIPPED_VERTICALLY_FLAG = 0x40000000;
    public static final int FLIPPED_DIAGONALLY_FLAG = 0x20000000;

    public static String mapsDir;

    public Map(String src) {
        try {

            URL resourceUri = getClass().getResource(src);
            File file = new File(resourceUri.getPath());
            mapsDir = file.getParent();

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
                    if (j.trim() == "") { continue; }
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

                tilesets.add(new Tileset(firstgid, mapsDir + "/" + source));
            }
            Collections.sort(tilesets); // Important, sorts in desc order

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
        int tilesize = 16;
        for (Layer l : layers) {
            //if (!l.name.equals("Lava")) { continue; }
            //System.out.println("Layer: " + l.name);
            for (int i = 0; i < l.data.size(); i++) {
                int val = l.data.get(i);
                if (val <= 0) { continue; }

                boolean horizontally = (val & FLIPPED_HORIZONTALLY_FLAG) == 0;
                boolean vertically = (val & FLIPPED_VERTICALLY_FLAG) == 0;
                boolean diagonally = (val & FLIPPED_DIAGONALLY_FLAG) == 0;
                boolean hasFlip = horizontally || vertically || diagonally;
                // Bitwise operators to remove the flags
                val &= ~(FLIPPED_HORIZONTALLY_FLAG | FLIPPED_VERTICALLY_FLAG | FLIPPED_DIAGONALLY_FLAG);

                Tileset t = getTilesetFromId(val);
                ImageRef ir = getImageRef(val);

                int toX = (i % l.height) * t.tilewidth;
                int toY = (int)Math.floor(i / l.height)* t.tileheight;
                //System.out.println("Val: " + val + ", x:y " + ir.x1 + ":" + ir.y1 + ", image: " + ir.name);

                tiles.add(new ImageRefTo(ir, toX, toY, toX+t.tilewidth, toY+t.tileheight, horizontally, vertically, diagonally));
            }
        }
        return tiles;
    }

    public ImageRef getImageRef(int gid) {
        Tileset t = getTilesetFromId(gid);
        return t.getImageRef(gid);
    }

    public String toString() {
        return "Map {"
        + "\n\t width=" + width
        + "\n\t height=" + height
        + "\n\t layers=" + layers.toString()
        + "\n\t tilesets=" + tilesets.toString()
        + "\n}";
    }

    public int getWidth() {
        return pixelWidth;
    }
    public int getHeight() {
        return pixelHeight;
    }

    public int getPartWidth() {
        return Math.min(pixelWidth, Const.WORLDPART_WIDTH);
    }
    public int getPartHeight() {
        return Math.min(pixelHeight, Const.WORLDPART_HEIGHT);
    }

    public int getScrollBounds() {
        return Const.SCROLL_BOUNDS;
    }
}
