package projectzelda.map;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.LinkedList;
import org.w3c.dom.*;
import projectzelda.engine.*;

public class XMLDocument {

    static int getAsInt(NamedNodeMap attrs, String name) {
        Node attr = attrs.getNamedItem(name);
        return Integer.parseInt(attr.getTextContent());
    }
    static int getAsInt(NamedNodeMap attrs, String name, int defaultVal) {
        Node attr = attrs.getNamedItem(name);
        return attr == null ? defaultVal : Integer.parseInt(attr.getTextContent());
    }

    static float getAsFloat(NamedNodeMap attrs, String name) {
        Node attr = attrs.getNamedItem(name);
        return Float.parseFloat(attr.getTextContent());
    }
    static float getAsFloat(NamedNodeMap attrs, String name, float defaultVal) {
        Node attr = attrs.getNamedItem(name);
        return attr == null ? defaultVal : Float.parseFloat(attr.getTextContent());
    }

    static String getAsString(NamedNodeMap attrs, String name) {
        Node attr = attrs.getNamedItem(name);
        return attr.getTextContent();
    }
    static String getAsString(NamedNodeMap attrs, String name, String defaultVal) {
        Node attr = attrs.getNamedItem(name);
        return attr == null ? defaultVal : attr.getTextContent();
    }

    static List<ObjectGroup> getObjectGroups(NodeList nodeGroupObjects, int tilewidth, int tileheight) {
        List<ObjectGroup> objectgroups = new LinkedList<ObjectGroup>();
        for (int i = 0; i < nodeGroupObjects.getLength(); i++) {
            Node objectgroup = nodeGroupObjects.item(i);
            // Read attributes about the objectgroup
            NamedNodeMap attrs = objectgroup.getAttributes();
            String name = getAsString(attrs, "name");
            int id = getAsInt(attrs, "id");
            int visible = getAsInt(attrs, "visible", 0);

            List<Node> nodeObjects = getChildNodes(objectgroup, "object");
            List<MapObject> mapobjects = new LinkedList<MapObject>();
            for (Node object : nodeObjects) {
                // Read attributes about the objects in objectgroups
                NamedNodeMap attrs_o = object.getAttributes();
                int id_o = getAsInt(attrs_o, "id");
                float x = getAsFloat(attrs_o, "x");
                float y = getAsFloat(attrs_o, "y");
                // Mostly we use integers
                int rx = Math.round(x);
                int ry = Math.round(y);

                MapObject.Type type = MapObject.Type.POLYGON;
                int width = Math.round(getAsFloat(attrs_o, "width", 20));
                int height = Math.round(getAsFloat(attrs_o, "height", 20));
                ImageRef startingBounds = null;  
                Node polygon = getChildNode(object, "polygon");
                Node elipse = getChildNode(object, "ellipse");
                Node point = getChildNode(object, "point");
                if (polygon != null) {
                    type = MapObject.Type.POLYGON;
                    startingBounds = fromPolygonToImageRef(polygon, name + id_o, rx, ry);
                } else if (elipse != null) {
                    type = MapObject.Type.ELLIPSE;
                    startingBounds = new ImageRef(name+id_o, rx, ry, rx+width, ry+height);
                } else if (point != null) {
                    type = MapObject.Type.POINT;
                    startingBounds = null;
                } else {
                    type = MapObject.Type.POLYGON;
                    startingBounds = new ImageRef(name+id_o, rx, ry, rx+width, ry+height);
                }

                // This is a normalized version of above because when we draw the virtual image, that's what we'll have
                ImageRef imageRef = null;
                if (startingBounds != null) {
                    imageRef = new ImageRef(
                            name + id_o, // Object-Layer / Layer name
                            0,
                            0,
                            Math.round(startingBounds.x2 - startingBounds.x1), 
                            Math.round(startingBounds.y2 - startingBounds.y1)
                            );
                }

                mapobjects.add(new MapObject(id_o, x, y, type, startingBounds, imageRef));
            }
            objectgroups.add(new ObjectGroup(id, name, visible, mapobjects));
        }
        return objectgroups;
    }

    static List<Tileset> getTilesets(NodeList nodeTilesets, String fileName, String src) {
        List<Tileset> tilesets = new LinkedList<Tileset>();
        for (int i = 0; i < nodeTilesets.getLength(); i++) {
            Node tileset = nodeTilesets.item(i);
            NamedNodeMap attrs = tileset.getAttributes();
            int firstgid = Integer.parseInt(attrs.getNamedItem("firstgid").getTextContent());
            String source = attrs.getNamedItem("source").getTextContent();

            tilesets.add(new Tileset(firstgid, src.replace(fileName, source)));
        }
        Collections.sort(tilesets); // Important, sorts in desc order
        return tilesets;
    }

    static List<Layer> getLayers(NodeList nodeLayers) {
        List<Layer> layers = new LinkedList<Layer>();
        for (int i = 0; i < nodeLayers.getLength(); i++) {
            Node layer = nodeLayers.item(i);

            // Read attributes about the layer
            NamedNodeMap attrs = layer.getAttributes();
            String name = getAsString(attrs, "name");
            int id = getAsInt(attrs, "id");
            int width = getAsInt(attrs, "width");
            int height = getAsInt(attrs, "height");

            String[] strData = getChildNode(layer, "data").getTextContent().split(",");
            LinkedList<Integer> data = new LinkedList<Integer>();
            for (String j : strData) {
                data.add(Integer.parseInt(j.trim()));
            }
            // Create a new layer and add it to our collection
            layers.add(new Layer(id, name, width, height, data));
        }
        return layers;
    }

    static ImageRef fromPolygonToImageRef(Node objectPolygon, String name, int offsetx, int offsety) {
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
            return new ImageRef(name,
                    Math.round(x1+offsetx), 
                    Math.round(y1+offsety), 
                    Math.round(x2+offsetx), 
                    Math.round(y2+offsety)
                    );
    }

    static List<Node> getChildNodes(Node parent, String name) {
        NodeList children = parent.getChildNodes();
        List<Node> found = new LinkedList<Node>();
        for (int k = 0; k < children.getLength(); k++) {
            Node child = children.item(k);
            if (!child.getNodeName().equals(name)) { continue; }
            found.add(children.item(k));
        }
        return found;
    }

    static Node getChildNode(Node parent, String name) {
        Node found = null;
        NodeList children = parent.getChildNodes();
        for (int k = 0; k < children.getLength() && found == null; k++) {
            Node child = children.item(k);
            if (!child.getNodeName().equals(name)) { continue; }
            found = children.item(k);
        }
        return found;
    }
}
