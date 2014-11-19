package org.perfcake.pc4idea.editor.designer.common;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 18.11.2014
 */
public class DependenciesPanel extends JPanel {
    private List<DependencyLine> dependencyLines;


    public DependenciesPanel(){
        this.setOpaque(false);
        dependencyLines = new ArrayList<>();
    }

    @Override
    protected void paintComponent(Graphics g){
        g.setColor(Color.black);

        for (DependencyLine line : dependencyLines){
            g.drawLine(line.getLineStart().x,line.getLineStart().y,line.getLineEnd().x,line.getLineEnd().y);
        }
    }

    public void removeAllDependencyLines(){
        dependencyLines.clear();
    }

    public void addDependencyLine(Point start, Point end){
        dependencyLines.add(new DependencyLine(start,end));
    }

    private class DependencyLine {
        Point lineStart;
        Point lineEnd;

        private DependencyLine(Point lineStart, Point lineEnd){
            this.lineStart = lineStart;
            this.lineEnd = lineEnd;
        }

        private Point getLineStart(){
            return lineStart;
        }
        private Point getLineEnd(){
            return lineEnd;
        }
    }
}
