package projectzelda;

import projectzelda.engine.*;
import projectzelda.game.*;
import projectzelda.gfx.*;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

// (c) Thorsten Hasbargen

final class Main 
{
    private World world = null;

    public Main()
    { 
        Frame frame = new SwingFrame();
        frame.displayOnScreen();

        world = new RPGWorld();

        world.setGraphicSystem(frame.getGraphicSystem());
        world.setInputSystem(frame.getInputSystem());

        GameObject.setWorld(world);
        TextObject.setWorld(world);
        frame.getGraphicSystem().setWorld(world);

        world.init();
        world.run();
    }

    public static void main(String[] args) {
        try {
            File file = new File("world_map.tmx");
            DocumentBuilderFactory documentBuilderFactory = 
                DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            NodeList layers = document.getElementsByTagName("layer");
            for (int i = 0; i < layers.getLength(); i++) {
                Node layer = layers.item(i);
                System.out.println(layer + layer.getTextContent());
            }
            //String pwd = document.getElementsByTagName("password").item(0).getTextContent();
            new Main();
        } catch(Exception e) {
            System.out.println("Exception! " + e.getMessage());
        }
    }
}
