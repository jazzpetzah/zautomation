package com.wearezeta.auto.common.misc;

import java.awt.image.BufferedImage;

public interface FunctionalInterfaces {

    @FunctionalInterface
    interface FunctionFor2Parameters<A, B, C> {
        A apply(B b, C c);
    }

    @FunctionalInterface
    interface FunctionFor3Parameters<A, B, C, D> {
        A apply(B b, C c, D d);
    }

    @FunctionalInterface
    interface StateGetter {
        BufferedImage getState() throws Exception;
    }

    @FunctionalInterface
    interface ISupplierWithException<T> {
        T call() throws Exception;
    }

    @FunctionalInterface
    interface RunnableWithException {
        void run() throws Exception;
    }
}
