package edu.swe2.cs.reporting;

import edu.swe2.cs.bl.PictureBL;
import edu.swe2.cs.model.Iptc;
import edu.swe2.cs.model.Picture;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TagReport implements IReport {

    private static final String FILE_NAME = "Tags_Report";
    private final List<Picture> pictures;

    public TagReport(List<Picture> pictures) {
        this.pictures = pictures;
    }

    /**
     * @return Map (key=tagname, value=number of tag usage) of tags data.
     * It checks for all pictures all tags and creates map structure.
     */
    public Map<String, Integer> getTagsData() {
        Map<String, Integer> tagsData = new HashMap<>();
        for (Picture picture : pictures) {
            Iptc iptc = PictureBL.getInstance().getIptcToPicture(picture);
            if (iptc != null) {
                Set<String> tags = iptc.getAllTags();
                tags.forEach((tagName) -> {
                    // check if container already in map
                    if (tagsData.containsKey(tagName)) {
                        int num = tagsData.get(tagName);
                        num += 1;
                        tagsData.put(tagName, num);
                    } else {
                        // start with 1
                        tagsData.put(tagName, 1);
                    }
                });
            }
        }
        return tagsData;
    }

    @Override
    public String getFileName() {
        return FILE_NAME;
    }

}
