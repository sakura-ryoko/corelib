package com.github.sakuraryoko.corelib.api.events;

public interface IServerEventsManager
{
    void registerServerEvents(IServerEventsDispatch handler);

    void unregisterServerEvents(IServerEventsDispatch handler);
}
