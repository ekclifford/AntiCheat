package me.rida.anticheat.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

import me.rida.anticheat.checks.Check;

import java.util.*;

public class DataManager {
	private List<Check> checks;
    private Map<Player, Map<Check, Integer>> violations;
    private List<DataPlayer> players;
    
    private Set<DataPlayer> dataSet = new HashSet<>();

    public DataManager() {
        Bukkit.getOnlinePlayers().forEach(this::add);
        checks = new ArrayList<>();
        violations = new WeakHashMap<>();
        players = new ArrayList<>();
    }

    public DataPlayer getDataPlayer(Player p) {
        return dataSet.stream().filter(dataPlayer -> dataPlayer.player == p).findFirst().orElse(null);
    }

    public void add(Player p) {
        dataSet.add(new DataPlayer(p));
    }

    public void remove(Player p) {
        dataSet.removeIf(dataPlayer -> dataPlayer.player == p);
    }

    public void removeCheck(Check c) {
        if(checks.contains(c)) checks.remove(c);
    }

    public boolean isCheck(Check c) {
        return checks.contains(c);
    }

    public Check getCheckAyName(String cn) {
        for(Check checkLoop : Collections.synchronizedList(checks)) {
            if(checkLoop.getName().equalsIgnoreCase(cn)) return checkLoop;
        }

        return null;
    }

    public Map<Player, Map<Check, Integer>> getViolationsMap() {
        return violations;
    }

    public int getViolatonsPlayer(Player p, Check c) {
        if(violations.containsKey(p)) {
            Map<Check, Integer> vlMap = violations.get(p);

            return vlMap.getOrDefault(c, 0);
        }
        return 0;
    }

    public void addViolation(Player p, Check c) {
        if (violations.containsKey(p)) {
            Map<Check, Integer> vlMap = violations.get(p);

            vlMap.put(c, vlMap.getOrDefault(c, 0) + 1);
            violations.put(p, vlMap);
        } else {
            Map<Check, Integer> vlMap = new HashMap<>();

            vlMap.put(c, 1);

            violations.put(p, vlMap);
        }
    }

    public void addPlayerData(Player p) {
        players.add(new DataPlayer(p));
    }

    public DataPlayer getData(Player p) {
        for(DataPlayer dataLoop : Collections.synchronizedList(players)) {
            if(dataLoop.getPlayer() == p) {
                return dataLoop;
            }
        }
        return null;
    }

    public void removePlayerData(Player p) {
        for(DataPlayer dataLoop : Collections.synchronizedList(players)) {
            if(dataLoop.getPlayer() == p) {
                players.remove(dataLoop);
                break;
            }
        }
    }


    public void addCheck(Check c) {
        if(!checks.contains(c)) checks.add(c);
    }
    public List<Check> getChecks() {
        return checks;
    }
}