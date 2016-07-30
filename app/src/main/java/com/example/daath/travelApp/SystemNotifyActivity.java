package com.example.daath.travelApp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.daath.travelApp.DaoService.NotifyDaoService;
import com.example.daath.travelApp.customClass.NotifyAdapter;
import com.my.greenDao.bean.Notify;
import com.my.greenDao.dao.NotifyDao;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.Property;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

public class SystemNotifyActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView notifyListView;
    private List<Notify> notifyList = new ArrayList<Notify>();
    private NotifyAdapter notifyAdapter;

    public static void anotherActionStart(Context context) {
        Intent intent = new Intent(context, SystemNotifyActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_notify);
        initView();
        getNotifyListFromDatabase();
    }


    public void initView() {
        notifyListView = (ListView) findViewById(R.id.systemNotify_listView);
        notifyAdapter = new NotifyAdapter(this, R.layout.activity_system_notify_list_item, notifyList);
        notifyListView.setAdapter(notifyAdapter);
    }

    public void getNotifyListFromDatabase() {
        QueryBuilder queryBuilder = NotifyDaoService.getInstance(this).getNotifyDao().queryBuilder();
        queryBuilder.orderDesc(NotifyDao.Properties.CreateDate);
        queryBuilder.build();
        notifyList = queryBuilder.list();
        notifyAdapter.setNotifyList(notifyList);
        notifyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Notify notify = notifyList.get(position);

    }
}
