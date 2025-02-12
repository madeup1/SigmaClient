package git.sigmaclient.themes.impl;

import git.sigmaclient.themes.Theme;
import git.sigmaclient.SigmaClient;

import java.awt.*;

public class Gradient extends Theme {
    public Gradient()
    {
        super("Gradient");
    }

    @Override
    public Color getSecondary()
    {
        final float location = (float)((Math.cos((System.currentTimeMillis() * SigmaClient.clickGui.shiftSpeed.getValue()) / 1000.0) + 1.0) * 0.5);
        if (!SigmaClient.clickGui.hsb.isEnabled()) {
            return new Color((int)(SigmaClient.clickGui.redShift1.getValue() + (SigmaClient.clickGui.redShift2.getValue() - SigmaClient.clickGui.redShift1.getValue()) * location), (int)(SigmaClient.clickGui.greenShift1.getValue() + (SigmaClient.clickGui.greenShift2.getValue() - SigmaClient.clickGui.greenShift1.getValue()) * location), (int)(SigmaClient.clickGui.blueShift1.getValue() + (SigmaClient.clickGui.blueShift2.getValue() - SigmaClient.clickGui.blueShift1.getValue()) * location));
        }
        final float[] c1 = Color.RGBtoHSB((int) SigmaClient.clickGui.redShift1.getValue(), (int) SigmaClient.clickGui.greenShift1.getValue(), (int) SigmaClient.clickGui.blueShift1.getValue(), null);
        final float[] c2 = Color.RGBtoHSB((int) SigmaClient.clickGui.redShift2.getValue(), (int) SigmaClient.clickGui.greenShift2.getValue(), (int) SigmaClient.clickGui.blueShift2.getValue(), null);
        return Color.getHSBColor(c1[0] + (c2[0] - c1[0]) * location, c1[1] + (c2[1] - c1[1]) * location, c1[2] + (c2[2] - c1[2]) * location);
    }

    @Override
    public Color getSecondary(int index)
    {
        final float location = (float)((Math.cos((index * 100 + System.currentTimeMillis() * SigmaClient.clickGui.shiftSpeed.getValue()) / 1000.0) + 1.0) * 0.5);
        if (!SigmaClient.clickGui.hsb.isEnabled()) {
            return new Color((int)(SigmaClient.clickGui.redShift1.getValue() + (SigmaClient.clickGui.redShift2.getValue() - SigmaClient.clickGui.redShift1.getValue()) * location), (int)(SigmaClient.clickGui.greenShift1.getValue() + (SigmaClient.clickGui.greenShift2.getValue() - SigmaClient.clickGui.greenShift1.getValue()) * location), (int)(SigmaClient.clickGui.blueShift1.getValue() + (SigmaClient.clickGui.blueShift2.getValue() - SigmaClient.clickGui.blueShift1.getValue()) * location));
        }
        final float[] c1 = Color.RGBtoHSB((int) SigmaClient.clickGui.redShift1.getValue(), (int) SigmaClient.clickGui.greenShift1.getValue(), (int) SigmaClient.clickGui.blueShift1.getValue(), null);
        final float[] c2 = Color.RGBtoHSB((int) SigmaClient.clickGui.redShift2.getValue(), (int) SigmaClient.clickGui.greenShift2.getValue(), (int) SigmaClient.clickGui.blueShift2.getValue(), null);
        return Color.getHSBColor(c1[0] + (c2[0] - c1[0]) * location, c1[1] + (c2[1] - c1[1]) * location, c1[2] + (c2[2] - c1[2]) * location);
    }
}
