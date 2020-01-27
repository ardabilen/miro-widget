package com.miro.widget.utils;


import com.miro.widget.model.Widget;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.geom.Point2D;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TwoDimensionalTreeTest {

    @Test
    public void rangeSearch_shouldReturnWidgetsWithinTheRange() {

        TwoDimensionalTree tree = new TwoDimensionalTree();

        Widget widget1 = new Widget(new Point2D.Double(10, 10), 101, 10.0, 10.0);
        widget1.setId(1L);

        Widget widget2 = new Widget(new Point2D.Double(30, 30), 102, 10.0, 10.0);
        widget2.setId(2L);

        Widget widget3 = new Widget(new Point2D.Double(40, 40), 103, 10.0, 10.0);
        widget3.setId(3L);

        tree.insert(widget1);
        tree.insert(widget2);
        tree.insert(widget3);

        List<Widget> widgets = tree.rangeSearch(0.0, 50.0, 25.0, 35.0);
        Assert.assertEquals(1, widgets.size());
        Assert.assertEquals(widget2, widgets.get(0));
    }

    @Test
    public void deleteNode_shouldDeleteTheNodeAndPreserveBST() {

        /*
                10,10
               /     \
             5,20    15,20
                     /
                   12,15

            //after delete 10,10

                12,15
               /     \
             5,20    15,20

         */

        TwoDimensionalTree tree = new TwoDimensionalTree();

        Widget widget1 = new Widget();
        widget1.setId(1L);
        widget1.setCoordinates(new Point2D.Double(10.0, 10.0));

        Widget widget2 = new Widget();
        widget2.setId(2L);
        widget2.setCoordinates(new Point2D.Double(5.0, 20.0));

        Widget widget3 = new Widget();
        widget3.setId(3L);
        widget3.setCoordinates(new Point2D.Double(15.0, 20.0));

        Widget widget4 = new Widget();
        widget4.setId(4L);
        widget4.setCoordinates(new Point2D.Double(12.0, 15.0));


        tree.insert(widget1);
        tree.insert(widget2);
        tree.insert(widget3);
        tree.insert(widget4);

        tree.deleteNode(widget1);

        Assert.assertEquals(widget4, tree.root.widget);
        Assert.assertEquals(widget2, tree.root.left.widget);
        Assert.assertEquals(widget3, tree.root.right.widget);


    }

}
