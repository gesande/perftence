package net.sf.perftence.graph.afreechart;

import static java.awt.Color.BLACK;
import static java.awt.Color.CYAN;
import static java.awt.Color.GRAY;
import static java.awt.Color.GREEN;
import static java.awt.Color.RED;

import java.awt.Color;

import org.afree.graphics.PaintType;
import org.afree.graphics.SolidColor;

import net.sf.perfence.graph.afreechart.ChartColors;

final class SolidColors implements ChartColors {

    @Override
    public PaintType red() {
        return solidColor(RED);
    }

    @Override
    public PaintType cyan() {
        return solidColor(CYAN);
    }

    @Override
    public PaintType green() {
        return solidColor(GREEN);
    }

    @Override
    public PaintType gray() {
        return solidColor(GRAY);
    }

    private static PaintType solidColor(final Color color) {
        return new SolidColor(color.getRGB());
    }

    @Override
    public PaintType black() {
        return solidColor(BLACK);
    }

}
