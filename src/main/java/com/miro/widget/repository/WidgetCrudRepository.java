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
}
