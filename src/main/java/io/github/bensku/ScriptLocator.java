package io.github.bensku;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Locates scripts from a folder, and delegates loading them to
 * script manager.
 *
 */
public class ScriptLocator {
    
    private ScriptManager manager;
    private Path root;
    
    public ScriptLocator(ScriptManager manager, Path root) {
        this.manager = manager;
        this.root = root;
    }
    
    public void load(Path path) {
        manager.loadScript(path, root.relativize(path).toString().replaceAll(File.pathSeparator, ".")); // Load script
    }
    
    public void loadAll() {
        try {
            loadAll0(root);
        } catch (StackOverflowError e) {
            // Ouch, someone had too much fun with symlinks
            e.printStackTrace();
        }
    }
    
    private void loadAll0(Path dir) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir)) {
            for (Path path : directoryStream) {
                if (Files.isDirectory(path)) { // Subdirectory
                    loadAll0(path);
                } else {
                    manager.loadScript(path, root.relativize(path).toString().replaceAll(File.pathSeparator, ".")); // Load script
                    // Remember to replace "/" or "\" with "."
                }
            }
        } catch (IOException e) { // Don't interrupt whole loading, just this directory
            // TODO better logging
            e.printStackTrace();
        }
    }
    
    public void unload(Path path) {
        manager.unload(path);
    }
    
    public void unloadAll() {
        manager.unloadAll();
    }
}
