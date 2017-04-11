package com.yo.shishkoam.simpleui.helpers;

import com.yo.shishkoam.simpleui.activity.EditMovieActivity;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by User on 08.02.2017
 */
public class TaskManager {
    private static TaskManager ourInstance = new TaskManager();

    public static TaskManager getInstance() {
        return ourInstance;
    }

    private TaskManager() {
    }

    private Set<EditMovieActivity.LinkActivityAsyncTask> tasks = new HashSet<>();

    public void addTask(EditMovieActivity.LinkActivityAsyncTask task) {
        tasks.add(task);
    }

    public void cancelTasks() {
        for (EditMovieActivity.LinkActivityAsyncTask task : tasks) {
            task.cancel(true);
        }
        clearTasks();
    }

    public void clearTasks() {
        tasks.clear();
    }

    public boolean hasActiveTasks() {
        return tasks.size() > 0;
    }

    public void linkTasksToNewActivity(EditMovieActivity activity) {
        for (EditMovieActivity.LinkActivityAsyncTask task : tasks) {
            task.linkActivity(activity);
        }
    }
}
