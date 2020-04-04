/**
 * Global variables
 */

package com.news.cqunews;
import org.json.JSONArray;
import java.util.Map;

public class GetGlobals {
    public static JSONArray NEWS_ARRAY;// almost current news list,only changed when updated and translated
    public static String CUR_LABEL;// current label
    public static Map<String,JSONArray> ZH_NEWS;// news list in chinese, like a cache
    public static Map<String,JSONArray> EN_NEWS;// news list in english, like a cache
    public static Map<String,String> LANG;// current language
    //if the news list is updated, it decides whether the EN_NEWS should be updated——false: yes
    public static Map<String, Boolean> PAGE_INIT;

}
