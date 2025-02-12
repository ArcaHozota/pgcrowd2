/*
 * This file is generated by jOOQ.
 */
package jp.co.toshiba.ppocph.jooq.tables;


import java.util.Arrays;
import java.util.List;

import jp.co.toshiba.ppocph.jooq.Keys;
import jp.co.toshiba.ppocph.jooq.Public;
import jp.co.toshiba.ppocph.jooq.tables.records.AuthoritiesRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row4;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * 権限テーブル
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Authorities extends TableImpl<AuthoritiesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.authorities</code>
     */
    public static final Authorities AUTHORITIES = new Authorities();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AuthoritiesRecord> getRecordType() {
        return AuthoritiesRecord.class;
    }

    /**
     * The column <code>public.authorities.id</code>. ID
     */
    public final TableField<AuthoritiesRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false), this, "ID");

    /**
     * The column <code>public.authorities.name</code>. 権限名称
     */
    public final TableField<AuthoritiesRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(50).nullable(false), this, "権限名称");

    /**
     * The column <code>public.authorities.title</code>. 漢字名称
     */
    public final TableField<AuthoritiesRecord, String> TITLE = createField(DSL.name("title"), SQLDataType.VARCHAR(50).nullable(false), this, "漢字名称");

    /**
     * The column <code>public.authorities.category_id</code>. 分類ID
     */
    public final TableField<AuthoritiesRecord, Long> CATEGORY_ID = createField(DSL.name("category_id"), SQLDataType.BIGINT, this, "分類ID");

    private Authorities(Name alias, Table<AuthoritiesRecord> aliased) {
        this(alias, aliased, null);
    }

    private Authorities(Name alias, Table<AuthoritiesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("権限テーブル"), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.authorities</code> table reference
     */
    public Authorities(String alias) {
        this(DSL.name(alias), AUTHORITIES);
    }

    /**
     * Create an aliased <code>public.authorities</code> table reference
     */
    public Authorities(Name alias) {
        this(alias, AUTHORITIES);
    }

    /**
     * Create a <code>public.authorities</code> table reference
     */
    public Authorities() {
        this(DSL.name("authorities"), null);
    }

    public <O extends Record> Authorities(Table<O> child, ForeignKey<O, AuthoritiesRecord> key) {
        super(child, key, AUTHORITIES);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public UniqueKey<AuthoritiesRecord> getPrimaryKey() {
        return Keys.AUTH_PKEY;
    }

    @Override
    public List<UniqueKey<AuthoritiesRecord>> getKeys() {
        return Arrays.<UniqueKey<AuthoritiesRecord>>asList(Keys.AUTH_PKEY, Keys.AUTH_NAME_UNIQUE, Keys.AUTH_TITLE_UNIQUE);
    }

    @Override
    public Authorities as(String alias) {
        return new Authorities(DSL.name(alias), this);
    }

    @Override
    public Authorities as(Name alias) {
        return new Authorities(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Authorities rename(String name) {
        return new Authorities(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Authorities rename(Name name) {
        return new Authorities(name, null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<Long, String, String, Long> fieldsRow() {
        return (Row4) super.fieldsRow();
    }
}
