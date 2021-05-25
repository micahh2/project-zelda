package projectzelda.map;

import java.util.List;
import java.util.LinkedList;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;

public class Tileset {
    public int firstgid;
    public String name;
    public int tileheight;
    public int spacing;
    public int margin;
    public int tilecount;
    public int column;

    public String imageSource;

    public Tileset(int firstgid, String source) {
        this.firstgid = firstgid;
        try {
            // Read file
            File file = new File(source);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            // Read the first element
            Node tileset = document.getElementsByTagName("tileset").item(0);

            // Get tileset attributes
            NamedNodeMap attrs = tileset.getAttributes();
            name = attrs.getNamedItem("name").getTextContent();
            tileheight = Integer.parseInt(attrs.getNamedItem("tileheight").getTextContent());
            spacing = Integer.parseInt(attrs.getNamedItem("spacing").getTextContent());
            tilecount = Integer.parseInt(attrs.getNamedItem("tilecount").getTextContent());
            margin = Integer.parseInt(attrs.getNamedItem("margin").getTextContent());
            column = Integer.parseInt(attrs.getNamedItem("columns").getTextContent());

            // Get image attributes
            Node image = document.getElementsByTagName("image").item(0);
            NamedNodeMap imageAttrs = image.getAttributes();
            imageSource = imageAttrs.getNamedItem("source").getTextContent();

        } catch(Exception e) {
            System.out.println("Tileset Exception! " + e.getMessage());
            e.printStackTrace();
        }
    }
    public String toString() {
        return "Tileset { "
        + "firstgid=" + firstgid
        + "; name=" + name
        + "; tileheight=" + tileheight
        + "; spacing" + spacing
        + "; margin=" + margin
        + "; tilecount=" + tilecount
        + "; column=" + column
        + "; }";
    }
}
