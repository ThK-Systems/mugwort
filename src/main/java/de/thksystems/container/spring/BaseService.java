/*
 * tksCommons / mugwort
 *
 * Author : Thomas Kuhlmann (ThK-Systems, http://oss.thk-systems.de) License : LGPL (https://www.gnu.org/licenses/lgpl.html)
 */
package de.thksystems.container.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import de.thksystems.util.function.CheckedBiFunction;
import de.thksystems.util.function.CheckedConsumer;
import de.thksystems.util.function.CheckedFunction;
import de.thksystems.util.function.CheckedRunnable;
import de.thksystems.util.function.CheckedSupplier;

public abstract class BaseService extends BaseComponent {

    private final static Logger LOG = LoggerFactory.getLogger(BaseService.class);

    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * Start transaction manually.
     * <p>
     * <b>Use it with caution!</b><br>
     * <b>Do not mix annotation based transaction handling and programmatically one!</b><br>
     *
     * @param readonly    For readonly transactions.
     * @param propagation For special propagation (like REQUIRES_NEW)
     */
    protected TransactionStatus startTransaction(boolean readonly, Propagation propagation) {
        LOG.trace("Starting new transaction");
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setReadOnly(readonly);
        transactionDefinition.setPropagationBehavior(propagation.value());
        return transactionManager.getTransaction(transactionDefinition);
    }

    /**
     * Start transaction manually.
     * <p>
     * <b>Use it with caution!</b><br>
     * <b>Do not mix annotation based transaction handling and programmatically one!</b><br>
     *
     * @param readonly For readonly transactions.
     */
    protected TransactionStatus startTransaction(boolean readonly) {
        return startTransaction(readonly, Propagation.REQUIRED);
    }

    /**
     * Start transaction manually.
     * <p>
     * <b>Use it with caution!</b><br>
     * <b>Do not mix annotation based transaction handling and programmatically one!</b><br>
     *
     * @param propagation For special propagation (like REQUIRES_NEW)
     */
    protected TransactionStatus startTransaction(Propagation propagation) {
        return startTransaction(false, propagation);
    }

    /**
     * Start transaction manually.
     * <p>
     * <b>Use it with caution!</b><br>
     * <b>Do not mix annotation based transaction handling and programmatically one!</b><br>
     */
    protected TransactionStatus startTransaction() {
        return startTransaction(false, Propagation.REQUIRED);
    }

    /**
     * Start transaction manually, if required.
     *
     * @see #startTransaction()
     */
    protected TransactionStatus startTransactionIfRequired(TransactionStatus transactionStatus) {
        if(transactionStatus == null || transactionStatus.isCompleted()) {
            return startTransaction();
        }
        return transactionStatus;
    }

    /**
     * Commit transaction manually.
     * <p>
     * If the transaction is set rollbackOnly, a rollback is done.
     *
     * @see #startTransaction() for notes and warning.
     */
    protected void commitTransaction(TransactionStatus transactionStatus) {
        if(transactionStatus != null && !transactionStatus.isCompleted()) {
            if(transactionStatus.isRollbackOnly()) {
                LOG.trace("Transaction is set rollback only.");
                rollbackTransaction(transactionStatus);
            } else {
                LOG.trace("Committing transaction");
                transactionManager.commit(transactionStatus);
                LOG.trace("Transaction committed");
            }
        }
    }

    /**
     * Rollback transaction manually.
     *
     * @see #startTransaction() for notes and warning.
     */
    protected void rollbackTransaction(TransactionStatus transactionStatus) {
        if(transactionStatus != null && !transactionStatus.isCompleted()) {
            LOG.trace("Rolling back transaction.");
            transactionManager.rollback(transactionStatus);
            LOG.trace("Transaction rollback.");
        }
    }

    protected <X extends Throwable> void runInTransaction(CheckedRunnable<X> runnable) throws X {
        runInTransaction(Propagation.REQUIRED, runnable);
    }

    protected <X extends Throwable> void runInTransaction(Propagation propagation, CheckedRunnable<X> runnable) throws X {
        TransactionStatus transactionStatus = null;
        try {
            transactionStatus = startTransaction(propagation);
            runnable.run();
            commitTransaction(transactionStatus);
        } catch (Throwable t) { // NOSONAR
            rollbackTransaction(transactionStatus);
            throw t;
        }
    }

    protected <R, X extends Throwable> R runInTransaction(CheckedSupplier<R, X> supplier) throws X {
        return runInTransaction(Propagation.REQUIRED, supplier);
    }

    protected <R, X extends Throwable> R runInTransaction(Propagation propagation, CheckedSupplier<R, X> supplier) throws X {
        TransactionStatus transactionStatus = null;
        try {
            transactionStatus = startTransaction(propagation);
            R result = supplier.get();
            commitTransaction(transactionStatus);
            return result;
        } catch (Throwable t) { // NOSONAR
            rollbackTransaction(transactionStatus);
            throw t;
        }
    }

    protected <C, X extends Throwable> void runInTransaction(CheckedConsumer<C, X> consumer, C c) throws X {
        runInTransaction(Propagation.REQUIRED, consumer, c);
    }

    protected <C, X extends Throwable> void runInTransaction(Propagation propagation, CheckedConsumer<C, X> consumer, C c) throws X {
        TransactionStatus transactionStatus = null;
        try {
            transactionStatus = startTransaction(propagation);
            consumer.accept(c);
            commitTransaction(transactionStatus);
        } catch (Throwable t) { // NOSONAR
            rollbackTransaction(transactionStatus);
            throw t;
        }
    }

    protected <C, R, X extends Throwable> R runInTransaction(CheckedFunction<C, R, X> function, C c) throws X {
        return runInTransaction(Propagation.REQUIRED, function, c);
    }

    protected <C, R, X extends Throwable> R runInTransaction(Propagation propagation, CheckedFunction<C, R, X> function, C c) throws X {
        TransactionStatus transactionStatus = null;
        try {
            transactionStatus = startTransaction(propagation);
            R result = function.apply(c);
            commitTransaction(transactionStatus);
            return result;
        } catch (Throwable t) { // NOSONAR
            rollbackTransaction(transactionStatus);
            throw t;
        }
    }

    protected <C, D, R, X extends Throwable> R runInTransaction(CheckedBiFunction<C, D, R, X> function, C c, D d) throws X {
        return runInTransaction(Propagation.REQUIRED, function, c, d);
    }

    protected <C, D, R, X extends Throwable> R runInTransaction(Propagation propagation, CheckedBiFunction<C, D, R, X> function, C c, D d) throws X {
        TransactionStatus transactionStatus = null;
        try {
            transactionStatus = startTransaction(propagation);
            R result = function.apply(c, d);
            commitTransaction(transactionStatus);
            return result;
        } catch (Throwable t) { // NOSONAR
            rollbackTransaction(transactionStatus);
            throw t;
        }
    }

}
