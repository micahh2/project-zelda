package projectzelda.map;

import java.util.List;
import java.util.LinkedList;
import java.net.URI;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import projectzelda.engine.ImageRef;

public class Tileset implements Comparable<Tileset> {
    public int firstgid;
    public String name;
    public int tileheight;
    public int tilewidth;
    public int spacing;
    public int margin;
    public int tilecount;
    public int columns;
    public List<TileAnimation> animations;

    public String imageSource;

    public Tileset(int firstgid, String source) {
        this.firstgid = firstgid;
        try {
            // Read file
            URI resourceUri = getClass().getResource(source).toURI();
            File file = new File(resourceUri);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            // Read the first element
            Node tileset = document.getElementsByTagName("tileset").item(0);

            // Get tileset attributes
            NamedNodeMap attrs = tileset.getAttributes();
            name = attrs.getNamedItem("name").getTextContent();
            tileheight = Integer.parseInt(attrs.getNamedItem("tileheight").getTextContent());
            tilewidth = Integer.parseInt(attrs.getNamedItem("tilewidth").getTextContent());
            spacing = Integer.parseInt(attrs.getNamedItem("spacing").getTextContent());
            tilecount = Integer.parseInt(attrs.getNamedItem("tilecount").getTextContent());
            margin = Integer.parseInt(attrs.getNamedItem("margin").getTextContent());
            columns = Integer.parseInt(attrs.getNamedItem("columns").getTextContent());

            // Get image attributes
            Node image = document.getElementsByTagName("image").item(0);
            NamedNodeMap imageAttrs = image.getAttributes();
            String xmlImageSource = imageAttrs.getNamedItem("source").getTextContent();
            imageSource = source.replace(file.getName(), xmlImageSource);

            // Get animations
            animations = new LinkedList<TileAnimation>();
            NodeList animationNodes = document.getElementsByTagName("animation");
            for (int i = 0; i < animationNodes.getLength(); i++) {
                Node animationNode = animationNodes.item(i);

                // Get tile id
                Node tileNode = animationNode.getParentNode();
                NamedNodeMap tileAttrs = tileNode.getAttributes();
                int tileId = Integer.parseInt(tileAttrs.getNamedItem("id").getTextContent());
                int totalDuration = 0;

                // Get frames
                NodeList childNodes = animationNode.getChildNodes();
                List<TileAnimationFrame> frames = new LinkedList<TileAnimationFrame>();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node frameNode = childNodes.item(j);
                    if (!frameNode.getNodeName().equals("frame")) { continue; }
                    NamedNodeMap frameAttrs = frameNode.getAttributes();
                    int frameTileId = Integer.parseInt(frameAttrs.getNamedItem("tileid").getTextContent());
                    int duration = Integer.parseInt(frameAttrs.getNamedItem("duration").getTextContent());
                    // Add firstgid to make it a global id for easier later processing
                    frames.add(new TileAnimationFrame(frameTileId + firstgid, duration));
                    totalDuration += duration;
                }
                // Add firstgid to make it a global id for easier later processing
                animations.add(new TileAnimation(tileId + firstgid, frames, totalDuration));
            }

        } catch(Exception e) {
            System.out.println("Tileset Exception! " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ImageRef getImageRef(int gid) {
        if (gid < firstgid || gid > (firstgid+tilecount-1)) { 
            System.out.println("Error");
            return null;
        }
        int index = gid-firstgid;
        int x = (index % columns);
        int y = (index / columns);
        int xpixel = x*(tilewidth+spacing)+margin;
        int ypixel = y*(tileheight+spacing)+margin;
        return new ImageRef(imageSource, xpixel, ypixel, xpixel+tilewidth, ypixel+tileheight);
    }

    @Override
    public String toString() {
        return "Tileset { "
        + "firstgid=" + firstgid
        + "; name=" + name
        + "; tilewidth=" + tilewidth
        + "; tileheight=" + tileheight
        + "; spacing" + spacing
        + "; margin=" + margin
        + "; tilecount=" + tilecount
        + "; columns=" + columns
        + "; animations=" + animations.toString()
        + "; }";
    }

    @Override
    public int compareTo(Tileset other) {
        return other.firstgid - firstgid;
    }

}
