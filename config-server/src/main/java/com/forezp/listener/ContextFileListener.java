package com.forezp.listener;

/**
 * Created by Administrator on 2018/3/27.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ContextFileListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private FileMonitor fileMonitor;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //当spring 容器初始化完成后执行某个方法 防止onApplicationEvent方法被执行两次
        if(event.getApplicationContext().getParent().getParent() == null) {
            System.out.printf("我被执行了");
        }
        else{
            try {
                fileMonitor.monitor();
                fileMonitor.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}