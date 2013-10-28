package br.com.socialfut.util;

public enum ConstantsEnum
{
    TIMEA("timea"), TIMEB("timeb");

    private String name;

    ConstantsEnum(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}