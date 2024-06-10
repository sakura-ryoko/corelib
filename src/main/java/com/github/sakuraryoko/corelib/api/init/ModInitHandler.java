package com.github.sakuraryoko.corelib.api.init;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;

public class ModInitHandler implements IModInitManager
{
    private static final ModInitHandler INSTANCE = new ModInitHandler();
    private final List<IModInitDispatch> handlers = new ArrayList<>();
    public static IModInitManager getInstance() { return INSTANCE; }

    @Override
    public void registerModInitHandler(IModInitDispatch handler)
    {
        if (!this.handlers.contains(handler))
        {
            this.handlers.add(handler);
        }
    }

    @ApiStatus.Internal
    public void onModInit()
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach(IModInitDispatch::onModInit);
        }
    }

    @ApiStatus.Internal
    public void setIntegratedServer(boolean toggle)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.setIntegratedServer(toggle));
        }
    }

    @ApiStatus.Internal
    public void setDedicatedServer(boolean toggle)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.setDedicatedServer(toggle));
        }
    }

    @ApiStatus.Internal
    public void setOpenToLan(boolean toggle)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.setOpenToLan(toggle));
        }
    }

    @ApiStatus.Internal
    public void reset()
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach(IModInitDispatch::reset);
        }
    }
}
