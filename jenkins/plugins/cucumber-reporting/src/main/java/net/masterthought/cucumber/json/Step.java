package net.masterthought.cucumber.json;

import java.util.List;
import java.util.UUID;

import com.google.common.base.Joiner;
import com.google.gson.internal.LinkedTreeMap;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import net.masterthought.cucumber.util.Util;

import static com.googlecode.totallylazy.Option.option;
import static org.apache.commons.lang.StringUtils.EMPTY;

public class Step {

    private String name;
    private Integer index;
    private String keyword;
    private String line;
    private Result result;
    private Row[] rows;
    private Match match;
    private Object[] embeddings;
    private String[] output;
    private DocString doc_string;
    private static final String NO_IMAGE_URL="http://www.kalmbachfeeds.com/shopping/App_Themes/Kalmbach/Images/no-image-available.png";
    private static int screenshotIndex = 0;

    public void increaseIndex() {
        screenshotIndex++;
    }

    public Step() {
    }

    public DocString getDocString() {
        return doc_string;
    }

    public Row[] getRows() {
        return rows;
    }

    public String getOutput() {
        List<String> outputList = Sequences.sequence(option(output).getOrElse(new String[]{})).realise().toList();
        return Joiner.on("").skipNulls().join(outputList);
    }

    public Match getMatch() {
        return match;
    }

    public Object[] getEmbeddings() {
        return embeddings;
    }

    public boolean hasRows() {
        boolean result = false;
        if (rows != null) {
            if (rows.length > 0) {
                result = true;
            }
        }
        return result;
    }

    /**
     * @return - Returns true if has a sub doc-string, and that doc-string has a value
     */
    public boolean hasDocString() {
        return doc_string != null && doc_string.hasValue();
    }

    public Util.Status getStatus() {
        if (result == null) {
            System.out.println("[WARNING] Line " + line + " : " + "Step is missing Result: " + keyword + " : " + name);
            return Util.Status.MISSING;
        } else {
            return Util.resultMap.get(result.getStatus());
        }
    }

    public Long getDuration() {
        if (result == null) {
            return 1L;
        } else {
            return result.getDuration();
        }
    }

    public String getDataTableClass() {
        String content = "";
        Util.Status status = getStatus();
        if (status == Util.Status.FAILED) {
            content = "failed";
        } else if (status == Util.Status.PASSED) {
            content = "passed";
        } else if (status == Util.Status.SKIPPED) {
            content = "skipped";
        } else {
            content = "";
        }
        return content;
    }

    public String getRawName() {
        return name;
    }

    public Integer getIndex() {
        return this.index;
    }

    public void setIndex(int index) { this.index = index; }

    public String getName() {
        String content = "";
        if (getStatus() == Util.Status.FAILED) {
            String errorMessage = result.getErrorMessage();
            boolean doShowScreenshot = true;
            if (getStatus() == Util.Status.SKIPPED) {
                errorMessage = "Mode: Skipped causes Failure<br/><span class=\"skipped\">This step was skipped</span>";
                doShowScreenshot = false;
            }
            if (getStatus() == Util.Status.UNDEFINED) {
                errorMessage = "Mode: Not Implemented causes Failure<br/><span class=\"undefined\">This step is not yet implemented</span>";
                doShowScreenshot = false;
            }
            content =
            		Util.result(getStatus())
            			+ "<span class=\"step-keyword\">" + keyword + " </span>"
            			+ "<span class=\"step-name\">" + name + "</span>"
            			+ (doShowScreenshot?"<a class=\"step-show-screenshot\" onclick=\"javascript:showHide('scr " + getRawName().replaceAll("\\W+", "_") + "" + screenshotIndex + "', this);\">Show Screenshot</a>":"")
            			+ "<span class=\"step-duration\">" + Util.formatDuration(result.getDuration()) + "</span>"
            			+ "<div class=\"step-error-message\"><pre>" + formatError(errorMessage) + "</pre></div>";
            content += Util.closeDiv() + getImageTags();
        } else if (getStatus() == Util.Status.MISSING) {
            String errorMessage = "<span class=\"missing\">Result was missing for this step</span>";
            content = Util.result(getStatus()) + "<span class=\"step-keyword\">" + keyword + " </span><span class=\"step-name\">" + name + "</span><span class=\"step-duration\"></span><div class=\"step-error-message\"><pre>" + formatError(errorMessage) + "</pre></div>" + Util.closeDiv();
        } else {
            content = getNameAndDuration();
        }
        return content.replace(name, getFinalName(name));
    }

