package com.miro.widget.controller;

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

    @GetMapping()
    public ResponseEntity<Page<Widget>> getWidgets(
            @RequestParam(required = false, defaultValue ="1") @Min(value = 1, message = "page number should be positive") Integer page,
            @RequestParam(required = false, defaultValue = "10") @Max(value = 500, message = "size should be less than or equal to 500") Integer size) {

        Page<Widget> pageWidgets = widgetService.getWidgets(PageRequest.of(page - 1, size));
        return new ResponseEntity(pageWidgets, HttpStatus.OK);
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


}
