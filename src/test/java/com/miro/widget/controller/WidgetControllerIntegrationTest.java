package com.miro.widget.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miro.widget.model.Widget;
import com.miro.widget.utils.RestResponsePage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.awt.geom.Point2D;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WidgetControllerIntegrationTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createWidget_shouldCreateNewWidget() throws Exception {
        Widget expectedWidget = new Widget(new Point2D.Double(10.0,20.0), 55, 10.0, 15.0);

        Widget actualWidget = createWidget(expectedWidget);

        Assert.assertEquals(expectedWidget.getCoordinates(), actualWidget.getCoordinates());
        Assert.assertEquals(expectedWidget.getzIndex(), actualWidget.getzIndex());
    }

    @Test
    public void getWidgetById_shouldReturnNotFound_whenWidgetDoesNotExist() throws Exception {

        mockMvc.perform(get("/widgets/-1")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void getWidgetById_shouldReturnTheWidget() throws Exception {
        Widget widget = new Widget(new Point2D.Double(10, 20), 75, 10.0, 20.0);


        Widget expectedWidget = createWidget(widget);


        MvcResult result = mockMvc.perform(get("/widgets/{id}", expectedWidget.getId())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andReturn();

        Widget actualWidget = objectMapper.readValue(result.getResponse().getContentAsString(), Widget.class);
        Assert.assertEquals(widget.getCoordinates(), actualWidget.getCoordinates());
        Assert.assertEquals(widget.getzIndex(), actualWidget.getzIndex());
    }

    @Test
    public void getWidgets_shouldReturnBadRequest_whenPageIsNegative() throws Exception {
        mockMvc.perform(get("/widgets")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .param("page", "-1"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void getWidgets_shouldReturnBadRequest_whenSizeIsGreaterThan500() throws Exception {
        mockMvc.perform(get("/widgets")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .param("size", "1000"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getWidgets_shouldReturnOrderedWidgets() throws Exception {
        Widget widget1 = new Widget(new Point2D.Double(10, 10), 25, 10.0, 20.0);
        createWidget(widget1);

        Widget widget2 = new Widget(new Point2D.Double(20, 20), 23, 10.0, 20.0);
        createWidget(widget2);

        Widget widget3 = new Widget(new Point2D.Double(30, 30), 27, 10.0, 20.0);
        createWidget(widget3);

        MvcResult result = mockMvc.perform(get("/widgets")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Page<Widget> pageWidgets = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<RestResponsePage<Widget>>(){});
        Assert.assertEquals(3, pageWidgets.getTotalElements());
        Assert.assertTrue(pageWidgets.getContent().get(0).getzIndex() < pageWidgets.getContent().get(1).getzIndex());
        Assert.assertTrue(pageWidgets.getContent().get(1).getzIndex() < pageWidgets.getContent().get(2).getzIndex());
    }

    @Test
    public void updateWidget_shouldReturnNotFound_whenWidgetDoesNotExist() throws Exception {

        mockMvc.perform(patch("/widgets/-1")
                .content(objectMapper.writeValueAsBytes(new Widget()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateWidget_shouldUpdateWidget() throws Exception {

        Widget widget = new Widget(new Point2D.Double(10, 10), 33, 100.0, 100.0);
        Widget widgetToBeUpdated = createWidget(widget);

        Widget updatedWidget = new Widget();
        updatedWidget.setWidth(200.0);

        mockMvc.perform(patch("/widgets/{id}", widgetToBeUpdated.getId())
                .content(objectMapper.writeValueAsBytes(updatedWidget))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/widgets/{id}", widgetToBeUpdated.getId())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andReturn();

        Widget actualWidget = objectMapper.readValue(result.getResponse().getContentAsString(), Widget.class);
        Assert.assertEquals(widgetToBeUpdated.getHeight(), actualWidget.getHeight());
        Assert.assertEquals(updatedWidget.getWidth(), actualWidget.getWidth());
    }

    @Test
    public void deleteWidget_shouldDeleteWidget() throws Exception {
        Widget widget = new Widget(new Point2D.Double(10, 10), 44, 10.0, 20.0);
        Widget widgetToBeDeleted = createWidget(widget);

        mockMvc.perform(delete("/widgets/{$id}", widgetToBeDeleted.getId())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/widgets/{id}",widgetToBeDeleted.getId())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createWidget_shouldShiftWidgetsWithTheSameAndGreaterIndexUpwards_whenIndexAlreadyExist() throws Exception {

        Widget widget1 = new Widget(new Point2D.Double(10, 10), 15, 10.0, 20.0);
        Widget createdWidget1 = createWidget(widget1);

        Widget widget2 = new Widget(new Point2D.Double(10, 20), 16, 10.0, 20.0);
        Widget createdWidget2 = createWidget(widget2);

        Widget widget3 = new Widget(new Point2D.Double(10, 20), 15, 10.0, 20.0);
        Widget createdWidget3 = createWidget(widget3);

        MvcResult result1 = mockMvc.perform(get("/widgets/{id}", createdWidget1.getId())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andReturn();

        MvcResult result2 = mockMvc.perform(get("/widgets/{id}", createdWidget2.getId())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andReturn();

        MvcResult result3 = mockMvc.perform(get("/widgets/{id}", createdWidget3.getId())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andReturn();

        Widget actualWidget1 = objectMapper.readValue(result1.getResponse().getContentAsString(), Widget.class);
        Widget actualWidget2 = objectMapper.readValue(result2.getResponse().getContentAsString(), Widget.class);
        Widget actualWidget3 = objectMapper.readValue(result3.getResponse().getContentAsString(), Widget.class);

        Integer expectedZIndex1 = widget1.getzIndex()+1;
        Assert.assertEquals(expectedZIndex1, actualWidget1.getzIndex());

        Integer expectedZIndex2 = widget2.getzIndex()+1;
        Assert.assertEquals(expectedZIndex2, actualWidget2.getzIndex());


        Assert.assertEquals(widget3.getzIndex(), actualWidget3.getzIndex());

    }

    @Test
    public void createWidget_createWidgetWithHighestZIndex_whenZIndexIsNotSpecified() throws Exception {
        // when zIndex is not specified it should moves to the foreground
        // when it is the case, I assume that zIndex should be equal to highest zIndex + 1
        // if there is no highest zIndex, then it will be equal to 0;

        Widget widget1 = new Widget(new Point2D.Double(10, 10), 100, 10.0, 20.0);
        Widget createdWidget1 = createWidget(widget1);

        Widget widget2 = new Widget();
        widget2.setCoordinates(new Point2D.Double(20.0,10.0));
        Widget createdWidget2 = createWidget(widget2);


        MvcResult result = mockMvc.perform(get("/widgets/{id}", createdWidget2.getId())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andReturn();

        Widget actualWidget = objectMapper.readValue(result.getResponse().getContentAsString(), Widget.class);

        Integer expectedZIndex = createdWidget1.getzIndex() + 1;
        Assert.assertEquals(expectedZIndex, actualWidget.getzIndex());

    }



    private Widget createWidget(Widget widget) throws Exception {
        MvcResult result = mockMvc.perform(post("/widgets").content(objectMapper.writeValueAsBytes(widget))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        Widget actualWidget = objectMapper.readValue(result.getResponse().getContentAsString(), Widget.class);
        return actualWidget;
    }


}
