package com.miro.widget.model;

import java.awt.geom.Point2D;
import java.sql.Timestamp;

/**
 * The type Widget.
 */
public class Widget {

    private Long id;
    private Point2D.Double coordinates;
    private Integer zIndex;
    private Double width;
    private Double height;
    private Timestamp timestamp;

    /**
     * Instantiates a new Widget.
     */
    public Widget() {
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets coordinates.
     *
     * @return the coordinates
     */
    public Point2D.Double getCoordinates() {
        return coordinates;
    }

    /**
     * Sets coordinates.
     *
     * @param coordinates the coordinates
     */
    public void setCoordinates(Point2D.Double coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Gets index.
     *
     * @return the index
     */
    public Integer getzIndex() {
        return zIndex;
    }

    /**
     * Sets index.
     *
     * @param zIndex the z index
     */
    public void setzIndex(Integer zIndex) {
        this.zIndex = zIndex;
    }

    /**
     * Gets width.
     *
     * @return the width
     */
    public Double getWidth() {
        return width;
    }

    /**
     * Sets width.
     *
     * @param width the width
     */
    public void setWidth(Double width) {
        this.width = width;
    }

    /**
     * Gets height.
     *
     * @return the height
     */
    public Double getHeight() {
        return height;
    }

    /**
     * Sets height.
     *
     * @param height the height
     */
    public void setHeight(Double height) {
        this.height = height;
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Sets timestamp.
     *
     * @param timestamp the timestamp
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Increase z index.
     */
    public void increaseZIndex() {
        this.zIndex++;
    }
}
