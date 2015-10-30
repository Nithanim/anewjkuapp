package me.nithanim.mensaapi.converter;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.nithanim.mensaapi.Meal;
import me.nithanim.mensaapi.MensaApiResult;
import me.nithanim.mensaapi.Menu;
import me.nithanim.mensaapi.Type;

public class JsonToPojoConverter {

    public MensaApiResult convert(String json) {
        try {
            JSONObject jsonData = new JSONObject(json);
            boolean success = jsonData.getBoolean("success");
            if (!success) {
                String cause = jsonData.getString("cause");
                return new MensaApiResult(success, cause, null);
            } else {
                Map<Type, List<Menu>> result = parseResult(jsonData.getJSONObject("result"));
                return new MensaApiResult(success, null, result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<Type, List<Menu>> parseResult(JSONObject jo) throws JSONException {
        EnumMap<Type, List<Menu>> map = new EnumMap<>(Type.class);

        for (Iterator<String> it = jo.keys(); it.hasNext(); ) {
            String key = it.next();
            Type type = Type.valueOf(key);
            List<Menu> menus = parseMenus(jo.getJSONArray(key));
            map.put(type, menus);
        }
        return map;
    }

    private List<Menu> parseMenus(JSONArray ja) throws JSONException {
        List<Menu> menus = new ArrayList<>();
        for (int i = 0; i < ja.length(); i++) {
            menus.add(parseMenu(ja.getJSONObject(i)));
        }
        return menus;
    }

    private Menu parseMenu(JSONObject jo) throws JSONException {
        Type type = Type.valueOf(jo.getString("type"));
        String subtype = jo.getString("subtype");
        List<Meal> meals = parseMeals(jo.getJSONArray("meals"));
        int price = jo.getInt("price");
        int oehBonus;
        try {
            oehBonus = jo.getInt("oehBonus");
        } catch (JSONException ex) {
            oehBonus = -1;
        }
        String rawdate = jo.getString("date");
        boolean vegetarian;
        try {
            vegetarian = jo.getBoolean("vegetarian");
        } catch (JSONException ex) {
            vegetarian = false;
        }

        DateTimeFormatter fmt = ISODateTimeFormat.date();
        LocalDate date = fmt.parseLocalDate(rawdate);

        return new Menu(type, subtype, meals, price, oehBonus, date, vegetarian);
    }

    private List<Meal> parseMeals(JSONArray ja) throws JSONException {
        List<Meal> meals = new ArrayList<>();
        for (int i = 0; i < ja.length(); i++) {
            meals.add(parseMeal(ja.getJSONObject(i)));
        }
        return meals;
    }

    private Meal parseMeal(JSONObject jo) throws JSONException {
        String desc = jo.getString("desc");
        int price = jo.getInt("price");
        return new Meal(desc, price);
    }
}
