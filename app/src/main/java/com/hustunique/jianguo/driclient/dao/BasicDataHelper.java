package com.hustunique.jianguo.driclient.dao;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by JianGuo on 4/1/16.
 * Skeleton class for data access
 */
public abstract class BasicDataHelper {
    private Context mContext;
    private ContentProvider mContentProvider;

    public BasicDataHelper(Context ctx) {
        mContext = ctx;
    }

    protected abstract Uri getContentUri();

    public void notifyChange() {
        mContext.getContentResolver().notifyChange(getContentUri(), null);
    }

    protected final Cursor query(Uri uri, String[] projection, String selection,
                                 String[] selectionArgs, String sortOrder) {
        return mContext.getContentResolver().query(uri, projection, selection, selectionArgs,
                sortOrder);
    }

    protected final Cursor query(String[] projection, String selection, String[] selectionArgs,
                                 String sortOrder) {
        return mContext.getContentResolver().query(getContentUri(), projection, selection,
                selectionArgs, sortOrder);
    }

    protected final Uri insert(ContentValues values) {
        return mContext.getContentResolver().insert(getContentUri(), values);
    }

    protected final int bulkInsert(ContentValues[] values) {
        return mContext.getContentResolver().bulkInsert(getContentUri(), values);
    }

    protected final int update(ContentValues values, String where, String[] whereArgs) {
        return mContext.getContentResolver().update(getContentUri(), values, where, whereArgs);
    }

    protected final int delete(Uri uri, String selection, String[] selectionArgs) {
        return mContext.getContentResolver().delete(getContentUri(), selection, selectionArgs);
    }

    protected final Cursor getList(String[] projection, String selection, String[] selectionArgs,
                                   String sortOrder) {
        return mContext.getContentResolver().query(getContentUri(), projection, selection,
                selectionArgs, sortOrder);
    }

    /**
     * Get basic cursorloader
     * @return the {@link CursorLoader}
     */
    public CursorLoader getCursorLoader() {
        return getCursorLoader(mContext, null, null, null, null);
    }

    protected final CursorLoader getCursorLoader(Context context, String[] projection,
                                                 String selection, String[] selectionArgs, String sortOrder) {
        return new CursorLoader(context, getContentUri(), projection, selection, selectionArgs,
                sortOrder);
    }

}
