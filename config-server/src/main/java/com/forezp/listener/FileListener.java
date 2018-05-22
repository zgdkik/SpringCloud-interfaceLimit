package com.forezp.listener;

/**
 * Created by Administrator on 2018/3/27.
 */
import com.forezp.util.HttpUtilTest;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;


@Component(value = "alterationListener")      //不加这个注解的话, 使用@Autowired 就不能注入进去了
public class FileListener implements FileAlterationListener {

    @Value("${configCallBack}")
    private String url;

    @Override
    public void onStart(FileAlterationObserver observer) {
        // TODO Auto-generated method stub
        //System.out.println("onStart()");

    }

    @Override
    public void onDirectoryCreate(File directory) {
        // TODO Auto-generated method stub
        //System.out.println("onDirectoryCreate()");
    }

    @Override
    public void onDirectoryChange(File directory) {
        // TODO Auto-generated method stub
        //System.out.println("onDirectoryChange()");
    }

    @Override
    public void onDirectoryDelete(File directory) {
        // TODO Auto-generated method stub
        //System.out.println("onDirectoryDelete()");
    }

    @Override
    public void onFileCreate(File file) {
        // TODO Auto-generated method stub
        //System.out.println("onFileCreate()");
    }

    @Override
    public void onFileChange(File file) {
        // TODO Auto-generated method stub
        System.out.println("onFileChange()");
        System.out.println("url="+url);
        try {
            HttpUtilTest.sendPost(url,"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFileDelete(File file) {
        // TODO Auto-generated method stub
        System.out.println("onFileDelete()");
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
        // TODO Auto-generated method stub
        //System.out.println("onStop()");
    }

}
