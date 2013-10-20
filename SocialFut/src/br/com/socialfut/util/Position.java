package br.com.socialfut.util;

public enum Position
{
    Goleiro(0), Zagueiro(1), Lateral(2), Meio_Campo(3), Atacante(4);

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
