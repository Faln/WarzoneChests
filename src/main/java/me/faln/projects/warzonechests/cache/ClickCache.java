package me.faln.projects.warzonechests.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import me.faln.projects.warzonechests.tasks.OpeningTask;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
public class ClickCache {

    private final Cache<UUID, OpeningTask> clicks = CacheBuilder.newBuilder().expireAfterAccess(400, TimeUnit.MILLISECONDS).build();

}
