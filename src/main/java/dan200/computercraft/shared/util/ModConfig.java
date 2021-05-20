package dan200.computercraft.shared.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dan200.computercraft.api.turtle.event.TurtleAction;
import dan200.computercraft.core.apis.http.options.Action;
import dan200.computercraft.core.apis.http.options.AddressRule;
import dan200.computercraft.shared.peripheral.monitor.MonitorRenderer;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "computercraft")
public class ModConfig implements ConfigData {

    public MonitorRenderer monitor_renderer = MonitorRenderer.BEST;
    public int computer_space_limit = 1000 * 1000;
    public int floppy_space_limit = 125 * 1000;
    public int maximum_files_open = 128;
    public boolean disable_lua51_features = false;
    public String default_computer_settings = "";
    public boolean debug_enable = true;
    public boolean log_peripheral_errors = false;
    public boolean command_require_creative = false;
    
    @ConfigEntry.Gui.CollapsibleObject
    public Execution execution = new Execution();
    @ConfigEntry.Gui.CollapsibleObject
    public HTTP http = new HTTP();
    @ConfigEntry.Gui.CollapsibleObject
    public Peripheral peripheral = new Peripheral();
    @ConfigEntry.Gui.CollapsibleObject
    public Turtle turtle = new Turtle();
    @ConfigEntry.Gui.CollapsibleObject
    public TermSizesSection term_sizes = new TermSizesSection();

    public static class Execution {
        public int computer_threads = 1;
        public long max_main_global_time = TimeUnit.MILLISECONDS.toNanos(10);
        public long max_main_computer_time = TimeUnit.MILLISECONDS.toNanos(5);
    }
    
    public static class HTTP {
        public boolean enabled = true;
        public boolean websocket_enable = true;
        public int max_requests = 16;
        public int max_websockets = 4;
        public List<ArrayList<String>> rules = new ArrayList<ArrayList<String>>();
        // public List<List<String>> rules = Arrays.asList(Arrays.asList("*", "$deny"));
        // public List<AddressRule> rules = Collections.unmodifiableList( Arrays.asList(
        //     AddressRule.parse( "$private", null, Action.DENY.toPartial() ),
        //     AddressRule.parse( "*", null, Action.ALLOW.toPartial() )
        // ) );
    }
    
    public static class Peripheral {
        public boolean command_enabled = false;
        public int modem_range = 64;
        public int modem_high_altitude_range = 384;
        public int modem_range_during_storm = 64;
        public int modem_high_altitude_range_during_storm = 384;
        public int max_notes_per_tick = 8;
        public double monitor_distance = 64;
    }
    
    public static class Turtle {
        public boolean need_fuel = true;
        public int fuel_limit = 20000;
        public int advanced_turtle_fuel_limit = 100000;
        public boolean obey_block_protection = true;
        public boolean can_push = true;
        public List<String> disabled_actions = new ArrayList<String>();
        // public EnumSet<TurtleAction> disabled_actions = EnumSet.noneOf(TurtleAction.class);
    }
    public static class TermSizesSection {
        @ConfigEntry.Gui.CollapsibleObject
        public TermSizes computer = new TermSizes(51, 19);
        @ConfigEntry.Gui.CollapsibleObject
        public TermSizes pocket_computer = new TermSizes(26, 20);
        @ConfigEntry.Gui.CollapsibleObject
        public TermSizes monitor = new TermSizes(8, 6);

        public static class TermSizes {
            public int width;
            public int height;
            public TermSizes(int width, int height) {
                this.width = width;
                this.height = height;
            }
        }
    }
}