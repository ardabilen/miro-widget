package com.miro.widget.repository;

import com.miro.widget.model.Widget;

import java.util.List;

public interface WidgetCrudRepository {

    Widget create(Widget widget);
    Widget getById(Long id);
    List<Widget> getWidgets();
    void delete(Long id);
    void update(Widget widget);
}
