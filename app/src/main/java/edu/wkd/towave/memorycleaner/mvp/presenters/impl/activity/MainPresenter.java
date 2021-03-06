package edu.wkd.towave.memorycleaner.mvp.presenters.impl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import edu.wkd.towave.memorycleaner.R;
import edu.wkd.towave.memorycleaner.injector.ContextLifeCycle;
import edu.wkd.towave.memorycleaner.mvp.presenters.Presenter;
import edu.wkd.towave.memorycleaner.mvp.views.View;
import edu.wkd.towave.memorycleaner.mvp.views.impl.activity.MainView;
import edu.wkd.towave.memorycleaner.tools.PreferenceUtils;
import edu.wkd.towave.memorycleaner.ui.activity.AppManage;
import edu.wkd.towave.memorycleaner.ui.activity.AutoStartManage;
import edu.wkd.towave.memorycleaner.ui.activity.MemoryClean;
import edu.wkd.towave.memorycleaner.ui.activity.RubbishClean;
import edu.wkd.towave.memorycleaner.ui.activity.SettingActivity;
import edu.wkd.towave.memorycleaner.ui.fragment.CircularLoader;
import edu.wkd.towave.memorycleaner.ui.fragment.LineChart;
import java.util.ArrayList;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Administrator on 2016/5/4.
 */
public class MainPresenter implements Presenter {
    MainView mMainView;
    final Context mContext;
    private PreferenceUtils mPreferenceUtils;
    ArrayList<Fragment> items;


    @Inject
    public MainPresenter(@ContextLifeCycle("Activity")
                         Context context, PreferenceUtils preferenceUtils) {
        this.mContext = context;
        this.mPreferenceUtils = preferenceUtils;
    }


    @Override public void attachView(View view) {
        this.mMainView = (MainView) view;
    }


    @Override public void onCreate(Bundle savedInstanceState) {
        mMainView.initToolbar();
        mMainView.initDrawerView();
        initViewPager();
        EventBus.getDefault().register(this);
    }


    public void initViewPager() {
        items = new ArrayList<>();
        items.add(new CircularLoader());
        items.add(new LineChart());
        mMainView.initViewPager(items);
    }

    //public boolean onOptionsItemSelected(MenuItem item) {
    //    switch (item.getItemId()) {
    //        case R.id.action_settings:
    //            mContext.startActivity(
    //                    new Intent(mContext, SettingActivity.class));
    //            return true;
    //    }
    //    return false;
    //}


    @Subscribe public void onEventMainThread(NotifyEvent event) {
        switch (event.getType()) {
            case NotifyEvent.CHANGE_THEME:
                mMainView.reCreate();
                break;
        }
    }


    public static class NotifyEvent<T> {
        public static final int CHANGE_THEME = 0;
        public static final int CHANGE_ITEM_LAYOUT = 1;
        private int type;
        private T data;

        @IntDef({ CHANGE_THEME, CHANGE_ITEM_LAYOUT }) public @interface Type {}


        public @Type int getType() {
            return type;
        }


        public void setType(@Type int type) {
            this.type = type;
        }


        public T getData() {
            return data;
        }


        public void setData(T data) {
            this.data = data;
        }
    }


    @Override public void onResume() {

    }


    @Override public void onStart() {
        //EventBus.getDefault().register(this);
    }


    @Override public void onPause() {

    }


    @Override public void onStop() {
        //EventBus.getDefault().unregister(this);
    }


    @Override public void onDestroy() {
        EventBus.getDefault().unregister(this);
        //RefWatcher refWatcher = App.getRefWatcher(mContext);
        //refWatcher.watch(this);
    }


    public void startActivity(Class activity) {
        mContext.startActivity(new Intent(mContext, activity));
    }


    public void onNavigationItemSelected(int id) {
        // Handle navigation view item clicks here.
        switch (id) {
            case R.id.speedup:
                startActivity(MemoryClean.class);
                break;
            case R.id.trash:
                startActivity(RubbishClean.class);
                break;
            case R.id.autorun:
                startActivity(AutoStartManage.class);
                break;
            case R.id.apps:
                startActivity(AppManage.class);
                break;
            case R.id.nav_share:
                //startActivity(MemoryClean.class);
                break;
            case R.id.nav_setting:
                startActivity(SettingActivity.class);
                break;
        }
    }
}
