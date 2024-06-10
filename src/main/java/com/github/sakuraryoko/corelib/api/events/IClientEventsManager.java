package com.github.sakuraryoko.corelib.api.events;

public interface IClientEventsManager
{
    void registerClientEvents(IClientEventsDispatch handler);

    void unregisterClientEvents(IClientEventsDispatch handler);
}
