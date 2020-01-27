package com.miro.widget.controller;

import com.miro.widget.exception.MissingParameterException;
import com.miro.widget.model.Widget;
import com.miro.widget.exception.WidgetNotFoundException;
import com.miro.widget.services.WidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Optional;


@RestController
@RequestMapping(value = "/widgets", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class WidgetController {

    private final WidgetService widgetService;

    /**
     * Instantiates a new Widget controller.
     *
     * @param widgetService the widget service
     */
    @Autowired
    public WidgetController(WidgetService widgetService) {
        this.widgetService = widgetService;
    }

    /**
     * Create widget response entity.
     *
     * @param widget the widget
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<Widget> createWidget(@RequestBody Widget widget) {
        Widget created = widgetService.createWidget(widget);
        return new ResponseEntity(created, HttpStatus.CREATED);
    }

    /**
     * Gets widget by id.
     *
     * @param id the id
     * @return the widget by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Widget> getWidgetById(@PathVariable Long id) {
        Optional<Widget> widget = widgetService.getWidgetById(id);

        if(widget.isPresent()){
            return new ResponseEntity(widget.get(), HttpStatus.OK);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    /**
     * Gets widgets.
     *
     * @param page   the page
     * @param size   the size
     * @param x      the x
     * @param y      the y
     * @param width  the width
     * @param height the height
     * @return the widgets
     */
    @GetMapping()
    public ResponseEntity<Page<Widget>> getWidgets(
            @RequestParam(required = false, defaultValue ="1") @Min(value = 1, message = "page number should be positive") Integer page,
            @RequestParam(required = false, defaultValue = "10") @Max(value = 500, message = "size should be less than or equal to 500") Integer size,
            @RequestParam(required = false) Double x,
            @RequestParam(required = false) Double y,
            @RequestParam(required = false) Double width,
            @RequestParam(required = false) Double height) throws MissingParameterException {

        if (x != null || y != null || width != null || height != null) {
            if (x == null || y == null || width == null || height == null) {
                throw new MissingParameterException("x, y, width and height parameter should be provided together");
            }
        }

        Page<Widget> pageWidgets = widgetService.getWidgets(PageRequest.of(page - 1, size), x, y, width, height);
        return new ResponseEntity(pageWidgets, HttpStatus.OK);
    }


    /**
     * Update widget response entity.
     *
     * @param widget the widget
     * @param id     the id
     * @return the response entity
     */
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


    /**
     * Delete widget response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteWidget(@PathVariable Long id){
        widgetService.deleteWidget(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Handle validation failure response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationFailure(ConstraintViolationException ex) {
        StringBuilder messages = new StringBuilder();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            messages.append(violation.getMessage());
            messages.append("\n");
        }

        return ResponseEntity.badRequest().body(messages.toString());
    }

    @ExceptionHandler(value = {MissingParameterException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMissingParameterException(MissingParameterException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }


}
