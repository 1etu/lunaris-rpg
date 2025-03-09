package lunaris.rpg.custom_items.registry;

import lunaris.rpg.custom_items.RPGItem;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ItemRegistry {
    private static final Map<String, RPGItem> ITEMS = new HashMap<>();
    private static final String ITEMS_PACKAGE = "lunaris.rpg.custom_items";

    public static void initialize() {
        // Scan and register all items
        Reflections reflections = new Reflections(ITEMS_PACKAGE, new SubTypesScanner(false));
        
        // Find all Item classes
        for (Class<?> clazz : reflections.getSubTypesOf(Object.class)) {
            if (clazz.getSimpleName().equals("Item")) {
                try {
                    Method getMethod = clazz.getDeclaredMethod("get");
                    RPGItem item = (RPGItem) getMethod.invoke(null);
                    register(item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void register(RPGItem item) {
        ITEMS.put(item.getId().toLowerCase(), item);
    }

    public static RPGItem getItem(String id) {
        return ITEMS.get(id.toLowerCase());
    }

    public static Collection<RPGItem> getAllItems() {
        return Collections.unmodifiableCollection(ITEMS.values());
    }
} 