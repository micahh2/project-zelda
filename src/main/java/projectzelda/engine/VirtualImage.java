
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

    public static VirtualImage createFrom(String name, List<ImageRefTo> sources) {
        ImageRefTo first = sources.get(0);
        int x1 = first.destx1;
        int y1 = first.desty1;
        int x2 = first.destx2;
        int y2 = first.desty2;
        // Get dimensions
        for (ImageRefTo source : sources) {
            // Set min
            if (source.destx1 < x1) { x1 = source.destx1; }
            if (source.desty1 < y1) { y1 = source.desty1; }

            // Set max
            if (source.destx2 > x2) { x2 = source.destx2; }
            if (source.desty2 > y2) { y2 = source.desty2; }
        }

        // Convert ImageRefTo's to be relative
        List<ImageRefTo> newSources = new ArrayList<ImageRefTo>(sources.size());
        for (ImageRefTo source : sources) {
            // Make destination coordinates relative to image
            ImageRefTo newSource = new ImageRefTo(
                    source, 
                    source.destx1-x1, 
                    source.desty1-y1,
                    source.destx2-x1, 
                    source.desty2-y1,
                    source.horizontallyFlipped,
                    source.verticallyFlipped,
                    source.diagonallyFlipped
            );
            newSources.add(newSource);
        }

        int width = x2 - x1;
        int height = y2 - y1;
        System.out.println("Found x1: " + x1 + ", y1: " + y1 + ", x2: " + x2 + ", y2: " + y2);
        return new VirtualImage(name, width, height, newSources);
    }
}
