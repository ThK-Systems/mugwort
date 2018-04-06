/*
 * tksCommons / mugwort
 *
 * Author  : Thomas Kuhlmann (ThK-Systems, http://www.thk-systems.de)
 * License : LGPL (https://www.gnu.org/licenses/lgpl.html)
 */
package de.thksystems.persistence.hibernate;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.ClassUtils;

import de.thksystems.util.bean.ReflectiveBasicBean;
import de.thksystems.util.lang.IgnoreForEquals;

/**
 * Identified entities have an ID, can be compared to each other and have a toString() method.
 */
@MappedSuperclass
public abstract class IdentifiedEntity extends ReflectiveBasicBean {

    private static final long serialVersionUID = 5968828150324034087L;

    @Id
    @GeneratedValue
    @IgnoreForEquals // Ignore for reflective equals
    private long id;

    public long getId() {
        return id;
    }

    protected void setId(long id) {
        this.id = id;
    }

    /**
     * Returns short class name appended with unique business key, e.g. "Order: 00002" or "Customer: nobody@nowhere.bogus".
     */
    public String asBusinessString() {
        return ClassUtils.getShortClassName(this, null) + ": " + getUniqueBusinessKey();
    }

    /**
     * Business key must identify the object for its business, e.g. an order code or a customer number, and must be unique.
     */
    protected String getUniqueBusinessKey() {
        return String.valueOf(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass().equals(this.getClass()) && ((IdentifiedEntity) obj).getId() > 0 && this.getId() > 0) {
            return ((IdentifiedEntity) obj).getId() == this.getId();
        }
        return super.equals(obj);
    }
}