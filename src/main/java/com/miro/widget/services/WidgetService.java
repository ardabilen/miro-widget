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

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class WidgetService {

    private WidgetCrudRepository widgetRepository;

    /**
     * Instantiates a new Widget service.
     *
     * @param widgetCrudRepository the widget crud repository
     */
    @Autowired
    public WidgetService(WidgetCrudRepository widgetCrudRepository) {
        this.widgetRepository = widgetCrudRepository;
    }

    /**
     * Create widget widget.
     *
     * @param widget the widget
     * @return the widget
     */
    public Widget createWidget(Widget widget) {
        widget.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return widgetRepository.create(widget);
    }

    /**
     * Gets widget by id.
     *
     * @param id the id
     * @return the widget by id
     */
    public Optional<Widget> getWidgetById(Long id) {
        return Optional.ofNullable(widgetRepository.getById(id));
    }

    /**
     * Delete widget.
     *
     * @param id the id
     */
    public void deleteWidget(Long id) {
        widgetRepository.delete(id);
    }

    /**
     * Gets widgets.
     *
     * @param pageable the pageable
     * @return the widgets
     */
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

    /**
     * Update widget widget.
     *
     * @param id     the id
     * @param widget the widget
     * @return the widget
     * @throws WidgetNotFoundException the widget not found exception
     */
    public Widget updateWidget(Long id, Widget widget) throws WidgetNotFoundException {
        Widget widgetToUpdated = widgetRepository.getById(id);
        if(widgetToUpdated == null)
            throw new WidgetNotFoundException();

        // do not allow to change Id
        widget.setId(null);
        // update timestamp
        widget.setTimestamp(new Timestamp(System.currentTimeMillis()));

        //first remove it from repository, then update non null properties and add it to the repository again with same id.
        widgetRepository.delete(id);
        NullAwareBeanUtils.copyProperties(widget, widgetToUpdated);
        widgetRepository.update(widgetToUpdated);

        return widgetToUpdated;
    }
}
