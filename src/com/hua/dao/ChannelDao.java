
package com.hua.dao;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hua.bean.ChannelItem;
import com.hua.db.SQLHelper;
import com.hua.utils.LogUtils2;

public class ChannelDao implements ChannelDaoInface {
    private SQLHelper helper = null;

    public ChannelDao(Context context) {
        helper = new SQLHelper(context);
    }

    /**
     * 插入数据到ChannelItem表中
     */
    @Override
    public boolean addCache(ChannelItem item) {
        boolean flag = false;
        SQLiteDatabase database = null;
        long id = -1;
        try {
            database = helper.getWritableDatabase();
            database.beginTransaction();
            ContentValues values = new ContentValues();

            java.lang.Class<? extends ChannelItem> clazz = item.getClass();

            String tableNmae = clazz.getSimpleName();

            Method[] methods = clazz.getMethods();

            for (Method method : methods) {
                String mName = method.getName();
                if (mName.startsWith("get") && !mName.startsWith("getClass")) {
                    String fieldName = mName.substring(3, mName.length()).toLowerCase();
                    LogUtils2.i("fieldName  ==  "+fieldName);
                    Object value = method.invoke(item, null);
                    if (value instanceof String) {
                        values.put(fieldName, (String) value);
                    }
                }
            }

            // values.put("name", item.getName());
            // values.put("id", item.getId());
            // values.put("orderId", item.getOrderId());
            // values.put("selected", item.getSelected());
            id = database.insert(tableNmae, null, values);
            flag = (id != -1 ? true : false);
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.endTransaction();
                database.close();
            }
        }
        return flag;
    }

    // @Override
    // public boolean addCache(ChannelItem item) {
    // boolean flag = false;
    // SQLiteDatabase database = null;
    // long id = -1;
    // try {
    // database = helper.getWritableDatabase();
    // database.beginTransaction();
    // ContentValues values = new ContentValues();
    // values.put("name", item.getName());
    // values.put("id", item.getId());
    // values.put("orderId", item.getOrderId());
    // values.put("selected", item.getSelected());
    // id = database.insert(SQLHelper.TABLE_CHANNEL, null, values);
    // flag = (id != -1 ? true : false);
    // database.setTransactionSuccessful();
    // } catch (Exception e) {
    // e.printStackTrace();
    // } finally {
    // if (database != null) {
    // database.endTransaction();
    // database.close();
    // }
    // }
    // return flag;
    // }

    @Override
    public boolean deleteCache(String whereClause, String[] whereArgs) {
        boolean flag = false;
        SQLiteDatabase database = null;
        int count = 0;
        try {
            database = helper.getWritableDatabase();
            database.beginTransaction();
            count = database.delete(SQLHelper.TABLE_CHANNEL, whereClause, whereArgs);
            flag = (count > 0 ? true : false);
            database.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return flag;
    }

    @Override
    public boolean updateCache(ContentValues values, String whereClause,
            String[] whereArgs) {
        boolean flag = false;
        SQLiteDatabase database = null;
        int count = 0;
        try {
            database = helper.getWritableDatabase();
            count = database.update(SQLHelper.TABLE_CHANNEL, values, whereClause, whereArgs);
            flag = (count > 0 ? true : false);
        } catch (Exception e) {
        } finally {
            if (database != null) {
                database.endTransaction();
                database.close();
            }
        }
        return flag;
    }

    @Override
    public Map<String, String> viewCache(String selection,
            String[] selectionArgs) {
        // TODO Auto-generated method stub
        SQLiteDatabase database = null;
        Cursor cursor = null;
        Map<String, String> map = new HashMap<String, String>();
        try {
            database = helper.getReadableDatabase();
            database.beginTransaction();
            cursor = database.query(true, SQLHelper.TABLE_CHANNEL, null, selection,
                    selectionArgs, null, null, null, null);
            int cols_len = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                for (int i = 0; i < cols_len; i++) {
                    String cols_name = cursor.getColumnName(i);
                    String cols_values = cursor.getString(cursor
                            .getColumnIndex(cols_name));
                    if (cols_values == null) {
                        cols_values = "";
                    }
                    map.put(cols_name, cols_values);
                }
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            if (database != null) {
                database.endTransaction();
                cursor.close();
                database.close();
            }
        }
        return map;
    }

    /**
     * 获取表ChannelItem 中的数据
     * 查询的数据封装到List中，每条数据用map（key，value）表示
     */
    @Override
    public List<Map<String, String>> listCache(String selection, String[] selectionArgs) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = helper.getReadableDatabase();
            database.beginTransaction();
            cursor = database.query(false, SQLHelper.TABLE_CHANNEL, null, selection, selectionArgs,
                    null, null, null, null);
            int cols_len = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < cols_len; i++) {

                    String cols_name = cursor.getColumnName(i);
                    String cols_values = cursor.getString(cursor
                            .getColumnIndex(cols_name));
                    if (cols_values == null) {
                        cols_values = "";
                    }
                    map.put(cols_name, cols_values);
                }
                list.add(map);
            }

            database.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            if (database != null) {
                database.endTransaction();
                cursor.close();
                database.close();
            }
        }
        return list;
    }

    /***
     * 清除所有频道
     */
    @Override
    public void clearFeedTable() {
        String sql = "DELETE FROM " + SQLHelper.TABLE_CHANNEL + ";";
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(sql);
        revertSeq();
    }

    /**
     * 删除表的数据后，更新下序列 ，这个有待考证
     */
    private void revertSeq() {
        String sql = "update sqlite_sequence set seq=0 where name='"
                + SQLHelper.TABLE_CHANNEL + "'";
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(sql);
    }

}
