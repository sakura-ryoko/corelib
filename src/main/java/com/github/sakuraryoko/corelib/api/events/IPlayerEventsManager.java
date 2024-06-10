package com.github.sakuraryoko.corelib.api.events;

public interface IPlayerEventsManager
{
    void registerPlayerEvents(IPlayerEventsDispatch handler);

    void unregisterPlayerEvents(IPlayerEventsDispatch handler);
}
