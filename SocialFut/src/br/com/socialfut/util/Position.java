package br.com.socialfut.util;

public enum Position
{
    GOLEIRO(0), ZAGUEIRO(1), LATERAL(2), MEIO_CAMPO(3), ATACANTE(4);

    private int value;

    private Position(int value)
    {
        this.value = value;
    }

    public int getvalue()
    {
        return value;
    }
}
