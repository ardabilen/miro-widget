package com.miro.widget.services;

import com.miro.widget.model.Widget;
import com.miro.widget.exception.WidgetNotFoundException;
import com.miro.widget.repository.WidgetCrudRepository;

import com.miro.widget.utils.NullAwareBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class WidgetService {

    private WidgetCrudRepository widgetRepository;

    @Autowired
    public WidgetService(WidgetCrudRepository widgetCrudRepository) {
        this.widgetRepository = widgetCrudRepository;
    }

    public Widget createWidget(Widget widget) {
        return widgetRepository.create(widget);
    }

    public Optional<Widget> getWidgetById(Long id) {
        return Optional.ofNullable(widgetRepository.getById(id));
    }

    public void deleteWidget(Long id) {
        widgetRepository.delete(id);
    }

    public Page<Widget> getWidgets(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Widget> widgets = widgetRepository.getWidgets();
        List<Widget> result;
        if (widgets.size() < startItem) {
            result = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, widgets.size());
            result = widgets.subList(startItem, toIndex);
        }

        Page<Widget> widgetPage = new PageImpl(result, PageRequest.of(currentPage, pageSize), widgets.size());

        return widgetPage;
    }

    public Widget updateWidget(Long id, Widget widget) throws WidgetNotFoundException {
        Widget widgetToUpdated = widgetRepository.getById(id);
        if(widgetToUpdated == null)
            throw new WidgetNotFoundException();

        // do not allow to change Id
        widget.setId(null);

        //first remove it from repository, then update non null properties and add it to the repository again with same id.
        widgetRepository.delete(id);
        NullAwareBeanUtils.copyProperties(widget, widgetToUpdated);
        widgetRepository.update(widgetToUpdated);

        return widgetToUpdated;
    }
}
