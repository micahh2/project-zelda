package projectzelda.gfx;

import projectzelda.engine.*;
import java.util.List;

public interface MediaInfo {
    public List<String> getImageSources();
    public List<ImageRefTo> getBackgroundTiles(); 
    public ImageRef getImageRef(int id);
}
