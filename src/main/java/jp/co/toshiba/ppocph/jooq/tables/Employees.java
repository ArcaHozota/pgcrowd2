/*
 * This file is generated by jOOQ.
 */
package jp.co.toshiba.ppocph.jooq.tables;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import jp.co.toshiba.ppocph.jooq.Keys;
import jp.co.toshiba.ppocph.jooq.Public;
import jp.co.toshiba.ppocph.jooq.tables.records.EmployeesRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row8;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * 社員テーブル
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Employees extends TableImpl<EmployeesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.employees</code>
     */
    public static final Employees EMPLOYEES = new Employees();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EmployeesRecord> getRecordType() {
        return EmployeesRecord.class;
    }

    /**
     * The column <code>public.employees.id</code>. ID
     */
    public final TableField<EmployeesRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false), this, "ID");

    /**
     * The column <code>public.employees.login_account</code>. アカウント
     */
    public final TableField<EmployeesRecord, String> LOGIN_ACCOUNT = createField(DSL.name("login_account"), SQLDataType.VARCHAR(40).nullable(false), this, "アカウント");

    /**
     * The column <code>public.employees.password</code>. パスワード
     */
    public final TableField<EmployeesRecord, String> PASSWORD = createField(DSL.name("password"), SQLDataType.VARCHAR(120).nullable(false), this, "パスワード");

    /**
     * The column <code>public.employees.username</code>. ユーザ名称
     */
    public final TableField<EmployeesRecord, String> USERNAME = createField(DSL.name("username"), SQLDataType.VARCHAR(50).nullable(false), this, "ユーザ名称");

    /**
     * The column <code>public.employees.email</code>. メール
     */
    public final TableField<EmployeesRecord, String> EMAIL = createField(DSL.name("email"), SQLDataType.VARCHAR(40).nullable(false), this, "メール");

    /**
     * The column <code>public.employees.created_time</code>. 作成時間
     */
    public final TableField<EmployeesRecord, LocalDateTime> CREATED_TIME = createField(DSL.name("created_time"), SQLDataType.LOCALDATETIME(6).nullable(false), this, "作成時間");

    /**
     * The column <code>public.employees.delete_flg</code>. ステータス
     */
    public final TableField<EmployeesRecord, String> DELETE_FLG = createField(DSL.name("delete_flg"), SQLDataType.VARCHAR(8).nullable(false), this, "ステータス");

    /**
     * The column <code>public.employees.date_of_birth</code>. 生年月日
     */
    public final TableField<EmployeesRecord, LocalDate> DATE_OF_BIRTH = createField(DSL.name("date_of_birth"), SQLDataType.LOCALDATE, this, "生年月日");

    private Employees(Name alias, Table<EmployeesRecord> aliased) {
        this(alias, aliased, null);
    }

    private Employees(Name alias, Table<EmployeesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("社員テーブル"), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.employees</code> table reference
     */
    public Employees(String alias) {
        this(DSL.name(alias), EMPLOYEES);
    }

    /**
     * Create an aliased <code>public.employees</code> table reference
     */
    public Employees(Name alias) {
        this(alias, EMPLOYEES);
    }

    /**
     * Create a <code>public.employees</code> table reference
     */
    public Employees() {
        this(DSL.name("employees"), null);
    }

    public <O extends Record> Employees(Table<O> child, ForeignKey<O, EmployeesRecord> key) {
        super(child, key, EMPLOYEES);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public UniqueKey<EmployeesRecord> getPrimaryKey() {
        return Keys.EMPLOYEE_PKEY;
    }

    @Override
    public List<UniqueKey<EmployeesRecord>> getKeys() {
        return Arrays.<UniqueKey<EmployeesRecord>>asList(Keys.EMPLOYEE_PKEY, Keys.LOGIN_ACCOUNT_UNIQUE, Keys.EMAIL_UNIQUE);
    }

    @Override
    public Employees as(String alias) {
        return new Employees(DSL.name(alias), this);
    }

    @Override
    public Employees as(Name alias) {
        return new Employees(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Employees rename(String name) {
        return new Employees(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Employees rename(Name name) {
        return new Employees(name, null);
    }

    // -------------------------------------------------------------------------
    // Row8 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row8<Long, String, String, String, String, LocalDateTime, String, LocalDate> fieldsRow() {
        return (Row8) super.fieldsRow();
    }
}
