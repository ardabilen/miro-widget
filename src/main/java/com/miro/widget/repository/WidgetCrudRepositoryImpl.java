package com.miro.widget.repository;

import com.miro.widget.model.Widget;
import com.miro.widget.utils.WidgetLinkedHashMap;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WidgetCrudRepositoryImpl implements WidgetCrudRepository {

    private WidgetLinkedHashMap widgetLinkedHashMap;
    private Long id;

    public WidgetCrudRepositoryImpl() {
        widgetLinkedHashMap = new WidgetLinkedHashMap();
        id = 0L;
    }

    @Override
    public Widget create(Widget widget) {
        widget.setId(id);
        widgetLinkedHashMap.put(id++, widget);
        return widget;
    }

    @Override
    public void update(Widget widget) {
        widgetLinkedHashMap.put(widget.getId(), widget);
    }

    @Override
    public Widget getById(Long id){
        return widgetLinkedHashMap.get(id);
    }

    @Override
    public List<Widget> getWidgets() {
        return widgetLinkedHashMap.getWidgets();
    }

    @Override
    public void delete(Long id) {
        widgetLinkedHashMap.delete(id);
    }

}
