package edu.swe2.cs.model;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class Iptc {

    private int id = -1;
    private String title;
    private String caption;
    private String city;
    private Set<String> tags = new LinkedHashSet<>();

    /**
     * Constructs a new Iptc
     *
     */
    public Iptc() {
    }

    /**
     * Constructs a new Iptc with the specified id
     *
     */
    public Iptc(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getCaption() {
        return this.caption;
    }

    public String getCity() {
        return this.city;
    }

    public void setID(int ID) {
        this.id = ID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Adds the specified tag to the tags of this iptc instance
     *
     * @param tagName The tag to be added
     */
    public void addTag(String tagName) {
        this.tags.add(tagName);
    }

    /**
     * Adds the specified tags to the tags of this iptc instance
     *
     * @param tags The tags to be added
     */
    public void addTags(String tags) {
        this.tags.addAll(Arrays.asList(tags.split(",")));
    }

    public Set<String> getAllTags() {
        return this.tags;
    }

    /**
     * Returns all tags as string separated by a ","
     *
     * @return All tags with a "," as separator
     */
    public String getTags() {
        return String.join(",", this.tags);
    }
}
