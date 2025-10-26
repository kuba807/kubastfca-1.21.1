package net.kuba807.kubastfca.common.fluids;

import java.util.Locale;



public enum DefaultFluids
    {




    SOUR_MILK(0xFFFFFBE8);

    private final String id;
    private final int color;

    DefaultFluids(int color)
    {
        this.id = name().toLowerCase(Locale.ROOT);
        this.color = color;
    }

    public String getId()
    {
        return id;
    }

    public int getColor()
    {
        return color;
    }

    public boolean isTransparent()
    {
        return true;
    }
}