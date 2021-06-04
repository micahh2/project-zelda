package projectzelda.map;

import java.util.List;

public class ObjectGroup {
    public int id;
    public String name;
    public int visible;
    public List<MapObject> objects;

    public ObjectGroup(int id, String name, int visible, List<MapObject> objects) {
        this.id = id;
        this.name = name;
        this.visible = visible;
        this.objects = objects;
    }

    @Override
    public String toString() {
        return "ObjectGroup { " +
                "id=" + id +
                "; name='" + name + '\'' +
                "; visible=" + visible +
                "; objects=" + objects +
                "; }";
    }
}
