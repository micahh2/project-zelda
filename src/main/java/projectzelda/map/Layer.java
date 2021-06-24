package projectzelda.map;

import java.util.List;
import java.util.LinkedList;

public class Layer {
    public int id;
    public String name;
    public int width;
    public int height;
    public List<Long> data;

    public Layer(int id, String name, int width, int height, List<Long> data) {
        this.id = id;
        this.name = name;
        this.width = width;
        this.height = height;
        this.data = data;
    }
    public String toString() {
        return "Layer { "
        + "id=" + id
        + "; name=" + name
        + "; width=" + width
        + "; height=" + height
        + "; data=[length: " + data.size() + "]"
        + "; }";
    }
}
