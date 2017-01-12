package com.wearezeta.auto.web.pages;

import java.util.List;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CollectionPage extends WebPage {

    @FindBy(css = WebAppLocators.CollectionPage.cssNoItemsPlaceholder)
    private WebElement noItemsPlaceholder;

    @FindBy(css = WebAppLocators.CollectionPage.cssPictures)
    private List<WebElement> pictures;

    @FindBy(css = WebAppLocators.CollectionPage.cssPictureCollectionSize)
    private WebElement pictureCollectionSize;

    @FindBy(css = WebAppLocators.CollectionPage.cssVideos)
    private List<WebElement> videos;

    @FindBy(css = WebAppLocators.CollectionPage.cssVideoCollectionSize)
    private WebElement videoCollectionSize;

    @FindBy(css = WebAppLocators.CollectionPage.cssFiles)
    private List<WebElement> files;

    @FindBy(css = WebAppLocators.CollectionPage.cssFileCollectionSize)
    private WebElement fileCollectionSize;

    @FindBy(css = WebAppLocators.CollectionPage.cssLinks)
    private List<WebElement> links;

    @FindBy(css = WebAppLocators.CollectionPage.cssLinkCollectionSize)
    private WebElement linkCollectionSize;

    public CollectionPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public String getNoItemsPlaceholder() {
        return noItemsPlaceholder.getText();
    }

    public int getNumberOfPictures() {
        return pictures.size();
    }

    public String getLabelOfPictureCollectionSize() {
        return pictureCollectionSize.getText();
    }

    public int getNumberOfVideos() {
        return videos.size();
    }

    public String getLabelOfVideoCollectionSize() {
        return videoCollectionSize.getText();
    }

    public int getNumberOfFiles() {
        return files.size();
    }

    public String getLabelOfFileCollectionSize() {
        return fileCollectionSize.getText();
    }

    public int getNumberOfLinks() {
        return links.size();
    }

    public String getLabelOfLinkCollectionSize() {
        return linkCollectionSize.getText();
    }
}
