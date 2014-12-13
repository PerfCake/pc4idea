package org.perfcake.pc4idea.editor.designer.common;

import com.intellij.ide.FileIconPatcher;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;
import org.perfcake.pc4idea.editor.PerfCakeEditorProvider;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 5.12.2014
 */
public class PerfCakeIconPatcher implements FileIconPatcher {
    @Override
    public Icon patchIcon(Icon icon, VirtualFile virtualFile, @Iconable.IconFlags int i, @Nullable Project project) {
        boolean accepting = false;
        if (project != null && virtualFile != null){
            accepting = new PerfCakeEditorProvider().accept(project,virtualFile);
        }
        Icon perfCakeIcon = loadIcon();
        if (perfCakeIcon == null){
            accepting = false;
        }

        return (accepting) ? perfCakeIcon : icon;
    }

    public static Icon loadIcon(){
        Icon perfCakeIcon;
        try {
            final BufferedImage image = ImageIO.read(PerfCakeIconPatcher.class.getResourceAsStream("/file-logo.png"));

            perfCakeIcon = new Icon() {
                @Override
                public void paintIcon(Component c, Graphics g, int x, int y) {
                    g.drawImage(image,x,y,null);
                }

                @Override
                public int getIconWidth() {
                    return image.getWidth();
                }

                @Override
                public int getIconHeight() {
                    return image.getHeight();
                }
            };
        } catch (IOException e) {
            perfCakeIcon = null;
        }
        return perfCakeIcon;
    }
}
