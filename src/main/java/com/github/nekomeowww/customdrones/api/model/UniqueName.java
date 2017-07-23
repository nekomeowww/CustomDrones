package com.github.nekomeowww.customdrones.api.model;

public class UniqueName
{
    public String string;

    public UniqueName(String s)
    {
        this.string = s;
    }

    public String toString()
    {
        return this.string;
    }

    public int hashCode()
    {
        return this.string.hashCode();
    }

    public boolean equals(Object obj)
    {
        return ((obj instanceof UniqueName)) && (((UniqueName)obj).string.equals(this.string));
    }
}
