package io.github.kevinlovely.egs;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bedwarsrel.com.v1_8_r3.Title;
import io.github.bedwarsrel.events.BedwarsGameOverEvent;
import io.github.bedwarsrel.events.BedwarsPlayerKilledEvent;
import io.github.bedwarsrel.game.Game;
import net.md_5.bungee.api.ChatColor;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.TextEffect;
import de.slikey.effectlib.effect.VortexEffect;
import de.slikey.effectlib.effect.WarpEffect;

public class Main extends JavaPlugin implements Listener{
	
	private EffectManager effectManager;
	private FileConfiguration config;
	
	public Map<Game,Player> lastkills = new HashMap<Game,Player>();

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		effectManager = new EffectManager(this);
		this.saveDefaultConfig();
		config = this.getConfig();
		Metrics metrics = new Metrics(this);
		this.getServer().getConsoleSender().sendMessage("§e==========================");
		this.getServer().getConsoleSender().sendMessage("§c EndGameSurpriseV1.1.0");
		this.getServer().getConsoleSender().sendMessage("§a   起床终结拓展 > 加载成功");
		this.getServer().getConsoleSender().sendMessage("§c    By凯文QQ:3407053348   ");
		this.getServer().getConsoleSender().sendMessage("§e==========================");
	}
	
	@Override
    public void onDisable() {
        effectManager.dispose();
        HandlerList.unregisterAll((Listener) this);
		this.saveDefaultConfig();
		config = this.getConfig();
		this.getServer().getConsoleSender().sendMessage("§e==========================");
		this.getServer().getConsoleSender().sendMessage("§c EndGameSurpriseV1.1.0");
		this.getServer().getConsoleSender().sendMessage("§a   起床终结拓展 > 重载成功");
		this.getServer().getConsoleSender().sendMessage("§c    By凯文QQ:3407053348   ");
		this.getServer().getConsoleSender().sendMessage("§e==========================");

    }
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onGameOverEvent(BedwarsGameOverEvent e) {
		Game g = e.getGame();
		boolean flag = false;
		if(lastkills.containsKey(g)) {
			if(lastkills.get(g).isOnline()) {
				flag = true;
			}
		}
		for(Player p : g.getPlayers()) {
			p.sendMessage("§6§l==================================");
			p.sendMessage(" ");
			if(flag) {
				p.sendMessage("§a这场游戏的终结者是:  §c§l" + lastkills.get(g).getName());
			}else {
				p.sendMessage("      §e这场游戏没有终结者!");
			}
			p.sendMessage(" ");
			p.sendMessage("§6§l==================================");
			if(g.getPlayerTeam(p).equals(e.getWinner())) {
				//获胜队伍
				if(lastkills.get(g) == p) {
					//终结者
					if((boolean) this.config.get("TextEffect")) {
						this.makeEff(p, g, 1);
					}
					if((boolean) this.config.get("WarpEffect")) {
						this.makeEff(p, g, 2);
					}
					if((boolean) this.config.get("VortexEffect")) {
						this.makeEff(p, g, 3);
					}
					//this.endPlayer = p;
					//this.endGame = g;
					Title.showTitle(p, this.config.getString("EndTitle"), 1.0F, 20.0F,0F);
					Title.showSubTitle(p, this.config.getString("EndSubTitle"), 1.0F, 20.0F,0F);
					//p.getLocation().getWorld().playEffect(p.getLocation(), Effect.EXPLOSION_LARGE, 10);
				}else {
					Title.showTitle(p, this.config.getString("WinerTitle"), 1.0F, 20.0F,0F);
					String subTi = this.config.getString("NobodyEndGame");
					if(flag) {
						subTi = ChatColor.RED + "终结: " + lastkills.get(g).getName();
					}
					Title.showSubTitle(p, subTi, 1.0F, 20.0F,0F);	
					}
				}else {
				//失败队伍
				Title.showTitle(p, "§e终结:§c§l" + lastkills.get(g).getName(), 1.0F, 20.0F,0F);
				Title.showSubTitle(p, e.getWinner().getChatColor() + "§l" + e.getWinner().getName()+"§f获胜!", 1.0F, 20.0F,1.0F);
			}
		}
	}
	
	@EventHandler
	public void onBedwarsPlayerKilledEvent(BedwarsPlayerKilledEvent e) {
		lastkills.put(e.getGame(),e.getKiller());
	}

	public void makeEff(Player p, Game g, int Type) {
		switch (Type) {
		case 1:
			TextEffect b1 = new TextEffect(effectManager);
			b1.setEntity(p);
			b1.iterations = 5 * 20;
			b1.text = p.getName();
			b1.size = (float) 0.1;
			if(g != null) {
				b1.color = g.getPlayerTeam(p).getColor().getColor();
			}
			b1.start();
			break;
		case 2:
			WarpEffect b2 = new WarpEffect(effectManager);
			b2.color = g.getPlayerTeam(p).getColor().getColor();
			b2.setEntity(p);
			b2.iterations = 5 * 20;
			b2.start();
			break;
		case 3:
			VortexEffect b3 = new VortexEffect(effectManager);
			b3.setEntity(p);
			b3.iterations = 5 * 20;
			b3.start();
			break;
		default:
			break;
		}

	}

}
