package util;

public class Player
{

    private static String name;

    private static String sureName;

    private static String position;

    private static float rating;

    public static String getName()
    {
        return name;
    }

    public static void setName(String name)
    {
        Player.name = name;
    }

    public static String getSureName()
    {
        return sureName;
    }

    public static void setSureName(String sureName)
    {
        Player.sureName = sureName;
    }

    public static String getPosition()
    {
        return position;
    }

    public static void setPosition(String position)
    {
        Player.position = position;
    }

    public static float getRating()
    {
        return rating;
    }

    public static void setRating(float rating)
    {
        Player.rating = rating;
    }

}
