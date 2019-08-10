package coffee.keenan.network.helpers;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class ErrorTracking
{
    private final HashMap<String, List<Exception>> exceptions = new HashMap<>();

    /**
     * Adds an exception to the list at the given key
     *
     * @param key String representing the entity throwing the error
     * @param ex  Exception representing the error
     */
    public void addException(@NotNull final String key, @NotNull final Exception ex)
    {
        if (!exceptions.containsKey(key))
            exceptions.put(key, new ArrayList<>());
        exceptions.get(key).add(ex);
    }

    /**
     * Formats all logged errors into an array of strings, one string for each error prefixed by the key
     *
     * @return array of errors
     */
    public String[] getExceptions()
    {
        final List<String> ret = new ArrayList<>();
        exceptions.forEach((key, list) -> list.forEach(l -> ret.add("[" + key + "] " + l.getMessage())));
        return ret.toArray(new String[]{});
    }
}
