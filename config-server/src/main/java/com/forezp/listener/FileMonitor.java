package com.forezp.listener;

/**
 * Created by Administrator on 2018/3/27.
 */
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;


@Component      //不加这个注解的话, 使用@Autowired 就不能注入进去了
public class FileMonitor {

    private FileAlterationMonitor monitor ;

    @Value("${configFilePath}")
    private String filePath;

    @Autowired
    private FileListener alterationListener;
    private long intervalTime = 10000L;

    public FileMonitor(long interval) throws Exception {
        monitor = new FileAlterationMonitor(interval);
    }
    public FileMonitor() throws Exception {
        monitor = new FileAlterationMonitor(intervalTime);
    }

    public void monitor(String path, FileAlterationListener listener) {
        FileAlterationObserver observer = new FileAlterationObserver(new File(path));
        monitor.addObserver(observer);
        observer.addListener(listener);
    }
    public void monitor() {
        FileAlterationObserver observer = new FileAlterationObserver(new File(filePath));
        monitor.addObserver(observer);
        observer.addListener(alterationListener);
    }
    public void stop() throws Exception{
        monitor.stop();
    }
    public void start() throws Exception {
        monitor.start();
    }

    public long getIntervalTime() {
        return intervalTime;
    }
    public void setIntervalTime(long intervalTime) {
        this.intervalTime = intervalTime;
    }

}
