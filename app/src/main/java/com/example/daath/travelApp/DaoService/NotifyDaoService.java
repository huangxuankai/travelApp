package com.example.daath.travelApp.DaoService;

import android.content.Context;
import android.util.Log;

import com.example.daath.travelApp.App;
import com.my.greenDao.bean.Notify;
import com.my.greenDao.dao.DaoSession;
import com.my.greenDao.dao.NotifyDao;

import java.util.List;

/**
 * Created by daath on 16-4-25.
 */
public class NotifyDaoService {

    private static final String TAG = NotifyDaoService.class.getSimpleName();
    private static NotifyDaoService instance;
    private static Context appContext;
    private DaoSession mDaoSession;
    private NotifyDao notifyDao;

    private NotifyDaoService() {}

    public static NotifyDaoService getInstance(Context context) {
        if (instance == null) {
            instance = new NotifyDaoService();
            if (appContext == null) {
                appContext = context.getApplicationContext();
            }
            instance.mDaoSession = App.getDaoSession(context);
            instance.notifyDao = instance.mDaoSession.getNotifyDao();
        }
        return instance;
    }

    public NotifyDao getNotifyDao() {
        return notifyDao;
    }

    public void setNotifyDao(NotifyDao notifyDao) {
        this.notifyDao = notifyDao;
    }

    public Notify loadNotify(long id) {
        return notifyDao.load(id);
    }

    public List<Notify> loadAllNotify() {
        return notifyDao.loadAll();
    }

    /**
     * query list with where clause
     * ex: begin_date_time >= ? AND end_date_time <= ?
     * @param where where clause, include 'where' word
     * @param params query parameters
     * @return
     */

    public List<Notify> queryNote(String where, String... params){
        return notifyDao.queryRaw(where, params);
    }

    /**
     * insert or update note
     * @param notify
     * @return insert or update note id
     */
    public long saveNotify(Notify notify){
        return notifyDao.insertOrReplace(notify);
    }


    /**
     * insert or update noteList use transaction
     * @param list
     */
    public void saveNotifyLists(final List<Notify> list){
        if(list == null || list.isEmpty()){
            return;
        }
        notifyDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<list.size(); i++){
                    Notify note = list.get(i);
                    notifyDao.insertOrReplace(note);
                }
            }
        });

    }

    /**
     * delete all note
     */
    public void deleteAllNotify(){
        notifyDao.deleteAll();
    }

    /**
     * delete note by id
     * @param id
     */
    public void deleteNotify(long id){
        notifyDao.deleteByKey(id);
        Log.i(TAG, "delete");
    }

    public void deleteNote(Notify Notify){
        notifyDao.delete(Notify);
    }
}
