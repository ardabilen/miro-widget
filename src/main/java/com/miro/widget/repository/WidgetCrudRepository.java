package com.miro.widget.repository;

import com.miro.widget.model.Widget;

import java.util.List;

/**
 * The interface Widget crud repository.
 */
public interface WidgetCrudRepository {

    /**
     * Create widget.
     *
     * @param widget the widget
     * @return the widget
     */
    Widget create(Widget widget);

    /**
     * Gets by id.
     *
     * @param id the id
     * @return the by id
     */
    Widget getById(Long id);

    /**
     * Gets widgets.
     *
     * @return the widgets
     */
    List<Widget> getWidgets();

    /**
     * Delete.
     *
     * @param id the id
     */
    void delete(Long id);

    /**
     * Update.
     *
     * @param widget the widget
     */
    void update(Widget widget);

    /**
     * Filter widgets list.
     *
     * @param x1 the x 1
     * @param x2 the x 2
     * @param y1 the y 1
     * @param y2 the y 2
     * @return the list
     */
    List<Widget> filterWidgets(Double x1, Double x2, Double y1, Double y2);
}
