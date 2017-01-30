package io.github.bensku.loader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.bensku.Callback;
import net.sandius.rembulan.Variable;
import net.sandius.rembulan.load.ChunkLoader;
import net.sandius.rembulan.load.LoaderException;
import net.sandius.rembulan.runtime.LuaFunction;

/**
 * Works asynchronously to load the scripts.
 *
 */
public class AsyncScriptLoader implements ScriptLoader {
    
    private ExecutorService loaderService;
    private ChunkLoader loader;
    
    public AsyncScriptLoader(int threadCount, ChunkLoader chunkLoader) {
        this.loaderService = Executors.newFixedThreadPool(threadCount);
        this.loader = chunkLoader;
    }
    
    @Override
    public void loadScript(String code, String name, Variable env, Callback<LuaFunction> callback) {
        loaderService.submit(new Runnable() {

            @Override
            public void run() { 
                try {
                    callback.onComplete(loader.loadTextChunk(env, name, code));
                } catch (LoaderException e) {
                    callback.onException(e);
                }
            }
        });
    }

    @Override
    public void loadScript(Path path, String name, Variable env, Callback<LuaFunction> callback) {
        loaderService.submit(new Runnable() {

            @Override
            public void run() {
                String code = null;
                try {
                    code = new String(Files.readAllBytes(path), StandardCharsets.UTF_8); // Expect UTF-8
                } catch (IOException e) {
                    callback.onException(e);
                }
                
                try {
                    callback.onComplete(loader.loadTextChunk(env, name, code));
                } catch (LoaderException e) {
                    callback.onException(e);
                }
            }
        });
    }

}
