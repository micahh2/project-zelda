package projectzelda.map;

import java.util.List;
import java.util.LinkedList;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;

public class Map {
    public List<Layer> layers = new LinkedList<Layer>();
    public List<Tileset> tilesets = new LinkedList<Tileset>();

    public Map(String src) {
        try {
            File file = new File(src);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

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
            // java.lang.NullPointerException: 
            // Cannot invoke "org.w3c.dom.Node.getTextContent()" 
            // because the return value of "org.w3c.dom.NamedNodeMap.getNamedItem(String)" is null

            // Read the tilesets
            NodeList nodeTilesets = document.getElementsByTagName("tileset");
            for (int i = 0; i < nodeTilesets.getLength(); i++) {
                Node tileset = nodeTilesets.item(i);
                NamedNodeMap attrs = tileset.getAttributes();
                int firstgid = Integer.parseInt(attrs.getNamedItem("firstgid").getTextContent());
                String source = attrs.getNamedItem("source").getTextContent();

                tilesets.add(new Tileset(firstgid, source));
            }

        } catch(Exception e) {
            System.out.println("Map Exception! " + e);
            e.printStackTrace();
        }
    }

    public String toString() {
        return "Map {"
        + "\n\t layers=" + layers.toString()
        + "\n\t tilesets=" + tilesets.toString()
        + "\n}";
    }
}