    public String getName(String feature, String scenario) {
        increaseIndex();
        String content = "";
        if (getStatus() == Util.Status.FAILED) {
            String errorMessage = result.getErrorMessage();
            boolean doShowScreenshot = true;
            if (getStatus() == Util.Status.SKIPPED) {
                errorMessage = "Mode: Skipped causes Failure<br/><span class=\"skipped\">This step was skipped</span>";
                doShowScreenshot = false;
            }
            if (getStatus() == Util.Status.UNDEFINED) {
                errorMessage = "Mode: Not Implemented causes Failure<br/><span class=\"undefined\">This step is not yet implemented</span>";
                doShowScreenshot = false;
            }
            content =
            		Util.result(getStatus())
            			+ "<span class=\"step-keyword\">" + keyword + " </span>"
            			+ "<span class=\"step-name\">" + name + "</span>"
            			+ (doShowScreenshot?"<a class=\"step-show-screenshot\" onclick=\"javascript:showHide('scr " + getRawName().replaceAll("\\W+", "_") + "" + screenshotIndex + "', this);\">Show Screenshot</a>":"")
            			+ "<span class=\"step-duration\">" + Util.formatDuration(result.getDuration()) + "</span>"
            			+ "<div class=\"step-error-message\"><pre>" + formatError(errorMessage) + "</pre>";
            if (doShowScreenshot) {
            	content += addScreenshotTags(feature, scenario);
            }
            content += Util.closeDiv();
            content += Util.closeDiv() + getImageTags();
        } else if (getStatus() == Util.Status.MISSING) {
            String errorMessage = "<span class=\"missing\">Result was missing for this step</span>";
            content = Util.result(getStatus()) + "<span class=\"step-keyword\">" + keyword + " </span><span class=\"step-name\">" + name + "</span><span class=\"step-duration\"></span><div class=\"step-error-message\"><pre>" + formatError(errorMessage) + "</pre></div>" + Util.closeDiv();
        } else {
            content = getNameAndDuration(feature, scenario);
        }
        return content.replace(name, getFinalName(name));
    }
    
    private String getNameAndDuration() {
        String content = Util.result(getStatus())
                + "<span class=\"step-keyword\">" + keyword + "</span>"
        		+ "<span class=\"step-name\">" + name + "</span>"
        		+ "<a class=\"step-show-screenshot\" onclick=\"javascript:showHide('scr " + getRawName().replaceAll("\\W+", "_") + "" + screenshotIndex + "', this);\">Show Screenshot</a>"
                + "<span class=\"step-duration\">" + Util.formatDuration(result.getDuration()) + "</span>"
                + Util.closeDiv() + getImageTags();
        return content.replace(name, getFinalName(name));
    }

    private String getNameAndDuration(String feature, String scenario) {
        String content = Util.result(getStatus())
                + "<span class=\"step-keyword\">" + keyword + "</span>"
        		+ "<span class=\"step-name\">" + name + "</span>"
        		+ "<a class=\"step-show-screenshot\" onclick=\"javascript:showHide('scr " + getRawName().replaceAll("\\W+", "_") + "" + screenshotIndex + "', this);\">Show Screenshot</a>"
                + "<span class=\"step-duration\">" + Util.formatDuration(result.getDuration()) + "</span>"
                + addScreenshotTags(feature, scenario)
                + Util.closeDiv() + getImageTags();
        return content.replace(name, getFinalName(name));
    }
    
