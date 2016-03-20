package wdl.worldguard;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;
import org.mcstats.Metrics.Plotter;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import wdl.RangeGroupTypeRegistrationEvent;
import wdl.range.IRangeProducer;

public class WorldGuardSupportPlugin extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		
		try {
			class StringPlotter extends Plotter {
				public StringPlotter(String str) {
					super(str);
				}
				
				@Override
				public int getValue() {
					return 1;
				}
			}
			
			Metrics metrics = new Metrics(this);
			
			Graph worldGuardVersionGraph = metrics.createGraph("worldGuardVersion");
			String worldGuardVersion = getProvidingPlugin(WorldGuardPlugin.class)
					.getDescription().getFullName();
			worldGuardVersionGraph.addPlotter(new StringPlotter(worldGuardVersion));
			
			Graph worldEditVersionGraph = metrics.createGraph("worldEditVersion");
			String worldEditVersion = getProvidingPlugin(WorldEditPlugin.class)
					.getDescription().getFullName();
			worldEditVersionGraph.addPlotter(new StringPlotter(worldEditVersion));
			
			Graph wdlcVersionGraph = metrics.createGraph("wdlcompanionVersion");
			String wdlcVersion = getProvidingPlugin(IRangeProducer.class)
					.getDescription().getFullName();
			wdlcVersionGraph.addPlotter(new StringPlotter(wdlcVersion));
			
			metrics.start();
		} catch (Exception e) {
			getLogger().warning("Failed to start PluginMetrics :(");
		}
	}
	
	@EventHandler
	public void registerRangeGroupTypes(RangeGroupTypeRegistrationEvent e) {
		e.addRegistration("Owned WorldGuard regions", new WorldGuardRangeGroupType());
	}
}
