package de.hochschuletrier.gdw.ws1415.game.utils;

public class NoGamepadException extends Exception
{
    public NoGamepadException()
    {
        super("Kein Gamepad angeschlossen!");
    }
}
