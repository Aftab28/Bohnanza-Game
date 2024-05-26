import java.util.*;
public enum BeanCardType {
	
	STINK_BEAN("Stink Bean", new HashMap<Integer, Integer>() {{
        put(1, 1); put(2, 3); put(3, 6); put(4, 10); 
        put(5, 15); put(6, 21); put(7, 28); put(8, 36);
    }}),
    BLUE_BEAN("Blue Bean", new HashMap<Integer, Integer>() {{ 
        put(1, 1); put(2, 3); put(3, 6); put(4, 10);
        put(5, 15); put(6, 21); put(7, 28); put(8, 36);
    }}),
    CHILI_BEAN("Chili Bean", new HashMap<Integer, Integer>() {{ 
        put(1, 1); put(2, 3); put(3, 6); put(4, 10);
        put(5, 15); put(6, 21); put(7, 28); put(8, 36);
    }}),
    GREEN_BEAN("Green Bean", new HashMap<Integer, Integer>() {{ 
        put(1, 1); put(2, 3); put(3, 6); put(4, 9); 
        put(5, 13); put(6, 17); put(7, 22); put(8, 28);
    }}),
    SOY_BEAN("Soy Bean", new HashMap<Integer, Integer>() {{ 
        put(1, 1); put(2, 3); put(3, 6); put(4, 9); 
        put(5, 13); put(6, 17); put(7, 22); put(8, 28);
    }}),
    BLACK_EYED_BEAN("Black-Eyed Bean", new HashMap<Integer, Integer>() {{ 
        put(1, 1); put(2, 3); put(3, 5); put(4, 8);
        put(5, 11); put(6, 14); put(7, 18); 
    }}),
    RED_BEAN("Red Bean", new HashMap<Integer, Integer>() {{ 
        put(1, 1); put(2, 3); put(3, 5); put(4, 8);
        put(5, 11); put(6, 14); put(7, 18);
    }}),
    WAX_BEAN("Wax Bean", new HashMap<Integer, Integer>() {{ 
        put(1, 1); put(2, 2); put(3, 3); put(4, 4); 
        put(5, 5); put(6, 6); put(7, 7); put(8, 8);
    }}),
    GARDEN_BEAN("Garden Bean", new HashMap<Integer, Integer>() {{ 
        put(1, 0); put(2, 0); put(3, 1); put(4, 1); 
        put(5, 2); put(6, 2); put(7, 3); put(8, 3);
    }}),
    COCOA_BEAN("Cocoa Bean", new HashMap<Integer, Integer>() {{ 
        put(1, 0); put(2, 1); put(3, 1); put(4, 2); 
        put(5, 2); put(6, 3); put(7, 3); put(8, 4);
    }}),
    COFFEE_BEAN("Coffee Bean", new HashMap<Integer, Integer>() {{ 
        put(1, 1); put(2, 2); put(3, 3); put(4, 4); 
        put(5, 5); put(6, 6); put(7, 7); put(8, 8);
    }});

	private static final long serialVersionUID = 1L;
    private final String name;
    private final Map<Integer, Integer> scoringRules;

    BeanCardType(String name, Map<Integer, Integer> scoringRules) {
        this.name = name;
        this.scoringRules = scoringRules;
    }

    public String getName() {
        return name;
    }

    public int calculateScore(int numBeans) {
        return scoringRules.getOrDefault(numBeans, 0); // Default to 0 if not found
    }

}
