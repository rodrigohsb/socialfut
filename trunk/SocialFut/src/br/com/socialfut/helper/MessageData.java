package br.com.socialfut.helper;

import br.com.socialfut.util.Sender;

public class MessageData
{
    private String message;

    private Sender sender;

    public MessageData(String message, Sender sender)
    {
        this.message = message;
        this.sender = sender;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public Sender getSender()
    {
        return sender;
    }

    public void setSender(Sender sender)
    {
        this.sender = sender;
    }
}