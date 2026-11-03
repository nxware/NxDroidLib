package net.bsnx.jlib.collections;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ConsumerList<T> extends ArrayList<Consumer<T>> implements Consumer<T> {

    @Override
    public void accept(T t) {
        for (Consumer<T> listener : this) {
            listener.accept(t);
        }
    }
}
