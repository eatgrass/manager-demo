package com.example.manager.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.manager.model.User;

@Component
public class FileBasedUserRepository implements UserRepository, InitializingBean, DisposableBean {

    @Autowired
    private File dbFile;

    private ConcurrentHashMap<Long, User> state = new ConcurrentHashMap<>();

    private ExecutorService synchronizer = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(20), new ThreadPoolExecutor.AbortPolicy());

    @Override
    public void afterPropertiesSet() throws Exception {
        Path path = Paths.get(dbFile.getPath());
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("user")) {
                    var user = User.deserialize(line);
                    state.put(user.userId(), user);
                }
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        synchronizer.shutdown();
    }

    @Override
    public User findById(long userId) {
        return state.get(userId);
    }

    @Override
    public synchronized void save(User user) {
        state.put(user.userId(), user);
        try {
            synchronizer.submit(() -> {
                try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(dbFile.getPath()),
                        StandardOpenOption.APPEND)) {
                    writer.write(user.serialize());
                    writer.newLine();
                } catch (IOException e) {
                }
            });

        } catch (RejectedExecutionException e) {
            throw new RuntimeException("service temporarily unavailable, please try again later");
        }
    }

}
