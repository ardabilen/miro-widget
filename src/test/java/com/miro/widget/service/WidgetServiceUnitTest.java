package com.miro.widget.service;

import com.miro.widget.exception.WidgetNotFoundException;
import com.miro.widget.model.Widget;
import com.miro.widget.repository.WidgetCrudRepository;
import com.miro.widget.services.WidgetService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RunWith(MockitoJUnitRunner.class)
public class WidgetServiceUnitTest {

    @Mock
    private WidgetCrudRepository widgetCrudRepository;

    @InjectMocks
    private WidgetService widgetService;

    @Test
    public void createWidget_shouldCreateWidget() {
        Widget expected = new Widget();
        Mockito.doReturn(expected).when(widgetCrudRepository).create(Mockito.any());
        Widget actual = widgetService.createWidget(new Widget());
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void getWidget_shouldReturnWidget_whenWidgetExists() {
        Widget expected = new Widget();
        Mockito.doReturn(expected).when(widgetCrudRepository).getById(1L);
        Optional<Widget> actual = widgetService.getWidgetById(1L);

        Assert.assertTrue(actual.isPresent());
        Assert.assertEquals(actual.get(), expected);
    }

    @Test
    public void getWidget_shouldReturnNull_whenWidgetDoesNotExist() {
        Mockito.doReturn(null).when(widgetCrudRepository).getById(1L);
        Optional<Widget> actual = widgetService.getWidgetById(1L);

        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void deleteWidget_shouldDeleteWidget() {
        widgetService.deleteWidget(1L);
        Mockito.verify(widgetCrudRepository).delete(1L);
    }

    @Test
    public void getWidgets_shouldReturnEmptyList() {
        Mockito.doReturn(Collections.emptyList()).when(widgetCrudRepository).getWidgets();
        Page<Widget> actualWidgetPage = widgetService.getWidgets(PageRequest.of(1, 10));
        Assert.assertEquals(0,actualWidgetPage.getTotalElements());
    }

    @Test
    public void getWidgets_shouldReturnWidgets() {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(new Widget());
        widgets.add(new Widget());

        Mockito.doReturn(widgets).when(widgetCrudRepository).getWidgets();
        Page<Widget> actualWidgetPage = widgetService.getWidgets(PageRequest.of(0, 1));
        Assert.assertEquals(1, actualWidgetPage.getNumberOfElements());
        Assert.assertEquals(2, actualWidgetPage.getTotalElements());
        Assert.assertEquals(2, actualWidgetPage.getTotalPages());
    }

    @Test(expected = WidgetNotFoundException.class)
    public void updateWidget_shouldThrowException_whenWidgetDoesNotExist() throws WidgetNotFoundException {
        Mockito.doReturn(null).when(widgetCrudRepository).getById(1L);
        widgetService.updateWidget(1L, Mockito.any());
    }

    @Test
    public void updateWidget_shouldNotUpdateWidgetId() throws WidgetNotFoundException {
        Widget widgetToBeUpdated = new Widget();
        widgetToBeUpdated.setId(1L);

        Mockito.doReturn(widgetToBeUpdated).when(widgetCrudRepository).getById(1L);
        Mockito.doNothing().when(widgetCrudRepository).delete(Mockito.any());
        Mockito.doNothing().when(widgetCrudRepository).update(Mockito.any());


        Widget widget = new Widget();
        widget.setId(5L);

        Widget updatedWidget = widgetService.updateWidget(1L, widget);
        Assert.assertEquals(widgetToBeUpdated.getId(), updatedWidget.getId());

    }

    @Test
    public void updateWidget_shouldNotUpdateNullValues() throws WidgetNotFoundException {
        Widget widgetToBeUpdated = new Widget();
        widgetToBeUpdated.setId(1L);
        widgetToBeUpdated.setzIndex(7);

        Mockito.doReturn(widgetToBeUpdated).when(widgetCrudRepository).getById(1L);
        Mockito.doNothing().when(widgetCrudRepository).delete(Mockito.any());
        Mockito.doNothing().when(widgetCrudRepository).update(Mockito.any());


        Widget widget = new Widget();
        widget.setId(5L);
        widget.setzIndex(null);

        Widget updatedWidget = widgetService.updateWidget(1L, widget);
        Assert.assertEquals(widgetToBeUpdated.getzIndex(), updatedWidget.getzIndex());

    }

    @Test
    public void updateWidget_shouldUpdateNonNullValues() throws WidgetNotFoundException {
        Widget widgetToBeUpdated = new Widget();
        widgetToBeUpdated.setId(1L);
        widgetToBeUpdated.setzIndex(7);

        Mockito.doReturn(widgetToBeUpdated).when(widgetCrudRepository).getById(1L);
        Mockito.doNothing().when(widgetCrudRepository).delete(Mockito.any());
        Mockito.doNothing().when(widgetCrudRepository).update(Mockito.any());


        Widget widget = new Widget();
        widget.setId(5L);
        widget.setzIndex(8);

        Widget updatedWidget = widgetService.updateWidget(1L, widget);
        Assert.assertEquals(widget.getzIndex(), updatedWidget.getzIndex());

    }

}