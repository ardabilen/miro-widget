package com.miro.widget.utils;

import com.miro.widget.model.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Widget linked hash map.
 */
public class WidgetLinkedHashMap {


    private Map<Long, WidgetEntry> linkedMap;
    private WidgetEntry head, tail;


    /**
     * Instantiates a new Widget linked hash map.
     */
    public WidgetLinkedHashMap() {
        linkedMap = new HashMap<>();
        head = new WidgetEntry();
        tail = new WidgetEntry();
        head.next = tail;
        tail.prev = head;
    }

    /**
     * Get widget.
     *
     * @param id the id
     * @return the widget
     */
    public Widget get(Long id) {
        if (linkedMap.containsKey(id)) {
            return linkedMap.get(id).value;
        }
        return null;
    }

    /**
     * Put.
     *
     * @param id     the id
     * @param widget the widget
     */
    public void put(Long id, Widget widget) {
        WidgetEntry current;
        if (widget.getzIndex() != null) {
            WidgetEntry entry = findWidgetPosition(widget.getzIndex());

            // its the biggest zIndex among existing widgets. put it to the end OR
            // there is a gap with given widget's zIndex; put the widget to that gap.
            if (entry.next.value == null || entry.next.value.getzIndex() > widget.getzIndex()) {
                current = putAfter(entry, widget);
            }

            // there is another widget with given zIndex
            else{
                increaseZIndexAfter(entry.next);
                current = putAfter(entry, widget);
            }
        }
        // when zIndex is not specified it should moves to the foreground
        // when it is the case, I assume that zIndex should be equal to highest zIndex + 1
        // if there is no highest zIndex, then it will be equal to 0;
        else{
            if(tail.prev.value != null){
                widget.setzIndex(tail.prev.value.getzIndex() + 1);
            }
            else{
                widget.setzIndex(0);
            }
            current = putAfter(tail.prev, widget);
        }
        linkedMap.put(id, current);
    }

    /**
     * Delete.
     *
     * @param id the id
     */
    public void delete(Long id) {
        if (linkedMap.containsKey(id)) {
            WidgetEntry entry = linkedMap.get(id);
            entry.prev.next = entry.next;
            entry.next.prev = entry.prev;
            linkedMap.remove(id);
        }
    }

    /**
     * Gets widgets.
     *
     * @return the widgets
     */
    public List<Widget> getWidgets() {
        List<Widget> result = new ArrayList<>();
        WidgetEntry temp = head.next;
        while (temp != null && temp.value != null) {
            result.add(temp.value);
            temp = temp.next;
        }
        return result;
    }


    private void increaseZIndexAfter(WidgetEntry entry) {

        while (entry != null && entry.value != null) {
            entry.value.increaseZIndex();
            entry = entry.next;
        }
    }

    private WidgetEntry putAfter(WidgetEntry entry, Widget widget) {
        WidgetEntry current = new WidgetEntry(widget);
        current.prev = entry;
        current.next = entry.next;
        entry.next = current;
        current.next.prev = current;
        return current;
    }

    private WidgetEntry findWidgetPosition(Integer zIndex){
        WidgetEntry temp = head;
        while(temp.next != null &&  temp.next.value != null && temp.next.value.getzIndex() < zIndex){
            temp = temp.next;
        }
        return temp;
    }


    /**
     * The type Widget entry.
     */
    class WidgetEntry{
        /**
         * The Value.
         */
        public Widget value;
        /**
         * The Next.
         */
        public WidgetEntry next;
        /**
         * The Prev.
         */
        public WidgetEntry prev;

        /**
         * Instantiates a new Widget entry.
         */
        public WidgetEntry() {
        }

        /**
         * Instantiates a new Widget entry.
         *
         * @param value the value
         */
        public WidgetEntry(Widget value) {
            this.value = value;
        }
    }

}
