package de.pabulaner.filencrypt;

public interface ExceptionSupplier<T> {

    T get() throws Exception;
}
