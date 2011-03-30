/**
 * Copyright (c) 2005-2011 Juan F. Codagnone <http://juan.zaubersoftware.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// HTMLParser Library - A java-based parser for HTML
// http://htmlparser.org
// Copyright (C) 2006 Derrick Oswald
//
// Revision Control Information
//
// $URL: https://htmlparser.svn.sourceforge.net/svnroot/htmlparser/trunk/filterbuilder/src/main/java/org/htmlparser/parserapplications/filterbuilder/layouts/VerticalLayoutManager.java $
// $Author: derrickoswald $
// $Date: 2006-09-16 11:44:17 -0300 (Sat, 16 Sep 2006) $
// $Revision: 4 $
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the Common Public License; either
// version 1.0 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// Common Public License for more details.
//
// You should have received a copy of the Common Public License
// along with this library; if not, the license is available from
// the Open Source Initiative (OSI) website:
//   http://opensource.org/licenses/cpl1.0.php

package ar.com.leak.common.swing;

import java.awt.*;
import java.io.*;

/**
 * A layout manager like a vertical FlowLayout.
 * Stacks components vertically like GridLayout(0,1) but doesn't
 * resize each component equally. More like a vertical FlowLayout
 * but doesn't snake columns or align things.
 */
public class VerticalLayoutManager
    implements
        LayoutManager2,
        Serializable
{ 
    /**
     * Constructs a VerticalLayoutManager object.
     */
    public VerticalLayoutManager ()
    {
    }
  
    /**
     * Calculates the minimum size dimensions for the specified
     * panel given the components in the specified parent container.
     * @param target The component to be laid out.
     * @return The minimum size.
     * @see #preferredLayoutSize
     */
    public Dimension minimumLayoutSize (Container target)
    {
        return (preferredLayoutSize (target));
    }
  
    /**
     * Calculates the preferred size dimensions for the specified
     * panel given the components in the specified parent container.
     * @param target The component to be laid out.
     * @return A size deemed suitable for laying out the container.
     * @see #minimumLayoutSize
     */
    public Dimension preferredLayoutSize (Container target)
    {
        int count;
        Component component;
        Dimension dimension;
        Insets insets;
        Dimension ret;
        
        synchronized (target.getTreeLock ())
        {
            // get the the total height and maximum width component
            ret = new Dimension (0, 0);
            count = target.getComponentCount ();
            for (int i = 0 ; i < count ; i++)
            {
                component = target.getComponent (i);
                if (component.isVisible ())
                {
                    dimension = component.getPreferredSize ();
                    ret.width = Math.max (ret.width, dimension.width);
                    ret.height += dimension.height;
                }
            }
            insets = target.getInsets ();
            ret.width += insets.left + insets.right;
            ret.height += insets.top + insets.bottom;
        }

        return (ret);
    }
  
    /**
     * Returns the maximum size of this component.
     * @param target The component to be laid out.
     * @return The maximum size for the container.
     * @see #preferredLayoutSize
     */
    public Dimension maximumLayoutSize (Container target)
    {
        return (preferredLayoutSize (target));
    }
  
    //
    // LayoutManager Interface
    //
  
    /**
     * Adds the specified component with the specified name to
     * the layout.
     * @param name the component name
     * @param comp the component to be added
     */
    public void addLayoutComponent (String name, Component comp)
    {
    }        
  
    /**
     * Removes the specified component from the layout.
     * @param comp the component ot be removed
     */
    public void removeLayoutComponent (Component comp)
    {
    }
  
    /**
     * Lays out the container.
     * @param target The container which needs to be laid out.
     */
    public void layoutContainer (Container target)
    {
        Insets insets;
        int x;
        int y;
        int count;
        int width;
        Component component;
        Dimension dimension;
        
        synchronized (target.getTreeLock ())
        {
            insets = target.getInsets ();
            x = insets.left;
            y = insets.top;
            count = target.getComponentCount ();
            width = 0;
            for (int i = 0 ; i < count ; i++)
            {
                component = target.getComponent (i);
                if (component.isVisible ())
                {
                    dimension = component.getPreferredSize ();
                    width = Math.max (width, dimension.width);
                    component.setSize (dimension.width, dimension.height);
                    component.setLocation (x, y);
                    y += dimension.height;
                }
            }
            // now set them all to the same width
            for (int i = 0 ; i < count ; i++)
            {
                component = target.getComponent (i);
                if (component.isVisible ())
                {
                    dimension = component.getSize ();
                    dimension.width = width;
                    component.setSize (dimension.width, dimension.height);
                }
            }
        }
    }
  
    //
    // LayoutManager2 Interface
    //
  
    /**
     * Adds the specified component to the layout, using the specified
     * constraint object.
     * @param comp the component to be added
     * @param constraints  where/how the component is added to the layout.
     */
    public void addLayoutComponent (Component comp, Object constraints)
    {
    }
  
    /**
     * Returns the alignment along the x axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     * @param target The target container.
     * @return The X-axis alignment.
     */
    public float getLayoutAlignmentX (Container target)
    {
        return (0.0f);
    }
  
    /**
     * Returns the alignment along the y axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     * @param target The target container.
     * @return The Y-axis alignment.
     */
    public float getLayoutAlignmentY (Container target)
    {
        return (0.0f);
    }
  
    /**
     * Invalidates the layout, indicating that if the layout manager
     * has cached information it should be discarded.
     * @param target The target container.
     */
    public void invalidateLayout (Container target)
    {
    }
}