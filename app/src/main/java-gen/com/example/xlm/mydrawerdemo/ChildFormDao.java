package com.example.xlm.mydrawerdemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.example.xlm.mydrawerdemo.ChildForm;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CHILD_FORM".
*/
public class ChildFormDao extends AbstractDao<ChildForm, Void> {

    public static final String TABLENAME = "CHILD_FORM";

    /**
     * Properties of entity ChildForm.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", false, "ID");
        public final static Property Fgroup = new Property(1, String.class, "fgroup", false, "FGROUP");
        public final static Property Sort = new Property(2, String.class, "sort", false, "SORT");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
        public final static Property ShowName = new Property(4, String.class, "showName", false, "SHOW_NAME");
        public final static Property Msg = new Property(5, String.class, "msg", false, "MSG");
        public final static Property Interval = new Property(6, String.class, "interval", false, "INTERVAL");
        public final static Property CreateAt = new Property(7, String.class, "createAt", false, "CREATE_AT");
        public final static Property UpdateAt = new Property(8, String.class, "updateAt", false, "UPDATE_AT");
        public final static Property Status = new Property(9, String.class, "status", false, "STATUS");
    };


    public ChildFormDao(DaoConfig config) {
        super(config);
    }
    
    public ChildFormDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CHILD_FORM\" (" + //
                "\"ID\" TEXT NOT NULL ," + // 0: id
                "\"FGROUP\" TEXT," + // 1: fgroup
                "\"SORT\" TEXT," + // 2: sort
                "\"NAME\" TEXT," + // 3: name
                "\"SHOW_NAME\" TEXT," + // 4: showName
                "\"MSG\" TEXT," + // 5: msg
                "\"INTERVAL\" TEXT," + // 6: interval
                "\"CREATE_AT\" TEXT," + // 7: createAt
                "\"UPDATE_AT\" TEXT," + // 8: updateAt
                "\"STATUS\" TEXT);"); // 9: status
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CHILD_FORM\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ChildForm entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getId());
 
        String fgroup = entity.getFgroup();
        if (fgroup != null) {
            stmt.bindString(2, fgroup);
        }
 
        String sort = entity.getSort();
        if (sort != null) {
            stmt.bindString(3, sort);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
 
        String showName = entity.getShowName();
        if (showName != null) {
            stmt.bindString(5, showName);
        }
 
        String msg = entity.getMsg();
        if (msg != null) {
            stmt.bindString(6, msg);
        }
 
        String interval = entity.getInterval();
        if (interval != null) {
            stmt.bindString(7, interval);
        }
 
        String createAt = entity.getCreateAt();
        if (createAt != null) {
            stmt.bindString(8, createAt);
        }
 
        String updateAt = entity.getUpdateAt();
        if (updateAt != null) {
            stmt.bindString(9, updateAt);
        }
 
        String status = entity.getStatus();
        if (status != null) {
            stmt.bindString(10, status);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public ChildForm readEntity(Cursor cursor, int offset) {
        ChildForm entity = new ChildForm( //
            cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // fgroup
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // sort
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // name
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // showName
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // msg
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // interval
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // createAt
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // updateAt
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // status
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ChildForm entity, int offset) {
        entity.setId(cursor.getString(offset + 0));
        entity.setFgroup(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSort(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setShowName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setMsg(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setInterval(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCreateAt(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setUpdateAt(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setStatus(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(ChildForm entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(ChildForm entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
