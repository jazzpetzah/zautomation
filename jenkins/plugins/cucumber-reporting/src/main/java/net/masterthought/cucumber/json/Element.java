package net.masterthought.cucumber.json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import net.masterthought.cucumber.ConfigurationOptions;
import net.masterthought.cucumber.util.Util;
import org.apache.commons.lang.StringUtils;

import static com.googlecode.totallylazy.Option.option;

public class Element {

    private String name;
    private String description;
    private String keyword;
    private Step[] steps;
    private Tag[] tags;

    public Element() {

    }

    public Sequence<Step> getSteps() {
        return getSteps(false, 0);
    }

	public Sequence<Step> getSteps(Boolean update, int index) {
        if (update) {
            LinkedHashMap<String, Integer> stepNames = new LinkedHashMap<String, Integer>();
            for (Step step : steps) {
                String oldName = step.getRawName();
                Integer count = stepNames.get(oldName);
                if (count == null) {
                    count = 0;
                }
                if (count > 0) {
                    for (Step step1 : steps) {
                        for (int i = count; i > 0; i--) {
                            if (step1.getRawName().equals(oldName + " " + i)) {
                                step1.setName(oldName + " " + (i + index));
                                break;
                            }
                        }
                    }
                }
                count = count + index;
                step.setName(oldName + " " + (++count));
                stepNames.put(oldName, count);
            }
        }
        return Sequences.sequence(option(steps).getOrElse(new Step[]{})).realise();
    }

    public Sequence<Tag> getTags() {
        return Sequences.sequence(option(tags).getOrElse(new Tag[]{})).realise();
    }

    public Util.Status getStatus() {
    	// can be optimized to retrieve only the count of elements and not the all list
        int failedResults = getSteps().filter(Step.predicates.hasStatus(Util.Status.FAILED)).size();
        int passedResults = getSteps().filter(Step.predicates.hasStatus(Util.Status.PASSED)).size();
        
        if (failedResults == 0 && ConfigurationOptions.skippedFailsBuild()) {
            failedResults = getSteps().filter(Step.predicates.hasStatus(Util.Status.SKIPPED)).size();
        }

        if (failedResults == 0 && ConfigurationOptions.undefinedFailsBuild()) {
            failedResults = getSteps().filter(Step.predicates.hasStatus(Util.Status.UNDEFINED)).size();
        }
        
        return (failedResults == 0) && (passedResults != 0) ? Util.Status.PASSED : Util.Status.FAILED;
    }

    public String getRawName() {
        return name;
    }

    public String getDescription() {
        String result = "";
        if (Util.itemExists(description)) {
            String content = description.replaceAll("\n", "<br/>");
            result = "<span class=\"step-keyword\">Runner </span><span class=\"scenario-description\">" + content  +
                    "</span>";
        }
        return Util.itemExists(description) ?  Util.result(getStatus()) + result + Util.closeDiv() : "";
    }

    public String getKeyword() {
        return keyword;
    }

    public String getName() {
        List<String> contentString = new ArrayList<String>();

        if (Util.itemExists(keyword)) {
            contentString.add("<span class=\"scenario-keyword\">" + keyword + ": </span>");
        }

        if (Util.itemExists(name)) {
            contentString.add("<span class=\"scenario-name\">" + name + "</span>");
        }

        return Util.itemExists(contentString) ? Util.result(getStatus()) + StringUtils.join(contentString.toArray(), " ") + Util.closeDiv() : "";
    }

    public Sequence<String> getTagList() {
        return processTags();
    }

    public boolean hasTags() {
        return Util.itemExists(tags);
    }

    private Sequence<String> processTags() {
        return getTags().map(Tag.functions.getName());
    }

    public String getTagsList() {
        String result = "<div class=\"feature-tags\"></div>";
        if (Util.itemExists(tags)) {
            String tagList = StringUtils.join(processTags().toList().toArray(), ",");
            result = "<div class=\"feature-tags\">" + tagList + "</div>";
        }
        return result;
    }

    public static class functions {
        public static Function1<Element, Util.Status> status() {
            return new Function1<Element, Util.Status>() {
                @Override
                public Util.Status call(Element element) throws Exception {
                    return element.getStatus();
                }
            };
        }
    }

}
