/*
 * This file is generated by jOOQ.
 */
package jp.co.toshiba.ppocph.jooq.tables.records;


import jp.co.toshiba.ppocph.jooq.tables.RoleAuth;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RoleAuthRecord extends UpdatableRecordImpl<RoleAuthRecord> implements Record2<Long, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.role_auth.role_id</code>. 役割ID
     */
    public void setRoleId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.role_auth.role_id</code>. 役割ID
     */
    public Long getRoleId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.role_auth.auth_id</code>. 権限ID
     */
    public void setAuthId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.role_auth.auth_id</code>. 権限ID
     */
    public Long getAuthId() {
        return (Long) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<Long, Long> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<Long, Long> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<Long, Long> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return RoleAuth.ROLE_AUTH.ROLE_ID;
    }

    @Override
    public Field<Long> field2() {
        return RoleAuth.ROLE_AUTH.AUTH_ID;
    }

    @Override
    public Long component1() {
        return getRoleId();
    }

    @Override
    public Long component2() {
        return getAuthId();
    }

    @Override
    public Long value1() {
        return getRoleId();
    }

    @Override
    public Long value2() {
        return getAuthId();
    }

    @Override
    public RoleAuthRecord value1(Long value) {
        setRoleId(value);
        return this;
    }

    @Override
    public RoleAuthRecord value2(Long value) {
        setAuthId(value);
        return this;
    }

    @Override
    public RoleAuthRecord values(Long value1, Long value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RoleAuthRecord
     */
    public RoleAuthRecord() {
        super(RoleAuth.ROLE_AUTH);
    }

    /**
     * Create a detached, initialised RoleAuthRecord
     */
    public RoleAuthRecord(Long roleId, Long authId) {
        super(RoleAuth.ROLE_AUTH);

        setRoleId(roleId);
        setAuthId(authId);
    }
}
