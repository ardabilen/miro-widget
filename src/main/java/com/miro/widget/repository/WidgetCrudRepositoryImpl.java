package com.miro.widget.repository;

import com.miro.widget.model.Widget;
import com.miro.widget.utils.TwoDimensionalTree;
import com.miro.widget.utils.WidgetLinkedHashMap;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WidgetCrudRepositoryImpl implements WidgetCrudRepository {

    private WidgetLinkedHashMap widgetLinkedHashMap;
    private TwoDimensionalTree tree;
    private Long id;

    public WidgetCrudRepositoryImpl() {
        widgetLinkedHashMap = new WidgetLinkedHashMap();
        tree = new TwoDimensionalTree();
        id = 0L;
    }

    @Override
    public Widget create(Widget widget) {
        widget.setId(id);
        widgetLinkedHashMap.put(id++, widget);
        tree.insert(widget);
        return widget;
    }

    @Override
    public void update(Widget widget) {
        widgetLinkedHashMap.put(widget.getId(), widget);
        tree.insert(widget);
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
    public List<Widget> filterWidgets(Double x1, Double x2, Double y1, Double y2) {
        return tree.rangeSearch(x1, x2, y1, y2);
    }

    @Override
    public void delete(Long id) {
        Widget widgetToBeDeleted =  widgetLinkedHashMap.get(id);
        tree.deleteNode(widgetToBeDeleted);
        widgetLinkedHashMap.delete(id);
    }

}
