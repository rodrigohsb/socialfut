package br.com.socialfut.util;

public enum ConstantsEnum
{
    GOLEIRO("Goleiro"), LATERAL("Lateral"), ZAQUEIRO("Zagueiro"), MEIO_CAMPO("Meio_Campo"), ATACANTE("Atacante"), TIMEA(
            "timea"), TIMEB("timeb");

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