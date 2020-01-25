package com.miro.widget.controller;

import com.miro.widget.model.Widget;
import com.miro.widget.exception.WidgetNotFoundException;
import com.miro.widget.services.WidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value = "/widgets", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class WidgetController {

    private final WidgetService widgetService;

    @Autowired
    public WidgetController(WidgetService widgetService) {
        this.widgetService = widgetService;
    }

    @PostMapping
    public ResponseEntity<Widget> createWidget(@RequestBody Widget widget) {
        Widget created = widgetService.createWidget(widget);
        return new ResponseEntity(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Widget> getWidgetById(@PathVariable Long id) {
        Optional<Widget> widget = widgetService.getWidgetById(id);

        if(widget.isPresent()){
            return new ResponseEntity(widget.get(), HttpStatus.OK);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<Widget>> getWidgets() {
        List<Widget> widgets = widgetService.getWidgets();
        return new ResponseEntity(widgets, HttpStatus.OK);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Widget> updateWidget(@RequestBody Widget widget, @PathVariable Long id) {

        Widget updated;
        try {
            updated = widgetService.updateWidget(id, widget);
        } catch (WidgetNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(updated, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteWidget(@PathVariable Long id){
        widgetService.deleteWidget(id);
        return new ResponseEntity(HttpStatus.OK);
    }


}
