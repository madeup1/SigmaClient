package git.sigmaclient.settings;

import com.google.gson.annotations.*;
import java.util.function.*;

public class Setting
{
    @Expose
    @SerializedName("name")
    public String name;
    public String category = "";
    private Predicate<Boolean> predicate;

    protected Setting(final String name, final Predicate<Boolean> predicate) {
        this.name = name;
        this.predicate = predicate;
    }

    protected Setting(final String name, final Predicate<Boolean> predicate, String category) {
        this.name = name;
        this.predicate = predicate;
        this.category = category;
    }

    protected Setting(final String name) {
        this(name, null);
    }

    public boolean isHidden() {
        return this.predicate != null && this.predicate.test(true);
    }
}
