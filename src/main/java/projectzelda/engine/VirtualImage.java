
package projectzelda.engine;

import java.util.List;
import java.util.ArrayList;

public class VirtualImage {

    public String name;
    public int width;
    public int height;
    public List<ImageRefTo> sources;

    public VirtualImage(String name, int width, int height, List<ImageRefTo> sources) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.sources = sources;
    }

    public static VirtualImage createFrom(ImageRef bounds, List<ImageRefTo> sources) {
        // Convert ImageRefTo's to be relative
        List<ImageRefTo> newSources = new ArrayList<ImageRefTo>(sources.size());
        for (ImageRefTo source : sources) {
            // Make destination coordinates relative to image
            int destx1 = bounds.x1; 
            int desty1 = bounds.y1;
            int destx2 = bounds.x1;
            int desty2 = bounds.y1;

            int firstx2 = bounds.x2;
            int lastx1 = destx1;
            if (bounds.x1 > destx1) {
                firstx2 = destx2;
                lastx1 = bounds.x1;
            }
            int firsty2 = bounds.y2;
            int lasty1 = desty1;
            if (bounds.y1 > desty1) {
                firsty2 = desty2;
                lasty1 = bounds.y1;
            }

            // Ignore tiles that don't overlap with the bounds
            if (firstx2 < lastx1) { continue; }
            if (firsty2 < lasty1) { continue; }

            ImageRefTo newSource = new ImageRefTo(
                    source, 
                    source.destx1-bounds.x1, 
                    source.desty1-bounds.y1,
                    source.destx2-bounds.x1, 
                    source.desty2-bounds.y1,
                    source.horizontallyFlipped,
                    source.verticallyFlipped,
                    source.diagonallyFlipped
            );
            newSources.add(newSource);
        }

        int width = bounds.x2 - bounds.x1;
        int height = bounds.y2 - bounds.y1;
        return new VirtualImage(bounds.name, width, height, newSources);
    }
}
