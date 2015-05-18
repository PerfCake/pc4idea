package org.perfcake.pc4idea.api.editor.swing;

import com.intellij.openapi.editor.colors.EditorColorsManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
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
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground().darker().darker());
        g2d.setStroke(new BasicStroke(1.0f,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_MITER,10.0f,new float[]{2.0f,2.0f},0.0f));
        for (DependencyLine line : dependencyLines){
            g.drawLine(line.getLineStart().x,line.getLineStart().y,line.getLineEnd().x,line.getLineEnd().y);
        }
    }

    public void removeAllDependencyLines(){
        dependencyLines.clear();
    }

    public void addDependencyLine(Point start, Point end){
        dependencyLines.add(new DependencyLine(start,end));
        this.repaint();
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
