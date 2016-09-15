package com.wearezeta.auto.osx.pages.osx;

import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

import java.util.concurrent.Future;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.apache.log4j.Logger;

public class ForeignMessageContextMenuPage extends OSXPage {

    public static final Logger LOG = ZetaLogger.getLog(ForeignMessageContextMenuPage.class.getName());

    private static final int CONTEXT_LIKE_INDEX = 1;
    private static final int CONTEXT_DELETE_INDEX = 2;

    // TODO hide behind driver impl
    private final Robot robot = new Robot();

    public ForeignMessageContextMenuPage(Future<ZetaOSXDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    /**
     * It's not possible to locate elements inside of the context menu thus we have to implement it this way :/
     *
     * @throws Exception
     */
    public void clickDelete() throws Exception {
        selectByIndex(CONTEXT_DELETE_INDEX, 2000);
    }
    
    public void clickLike() throws Exception {
        selectByIndex(CONTEXT_LIKE_INDEX, 2000);
    }
    
    private void selectByIndex(int index, long wait) throws InterruptedException {
        Thread.sleep(wait);
        for (int i = 0; i < index; i++) {
            robot.keyPress(KeyEvent.VK_DOWN);
            Thread.sleep(wait);
        }
        robot.keyPress(KeyEvent.VK_ENTER);
    }
}