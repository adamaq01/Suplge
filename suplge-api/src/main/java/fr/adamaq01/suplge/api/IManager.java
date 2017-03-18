package fr.adamaq01.suplge.api;

/**
 * Created by Adamaq01 on 18/03/2017.
 */
public interface IManager<T extends Manageable> {

    public void add(T manageable);

    public T get(int index);

    public void remove(T manageable);

    public void handle();

}
