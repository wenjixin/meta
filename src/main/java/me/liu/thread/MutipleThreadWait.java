package me.liu.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by tongwei on 17/7/28.
 */
public class MutipleThreadWait {


    public static void main(String[] args) {

        testJoin();

    }

    private static void testJoin() {

        List<PrintThread> threads = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            PrintThread printThread = new PrintThread(i);

            threads.add(printThread);

            printThread.start();

        }


        for (PrintThread thread :
                threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}


class PrintThread extends Thread {


    private int id;

    public PrintThread(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        super.run();

        System.out.println(id + " start.");

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(id + " end.");

    }
}