    private String addScreenshotTags(String feature, String scenario) {
    	String tags = "";
    	tags += "<div class=\"step-screenshot\" id=\"scr " + getRawName().replaceAll("\\W+", "_") + "" + screenshotIndex + "\"><center>"
    		+ "<a href=\"Images/" + feature.replaceAll("\\W+", "_") + "/" + scenario.replaceAll("\\W+", "_") + "/" + getRawName().replaceAll("\\W+", "_") + ".png\" target=\"_blank\">"
    		+ "<img onerror=\"this.src='" + NO_IMAGE_URL +"';this.width=110;this.height=110\" src=\"Images/" + feature.replaceAll("\\W+", "_") + "/" + scenario.replaceAll("\\W+", "_") + "/" + getRawName().replaceAll("\\W+", "_") + ".png\" width=\"30%\" height=\"30%\" />"
    		+ "</a>"
  	  		+ "</center></div>";
  		return tags;
    }
    /**
     * Returns a formatted doc-string section.
     * This is formatted w.r.t the parent Step element.
     * To preserve whitespace in example, line breaks and whitespace are preserved
     *
     * @return string of html
     */
    public String getDocStringOrNothing() {
        if (!hasDocString()) {
            return "";
        }
        return Util.result(getStatus()) +
                "<div class=\"doc-string\">" +
                getDocString().getEscapedValue() +
                Util.closeDiv() +
                Util.closeDiv();
    }

    private String formatError(String errorMessage) {
        String result = errorMessage;
        if (errorMessage != null && !errorMessage.isEmpty()) {
            result = errorMessage.replaceAll("\\\\n", "<br/>");
        }
        return result;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getImageTags() {
        if (noEmbeddedScreenshots()) return EMPTY;

        String links = EMPTY;
        int index = 1;
        for (Object image : embeddings) {
            if (image != null) {
                String mimeEncodedImage = mimeEncodeEmbededImage(image);
                String imageId = UUID.nameUUIDFromBytes(mimeEncodedImage.getBytes()).toString();
                links = links + "<a href=\"\" onclick=\"img=document.getElementById('" + imageId + "'); img.style.display = (img.style.display == 'none' ? 'block' : 'none');return false\">Screenshot " + index++ + "</a>" +
                        "<img id='" + imageId + "' style='display:none' src='" + mimeEncodedImage + "'>\n";
            }
        }
        return links;
    }

    private boolean noEmbeddedScreenshots() {
        return getEmbeddings() == null;
    }


    public static String mimeEncodeEmbededImage(Object image) {
        return "data:image/png;base64," + ((LinkedTreeMap) image).get("data");

    }

    public static String uuidForImage(Object image) {
        return UUID.nameUUIDFromBytes(mimeEncodeEmbededImage(image).getBytes()).toString();
    }

    public static class functions {
        public static Function1<Step, Util.Status> status() {
            return new Function1<Step, Util.Status>() {
                @Override
                public Util.Status call(Step step) throws Exception {
                    return step.getStatus();
                }
            };
        }
    }

    public static class predicates {

        public static LogicalPredicate<Step> hasStatus(final Util.Status status) {
            return new LogicalPredicate<Step>() {
                @Override
                public boolean matches(Step step) {
                    return step.getStatus().equals(status);
                }
            };
        }


        public static Function1<Step, Util.Status> status() {
            return new Function1<Step, Util.Status>() {
                @Override
                public Util.Status call(Step step) throws Exception {
                    return step.getStatus();
                }
            };
        }
    }
    
    private static String getFinalName (String name) {
    	return (name.substring(name.length()-2, name.length()).matches("\\s[0-9]"))? name.substring(0, name.length()-2) : name;
    }

    @Override
    public String toString(){
        return name;
    }
}
