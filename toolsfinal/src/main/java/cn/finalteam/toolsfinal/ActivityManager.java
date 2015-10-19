package cn.finalteam.toolsfinal;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import java.util.Iterator;
import java.util.Stack;

/**
 * Desction:应用程序Activity管理类：用于Activity管理和应用程序退出
 * Author:pengjianbo
 * Date:15/9/17 下午4:48
 */
public class ActivityManager {

    private static Stack<Activity> activityStack;
    private static ActivityManager instance;

    private ActivityManager(){}

    /**
     * 单一实例
     */
    public static ActivityManager getActivityManager(){
        if(instance==null){
            instance=new ActivityManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity){
        if(activityStack==null){
            activityStack=new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity(){
        if( activityStack == null ) {
            return null;
        }
        Activity activity=activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity(){
        if( activityStack == null ) {
            return;
        }
        Activity activity=activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity){
        if( activityStack == null ) {
            return;
        }
        if(activity!=null){
            activityStack.remove(activity);
            activity.finish();
            activity=null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls){
        if( activityStack == null ) {
            return;
        }
        Iterator<Activity> iterator = activityStack.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if(activity != null && activity.getClass().equals(cls) ){
                activity.finish();
                iterator.remove();
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity(){
        if( activityStack == null ) {
            return;
        }
        Iterator<Activity> iterator = activityStack.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if(activity != null && !activity.isFinishing()){
                activity.finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 根据ActivityName获取堆中Activity实例
     * @param activityName
     * @return
     */
    public Activity getActivity(String activityName) {
        Iterator<Activity> iterator = activityStack.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if(activity != null && TextUtils.equals(activity.getClass().getName(), activityName)){
                return activity;
            }
        }
        return null;
    }

    /**
     * 退出应用程序
     */
    public void appExit(Context context) {
        try {
            finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {	}
    }
}
